package store.domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Promotions {

	private final Map<String, Promotion> promotions;

	private Promotions(Map<String, Promotion> promotions) {
		this.promotions = promotions;
	}

	public static Promotions from(List<Promotion> promotions) {
		return new Promotions(promotions.stream()
			.collect(Collectors.toMap(Promotion::getName, promotion -> promotion)));
	}

	public Promotion findBy(String promotionName) {
		return promotions.getOrDefault(promotionName, Promotion.getNoneInstance());
	}
}
