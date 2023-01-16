import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class UserActivityUtils {

	public static Mono<Product> findMostExpansivePurchase(Flux<Order> ordersHistory,
			ProductsCatalog productsCatalog) {

		return ordersHistory
				.flatMapIterable(Order::getProductsIds)
				.map(productsCatalog::findById)
				.reduce((p1, p2) -> {
					if (p1.getPrice() > p2.getPrice()){
						return p1;
					} else {
						return p2;
					}
				});
	}
}
