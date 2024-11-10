package store.domain;

import java.time.LocalDateTime;

public class Promotion {

	private static final LocalDateTime DEFAULT_DATE_TIME =
		LocalDateTime.of(2000, 1, 1, 0, 0, 0);
	private static final String NO_PROMOTION_NAME = "none";
	private static final Promotion NONE_PROMOTION = new Promotion(
		NO_PROMOTION_NAME,
		0,
		0,
		DEFAULT_DATE_TIME,
		DEFAULT_DATE_TIME
	);

	private final String name;
	private final int buy;
	private final int get;
	private final LocalDateTime startDate;
	private final LocalDateTime endDate;

	private Promotion(String name, int buy, int get, LocalDateTime startDate, LocalDateTime endDate) {
		this.name = name;
		this.buy = buy;
		this.get = get;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public static Promotion of(String name, int buy, int get, LocalDateTime startDate, LocalDateTime endDate) {
		return new Promotion(name, buy, get, startDate, endDate);
	}

	public static Promotion getNoneInstance() {
		return NONE_PROMOTION;
	}

	public boolean isPromotion() {
		return !isNonePromotion();
	}

	private boolean isNonePromotion() {
		return this.equals(NONE_PROMOTION);
	}

	public boolean isActive(LocalDateTime now) {
		return !now.isBefore(startDate) && now.isBefore(endDate);
	}

	public String getName() {
		return name;
	}
}
