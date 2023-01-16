import java.time.Duration;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class DatabasesIntegration {

	private final DatabaseApi oracleDb;
	private final DatabaseApi fileDb;

	public DatabasesIntegration(DatabaseApi oracleDb, DatabaseApi fileDb) {
		this.oracleDb = oracleDb;
		this.fileDb = fileDb;
	}

	public Mono<Void> storeToDatabases(Flux<Integer> integerFlux) {
		// TODO: Main) Write data to both databases
		// TODO: 1) Ensure Transaction is rolled back in case of failure
		// TODO: 2) Ensure All transactions are rolled back ion case any of write operations fails
		// TODO: 3) Ensure Transaction lasts less than 1 sec

		return integerFlux
				.publish(integerFlux1 -> dbWriteInTransaction(oracleDb, integerFlux1)
						.zipWith(dbWriteInTransaction(fileDb, integerFlux1))
						.flatMap(objects -> {
							if (objects.getT1().error() == null && objects.getT2().error() == null) {
								return Mono.empty();
							} else if (objects.getT1().error() != null) {
								fileDb.rollbackTransaction(objects.getT2().transactionId());
								return Mono.error(objects.getT1().error());
							} else {
								oracleDb.rollbackTransaction(objects.getT1().transactionId());
								return Mono.error(objects.getT2().error());
							}
						})
				)
				.then();
	}

    static Mono<Result> dbWriteInTransaction(DatabaseApi db, Flux<Integer> dataSource) {
        return Mono.usingWhen(
				db.<Integer>open().retry(10),
				objectConnection -> objectConnection.write(dataSource),
				Connection::close,
				(objectConnection, throwable) -> objectConnection.rollback().then(objectConnection.close()),
				objectConnection ->objectConnection.rollback().then(objectConnection.close())
		)
				.map(id -> (Result) new SuccessResult(id))
				.timeout(Duration.ofMillis(1000))
				.onErrorResume(throwable -> Mono.just(new ErrorResult(throwable)));
    }
}
