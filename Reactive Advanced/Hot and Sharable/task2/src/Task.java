import java.util.concurrent.atomic.AtomicInteger;
import org.reactivestreams.Publisher;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.EmitFailureHandler;

public class Task {

	public static Publisher<String> replayLast3ElementsInHotFashion1(Flux<String> coldSource) {
		return coldSource.replay(3).autoConnect();
	}

	public static Publisher<String> replayLast3ElementsInHotFashion2(Flux<String> coldSource) {
		final Sinks.Many<String> replaySink = Sinks.many().replay().limit(3);
		final Disposable subscribe = coldSource.subscribe(
				s -> replaySink.emitNext(s, EmitFailureHandler.FAIL_FAST),
				throwable -> replaySink.emitError(throwable, EmitFailureHandler.FAIL_FAST),
				() -> replaySink.emitComplete(EmitFailureHandler.FAIL_FAST)
		);

		AtomicInteger cnt = new AtomicInteger();
		return replaySink.asFlux()
				.doOnSubscribe(subscription -> cnt.getAndIncrement())
				.doFinally(signalType -> {
					if (cnt.decrementAndGet() == 0) {
						subscribe.dispose();
					}
				});
	}
}