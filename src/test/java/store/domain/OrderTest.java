package store.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

	@DisplayName("주문을 생성할 수 있다.")
	@Test
	void create() {

		// given
		Promotion promotion01 = Promotion.of("Frod Promotion", 1, 1,
			LocalDateTime.of(2001, 4, 21, 0, 0),
			LocalDateTime.of(2001, 5, 21, 0, 0)
		);
		Promotion promotion02 = Promotion.of("Roy Promotion", 1, 1,
			LocalDateTime.of(2001, 5, 11, 0, 0),
			LocalDateTime.of(2001, 6, 11, 0, 0)
		);

		Product product01 = Product.create("제로콜라", 2000, promotion01);
		Product product02 = Product.create("아메리카노", 3000, promotion02);
		Product product03 = Product.create("녹차라떼", 4000, Promotion.getNoneInstance());

		Map<Product, Integer> orders = Map.of(
			product01, 1,
			product02, 2,
			product03, 3
		);

		// when
		Order order = Order.create(orders);

		// then
		assertThat(order.getOrders())
			.hasSize(3)
			.contains(
				entry(product01, 1),
				entry(product02, 2),
				entry(product03, 3)
			);

	}
}