package store.domain;

import java.util.List;

public class Receipts {

	private final List<Receipt> receipts;
	private final MemberShip memberShip;

	private Receipts(List<Receipt> receipts, MemberShip memberShip) {
		this.receipts = receipts;
		this.memberShip = memberShip;
	}

	public static Receipts from(List<Receipt> receipts, MemberShip memberShip) {
		return new Receipts(receipts, memberShip);
	}

	public long calculateTotalQuantity() {
		return receipts.stream()
			.mapToLong(Receipt::getTotalQuantity)
			.sum();
	}

	public long calculateTotalPrice() {
		return receipts.stream()
			.mapToLong(Receipt::calculatePurchasedPrice)
			.sum();
	}

	public long calculateTotalPromotionDiscount() {
		return receipts.stream()
			.mapToLong(Receipt::calculatePromotionDiscountPrice)
			.sum();
	}

	public long calculateMemberShipDiscount() {
		long netPrice = receipts.stream()
			.mapToLong(Receipt::calculateNetPurchasedPrice)
			.sum();

		return (long)(netPrice * memberShip.getDiscountRate());
	}

	public long calculateTotalPayment() {
		return calculateTotalPrice() - calculateTotalPromotionDiscount() - calculateMemberShipDiscount();
	}

	public List<Receipt> getReceipts() {
		return receipts;
	}
}
