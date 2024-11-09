package store.domain;

import java.util.Map;

public class Promotions {

	private final Map<String, Promotion> promotions;

	private Promotions(Map<String, Promotion> promotions) {
		this.promotions = promotions;
	}

	public static Promotions from(Map<String, Promotion> promotions) {
		return new Promotions(promotions);
	}

	public Promotion findBy(String promotionName) {
		return promotions.getOrDefault(promotionName, Promotion.getNoneInstance());
	}
}
