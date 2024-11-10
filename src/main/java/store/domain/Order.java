package store.domain;

import java.util.Map;

public class Order {
	private final Map<Product, Integer> orders;

	private Order(Map<Product, Integer> orders) {
		this.orders = orders;
	}

	public static Order create(Map<Product, Integer> orders) {
		return new Order(orders);
	}

	public Map<Product, Integer> getOrders() {
		return orders;
	}
}
