package store.util;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StoreFileReaderTest {

	@DisplayName("정해진 입력 형식을 지킨 파일을 읽을 수 있다.")
	@Test
	void readWithoutHeader() {

		// given
		String filePath = "src/test/resources/test_promotions.md";

		// when
		List<String[]> result = StoreFileReader.readWithoutHeader(filePath);

		// then
		assertThat(result).hasSize(3)
			.containsExactly(
				new String[] {"frod promotion", "1", "1", "2024-01-01", "2024-02-21"},
				new String[] {"roy promotion", "2", "1", "2024-02-01", "2024-03-31"},
				new String[] {"hana promotion", "2", "2", "2024-03-01", "2024-04-30"}
			);
	}
}