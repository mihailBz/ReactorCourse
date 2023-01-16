import reactor.core.publisher.Flux;

public class Task {

	public static Flux<String> transformSequence(Flux<String> flux) {
		return flux.filter(v -> v.length() > 3);
	}
}