package store.domain;

public class PromotionResult {

	private final int freeQuantity;
	private final int extraQuantity;
	private final int unApplicableQuantity;

	private PromotionResult(int freeQuantity, int extraQuantity, int unApplicableQuantity) {
		this.freeQuantity = freeQuantity;
		this.extraQuantity = extraQuantity;
		this.unApplicableQuantity = unApplicableQuantity;
	}

	public static PromotionResult of(int freeQuantity, int extraQuantity, int unApplicableQuantity) {
		return new PromotionResult(freeQuantity, extraQuantity, unApplicableQuantity);
	}

	public boolean hasExtraQuantity() {
		return extraQuantity > 0;
	}

	public boolean hasUnApplicableQuantity() {
		return unApplicableQuantity > 0;
	}

	public int getFreeQuantity() {
		return freeQuantity;
	}

	public int getExtraQuantity() {
		return extraQuantity;
	}

	public int getUnApplicableQuantity() {
		return unApplicableQuantity;
	}
}
