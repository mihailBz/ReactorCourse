import java.util.concurrent.CountDownLatch;

import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;

public class Task {

	public static void dynamicDemand(Flux<String> source, CountDownLatch countDownOnComplete) {

		source.subscribe(new BaseSubscriber<String>() {

			int r = 1;
			int cnt = 0;

			@Override
			protected void hookOnSubscribe(Subscription subscription) {
				request(r);
			}

			@Override
			protected void hookOnNext(String value) {
				cnt++;

				if (cnt == r) {
					cnt = 0;
					r *= 2;
					request(r);
				}
			}

			@Override
			protected void hookFinally(SignalType type) {
				countDownOnComplete.countDown();
			}
		});
	}
}