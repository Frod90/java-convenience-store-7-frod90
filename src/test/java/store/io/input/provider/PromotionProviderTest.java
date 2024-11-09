package store.io.input.provider;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import store.domain.Promotions;

class PromotionProviderTest {

	@DisplayName("파일을 읽어와 프로모션 목록을 생성할 수 있다.")
	@Test
	void provide() {

		// given
		PromotionProvider promotionProvider = new PromotionProvider();
		String filePath = "src/test/resources/test_promotions.md";

		// when
		Promotions promotions = promotionProvider.provide(filePath);

		// then
		assertThat(promotions).extracting("promotions")
			.extracting("frod promotion", "roy promotion", "hana promotion")
			.extracting("name", "buy", "get", "startDate", "endDate")
			.containsExactly(
				tuple("frod promotion", 1, 1,
					LocalDateTime.of(2024, 1, 1, 0, 0),
					LocalDateTime.of(2024, 2, 22, 0, 0)),
				tuple("roy promotion", 2, 1,
					LocalDateTime.of(2024, 2, 1, 0, 0),
					LocalDateTime.of(2024, 4, 1, 0, 0)),
				tuple("hana promotion", 2, 2,
					LocalDateTime.of(2024, 3, 1, 0, 0),
					LocalDateTime.of(2024, 5, 1, 0, 0))
			);
	}
}