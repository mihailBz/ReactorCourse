import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

public class Task {

	public static Publisher<String> combineSeveralSources(Publisher<String> prefixPublisher,
			Publisher<String> wordPublisher,
			Publisher<String> suffixPublisher) {

		return Flux.combineLatest(v -> v[0].toString()+v[1]+v[2],
				prefixPublisher, wordPublisher, suffixPublisher);
	}
}