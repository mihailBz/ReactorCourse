import reactor.core.publisher.Flux;

import java.util.Objects;

public class Task {

	public static Flux<IceCreamBall> fillIceCreamWaffleBowl(Flux<IceCreamType> clientPreferences,
			Flux<IceCreamBall> vanillaIceCreamStream,
			Flux<IceCreamBall> chocolateIceCreamStream) {
		return clientPreferences.switchMap(v -> {
			if (Objects.equals(v.toString(), IceCreamType.VANILLA.toString())){
				return vanillaIceCreamStream;
			} else {
				return chocolateIceCreamStream;
			}
		});
	}

	static class IceCreamBall {

		private final String type;

		public IceCreamBall(String type) {
			this.type = type;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}

			IceCreamBall that = (IceCreamBall) o;

			return Objects.equals(type, that.type);
		}

		@Override
		public int hashCode() {
			return type != null ? type.hashCode() : 0;
		}

		public static IceCreamBall ball(String type) {
			return new IceCreamBall(type);
		}

		@Override
		public String toString() {
			return type;
		}
	}

	enum IceCreamType {
		VANILLA, CHOCOLATE
	}
}