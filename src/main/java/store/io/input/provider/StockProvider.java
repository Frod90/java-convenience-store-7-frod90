package store.io.input.provider;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import store.domain.Product;
import store.domain.Promotions;
import store.domain.Stock;
import store.domain.Stocks;
import store.util.StoreFileReader;

public class StockProvider {

	private static final int NAME_INDEX = 0;
	private static final int PRICE_INDEX = 1;
	private static final int QUANTITY_INDEX = 2;
	private static final int PROMOTION_INDEX = 3;
	private static final String NONE_PROMOTION_SIGN = "null";

	public Stocks provide(Promotions promotions, String productFilePath) {
		List<String[]> infos = StoreFileReader.readWithoutHeader(productFilePath);
		Map<String, Stock> stocks = createStocks(promotions, infos);
		return Stocks.from(stocks);
	}

	private Map<String, Stock> createStocks(Promotions promotions, List<String[]> infos) {
		Map<String, Stock> stocks = createInitialStocks(promotions, infos);

		for (String[] info : infos) {
			updateQuantity(info, stocks.get(info[NAME_INDEX]));
		}
		return stocks;
	}

	private Map<String, Stock> createInitialStocks(Promotions promotions, List<String[]> infos) {
		return infos.stream()
			.collect(Collectors.toMap(
				info -> info[NAME_INDEX],
				info -> createStock(promotions, info),
				this::mergeStock,
				LinkedHashMap::new
			));
	}

	private Stock createStock(Promotions promotions, String[] info) {
		Product product = Product.create(
			info[NAME_INDEX],
			Integer.parseInt(info[PRICE_INDEX]),
			promotions.findBy(info[PROMOTION_INDEX])
		);
		return Stock.from(product);
	}

	private Stock mergeStock(Stock oldStock, Stock newStock) {
		if (oldStock.hasPromotionProduct()) {
			return oldStock;
		}

		return newStock;
	}

	private void updateQuantity(String[] info, Stock stock) {
		if (NONE_PROMOTION_SIGN.equals(info[PROMOTION_INDEX])) {
			stock.incrementGeneralQuantity(Integer.parseInt(info[QUANTITY_INDEX]));
		}

		if (!NONE_PROMOTION_SIGN.equals(info[PROMOTION_INDEX])) {
			stock.incrementPromotionQuantity(Integer.parseInt(info[QUANTITY_INDEX]));
		}
	}

}