package store.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MemberShipTest {

	@DisplayName("멤버십을 생성할 수 있다.")
	@CsvSource({
		"true, true",
		"false, false"
	})
	@ParameterizedTest(name = "활성화가 {1}인 멤버십을 생성할 수 있다.")
	void of(boolean isActive, boolean expect) {

		// when
		MemberShip memberShip = MemberShip.of(isActive);

		// then
		assertThat(memberShip)
			.extracting("isActive")
			.isEqualTo(expect);
	}

	@DisplayName("멤버십을 할인율을 조회할 수 있다.")
	@CsvSource({
		"true, 0.3",
		"false, 0"
	})
	@ParameterizedTest(name = "활성화가 {1}인 멤버십의 할인율은 {1}이다.")
	void getDiscountRate(boolean isActive, double expectedDiscountRate) {

		// given
		MemberShip memberShip = MemberShip.of(isActive);

		// when
		double discountRate = memberShip.getDiscountRate();

		// then
		assertThat(discountRate).isEqualTo(expectedDiscountRate);
	}
}