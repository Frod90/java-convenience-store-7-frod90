package store.domain;

public class MemberShip {

	private static final double DISCOUNT_RATE = 0.3;

	private final boolean isActive;

	public MemberShip(boolean isActive) {
		this.isActive = isActive;
	}

	public static MemberShip of(boolean isActive) {
		return new MemberShip(isActive);
	}

	public double getDiscountRate() {
		if (isActive) {
			return DISCOUNT_RATE;
		}

		return 0;
	}

}
