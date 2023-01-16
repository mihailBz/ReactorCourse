import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;

public class Task {

	public static Mono<String> grabDataFromTheGivenContext(Object key) {
		return Mono.deferContextual(contextView -> Mono.just(contextView.get(key)));
	}
}