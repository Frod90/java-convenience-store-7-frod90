package store.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PromotionTest {

	@DisplayName("프로모션을 생성할 수 있다.")
	@Test
	void of() {

		// given
		String name = "Frod 행사";
		int buy = 2;
		int get = 2;
		LocalDateTime startDate = LocalDateTime.of(2001, 4, 21, 0, 0);
		LocalDateTime endDate = LocalDateTime.of(2001, 11, 27, 0, 0);

		// when
		Promotion promotion = Promotion.of(name, buy, get, startDate, endDate);

		// then
		assertThat(promotion)
			.extracting("name", "buy", "get", "startDate", "endDate")
			.containsExactly(
				name, buy, get, startDate, endDate
			);
	}

	@DisplayName("빈 프로모션을 조회할 수 있다.")
	@Test
	void getNoneInstance() {

		// when
		Promotion promotion = Promotion.getNoneInstance();

		// then
		assertThat(promotion)
			.extracting("name", "buy", "get", "startDate", "endDate")
			.contains(
				"none",
				0,
				0,
				LocalDateTime.of(2000, 1, 1, 0, 0, 0),
				LocalDateTime.of(2000, 1, 1, 0, 0, 0)
			);
	}

	@DisplayName("프로모션을 가지고 있는지 확인할 수 있다.")
	@Test
	void isPromotionWithPromotion() {
	    // given
		Promotion promotion = Promotion.of("Frod Promotion", 1, 1,
			LocalDateTime.of(2001, 4, 21, 0, 0),
			LocalDateTime.of(2001, 5, 21, 0, 0)
		);

	    // when
		boolean result = promotion.isPromotion();

		// then
		assertThat(result).isEqualTo(true);
	}

	@DisplayName("none 프로모션을 가지고 있는지 확인할 수 있다.")
	@Test
	void isPromotionWithNonePromotion() {
	    // given
		Promotion promotion = Promotion.getNoneInstance();

	    // when
		boolean result = promotion.isPromotion();

		// then
		assertThat(result).isEqualTo(false);
	}

	@DisplayName("프로모션이 활성화되어 있는지 확인할 수 있다.")
	@MethodSource("provideDateTimeForCheckingActive")
	@ParameterizedTest(name = "{0} 시각에 프로모션 활성화는 {1}이다")
	void isActive(LocalDateTime testDateTime, boolean expect) {
		// given
		Promotion promotion = Promotion.of("Frod Promotion", 1, 1,
			LocalDateTime.of(2001, 4, 21, 0, 0),
			LocalDateTime.of(2001, 5, 21, 0, 0)
		);

		// when
		boolean result = promotion.isActive(testDateTime);

		// then
		assertThat(result).isEqualTo(expect);
	}

	private static Stream<Arguments> provideDateTimeForCheckingActive() {
		return Stream.of(
			Arguments.of(LocalDateTime.of(2001, 4, 20, 23, 59, 59), false),
			Arguments.of(LocalDateTime.of(2001, 4, 21, 0, 0), true),
			Arguments.of(LocalDateTime.of(2001, 5, 20, 23, 59, 59), true),
			Arguments.of(LocalDateTime.of(2001, 5, 21, 0, 0, 0), false)
		);
	}


}