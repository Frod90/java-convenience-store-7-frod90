package store.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
			.contains(
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
}