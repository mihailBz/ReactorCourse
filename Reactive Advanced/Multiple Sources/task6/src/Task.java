import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.function.TupleUtils;

public class Task {

	public static Publisher<String> zipSeveralSources(Publisher<String> prefixPublisher,
			Publisher<String> wordPublisher,
			Publisher<String> suffixPublisher) {
		return Flux.zip(prefixPublisher, wordPublisher, suffixPublisher)
				   .map(v -> v.getT1()+v.getT2()+v.getT3());
	}
}