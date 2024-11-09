package store.io.input.provider;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import store.domain.Promotion;
import store.domain.Promotions;
import store.util.StoreFileReader;

public class PromotionProvider {

	private static final int NAME_INDEX = 0;
	private static final int BUY_INDEX = 1;
	private static final int GET_INDEX = 2;
	private static final int START_DATE_INDEX = 3;
	private static final int END_DATE_INDEX = 4;

	public Promotions provide(String promotionFilePath) {
		List<String[]> readInfos = StoreFileReader.readWithoutHeader(promotionFilePath);
		return createPromotions(readInfos);
	}

	private Promotions createPromotions(List<String[]> readLinesWithoutHeader) {
		Map<String, Promotion> promotions = readLinesWithoutHeader.stream()
			.collect(Collectors.toMap(
				info -> info[NAME_INDEX],
				this::createPromotion,
				(oldPromotion, newPromotion) -> oldPromotion
			));

		return Promotions.from(promotions);
	}

	private Promotion createPromotion(String[] info) {
		return Promotion.of(
			info[NAME_INDEX],
			Integer.parseInt(info[BUY_INDEX]),
			Integer.parseInt(info[GET_INDEX]),
			LocalDateTime.of(LocalDate.parse(info[START_DATE_INDEX]), LocalTime.of(0, 0)),
			LocalDateTime.of(LocalDate.parse(info[END_DATE_INDEX]), LocalTime.of(0, 0)).plusDays(1)
		);
	}

}