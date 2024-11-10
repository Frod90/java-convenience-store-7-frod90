package store.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ReceiptsTest {

	@DisplayName("구매 결과 목록을 생성할 수 있다.")
	@Test
	void from() {

		// given
		Promotion promotion01 = Promotion.of("Frod Promotion", 1, 1,
			LocalDateTime.of(2001, 4, 21, 0, 0),
			LocalDateTime.of(2001, 5, 21, 0, 0)
		);
		Promotion promotion02 = Promotion.of("Roy Promotion", 2, 1,
			LocalDateTime.of(2001, 5, 11, 0, 0),
			LocalDateTime.of(2001, 6, 11, 0, 0)
		);
		Promotion promotion03 = Promotion.of("Hana Promotion", 2, 2,
			LocalDateTime.of(2001, 7, 1, 0, 0),
			LocalDateTime.of(2001, 8, 1, 0, 0)
		);

		Product product01 = Product.create("티즐", 1600, promotion01);
		Product product02 = Product.create("아메리카노", 2000, promotion02);
		Product product03 = Product.create("초콜릿", 3000, promotion03);

		Receipt receipt01 = Receipt.of(product01, 2, 1);
		Receipt receipt02 = Receipt.of(product02, 3, 0);
		Receipt receipt03 = Receipt.of(product03, 4, 2);

		MemberShip memberShip = MemberShip.of(true);

		// when
		Receipts receipts = Receipts.from(List.of(receipt01, receipt02, receipt03), memberShip);

		// then
		assertAll(
			() -> assertThat(receipts.getReceipts())
				.hasSize(3)
				.containsExactly(receipt01, receipt02, receipt03),
			() -> assertThat(receipts)
				.extracting("memberShip")
				.isEqualTo(memberShip)
		);
	}

	@DisplayName("총 구매 수량을 계산할 수 있다.")
	@Test
	void calculateTotalQuantity() {
		// given
		Promotion promotion01 = Promotion.of("Frod Promotion", 1, 1,
			LocalDateTime.of(2001, 4, 21, 0, 0),
			LocalDateTime.of(2001, 5, 21, 0, 0)
		);
		Promotion promotion02 = Promotion.of("Roy Promotion", 2, 1,
			LocalDateTime.of(2001, 5, 11, 0, 0),
			LocalDateTime.of(2001, 6, 11, 0, 0)
		);
		Promotion promotion03 = Promotion.of("Hana Promotion", 2, 2,
			LocalDateTime.of(2001, 7, 1, 0, 0),
			LocalDateTime.of(2001, 8, 1, 0, 0)
		);

		Product product01 = Product.create("티즐", 1600, promotion01);
		Product product02 = Product.create("아메리카노", 2000, promotion02);
		Product product03 = Product.create("초콜릿", 3000, promotion03);

		Receipt receipt01 = Receipt.of(product01, 2, 1);
		Receipt receipt02 = Receipt.of(product02, 3, 0);
		Receipt receipt03 = Receipt.of(product03, 4, 2);

		MemberShip memberShip = MemberShip.of(true);
		Receipts receipts = Receipts.from(List.of(receipt01, receipt02, receipt03), memberShip);

		// when
		long totalQuantity = receipts.calculateTotalQuantity();

		// then
		assertThat(totalQuantity).isEqualTo(9);

	}

	@DisplayName("총 구매 금액을 계산할 수 있다.")
	@Test
	void calculateTotalPrice() {
		// given
		Promotion promotion01 = Promotion.of("Frod Promotion", 1, 1,
			LocalDateTime.of(2001, 4, 21, 0, 0),
			LocalDateTime.of(2001, 5, 21, 0, 0)
		);
		Promotion promotion02 = Promotion.of("Roy Promotion", 2, 1,
			LocalDateTime.of(2001, 5, 11, 0, 0),
			LocalDateTime.of(2001, 6, 11, 0, 0)
		);
		Promotion promotion03 = Promotion.of("Hana Promotion", 2, 2,
			LocalDateTime.of(2001, 7, 1, 0, 0),
			LocalDateTime.of(2001, 8, 1, 0, 0)
		);

		Product product01 = Product.create("티즐", 1600, promotion01);
		Product product02 = Product.create("아메리카노", 2000, promotion02);
		Product product03 = Product.create("초콜릿", 3000, promotion03);

		Receipt receipt01 = Receipt.of(product01, 2, 1);
		Receipt receipt02 = Receipt.of(product02, 3, 0);
		Receipt receipt03 = Receipt.of(product03, 4, 2);

		MemberShip memberShip = MemberShip.of(true);
		Receipts receipts = Receipts.from(List.of(receipt01, receipt02, receipt03), memberShip);

		// when
		long totalPrice = receipts.calculateTotalPrice();

		// then
		assertThat(totalPrice).isEqualTo(1600 * 2 + 2000 * 3 + 3000 * 4);
	}

	@DisplayName("프로모션 할인 총 금액을 계산할 수 있다.")
	@Test
	void calculateTotalPromotionDiscount() {
		// given
		Promotion promotion01 = Promotion.of("Frod Promotion", 1, 1,
			LocalDateTime.of(2001, 4, 21, 0, 0),
			LocalDateTime.of(2001, 5, 21, 0, 0)
		);
		Promotion promotion02 = Promotion.of("Roy Promotion", 2, 1,
			LocalDateTime.of(2001, 5, 11, 0, 0),
			LocalDateTime.of(2001, 6, 11, 0, 0)
		);
		Promotion promotion03 = Promotion.of("Hana Promotion", 2, 2,
			LocalDateTime.of(2001, 7, 1, 0, 0),
			LocalDateTime.of(2001, 8, 1, 0, 0)
		);

		Product product01 = Product.create("티즐", 1600, promotion01);
		Product product02 = Product.create("아메리카노", 2000, promotion02);
		Product product03 = Product.create("초콜릿", 3000, promotion03);

		Receipt receipt01 = Receipt.of(product01, 2, 1);
		Receipt receipt02 = Receipt.of(product02, 3, 0);
		Receipt receipt03 = Receipt.of(product03, 4, 2);

		MemberShip memberShip = MemberShip.of(true);
		Receipts receipts = Receipts.from(List.of(receipt01, receipt02, receipt03), memberShip);

		// when
		long promotionDiscount = receipts.calculateTotalPromotionDiscount();

		// then
		assertThat(promotionDiscount).isEqualTo(7600);
	}

	@DisplayName("멤버십 할인 금액을 계산할 수 있다.")
	@CsvSource({
		"true, 1800",
		"false, 0"
	})
	@ParameterizedTest(name = "멤버십 적용 여부가 {0}일 때, 멤버십 할인 금액은 {1}이다.")
	void calculateMemberShipDiscount(boolean isActiveMemberShip, long expectedDiscountPrice) {
		// given
		Promotion promotion01 = Promotion.of("Frod Promotion", 1, 1,
			LocalDateTime.of(2001, 4, 21, 0, 0),
			LocalDateTime.of(2001, 5, 21, 0, 0)
		);
		Promotion promotion02 = Promotion.of("Roy Promotion", 2, 1,
			LocalDateTime.of(2001, 5, 11, 0, 0),
			LocalDateTime.of(2001, 6, 11, 0, 0)
		);
		Promotion promotion03 = Promotion.of("Hana Promotion", 2, 2,
			LocalDateTime.of(2001, 7, 1, 0, 0),
			LocalDateTime.of(2001, 8, 1, 0, 0)
		);

		Product product01 = Product.create("티즐", 1600, promotion01);
		Product product02 = Product.create("아메리카노", 2000, promotion02);
		Product product03 = Product.create("초콜릿", 3000, promotion03);

		Receipt receipt01 = Receipt.of(product01, 2, 1);
		Receipt receipt02 = Receipt.of(product02, 3, 0);
		Receipt receipt03 = Receipt.of(product03, 4, 2);

		MemberShip memberShip = MemberShip.of(isActiveMemberShip);
		Receipts receipts = Receipts.from(List.of(receipt01, receipt02, receipt03), memberShip);

		// when
		long discount = receipts.calculateMemberShipDiscount();

		// then
		assertThat(discount).isEqualTo(expectedDiscountPrice);
	}

	@DisplayName("총 결제 금액을 계산할 수 있다.")
	@Test
	void calculateTotalPayment() {

		// given
		Promotion promotion01 = Promotion.of("Frod Promotion", 1, 1,
			LocalDateTime.of(2001, 4, 21, 0, 0),
			LocalDateTime.of(2001, 5, 21, 0, 0)
		);
		Promotion promotion02 = Promotion.of("Roy Promotion", 2, 1,
			LocalDateTime.of(2001, 5, 11, 0, 0),
			LocalDateTime.of(2001, 6, 11, 0, 0)
		);
		Promotion promotion03 = Promotion.of("Hana Promotion", 2, 2,
			LocalDateTime.of(2001, 7, 1, 0, 0),
			LocalDateTime.of(2001, 8, 1, 0, 0)
		);

		Product product01 = Product.create("티즐", 1600, promotion01);
		Product product02 = Product.create("아메리카노", 2000, promotion02);
		Product product03 = Product.create("초콜릿", 3000, promotion03);

		Receipt receipt01 = Receipt.of(product01, 2, 1);
		Receipt receipt02 = Receipt.of(product02, 3, 0);
		Receipt receipt03 = Receipt.of(product03, 4, 2);

		MemberShip memberShip = MemberShip.of(true);
		Receipts receipts = Receipts.from(List.of(receipt01, receipt02, receipt03), memberShip);

		// when
		long totalPayment = receipts.calculateTotalPayment();

		// then
		assertThat(totalPayment).isEqualTo(11800);
	}
}