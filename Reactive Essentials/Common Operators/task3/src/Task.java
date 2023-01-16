import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.util.function.Function;

public class Task {

	public static Flux<Character> createSequence(Flux<String> stringFlux) {
		return stringFlux
                .flatMap(s -> Flux.just(s.split("")))
                .map(s -> s.charAt(0));
	}
}
