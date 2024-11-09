package store.domain;

import java.util.Map;

public class Stocks {

	private final Map<String, Stock> stocks;

	private Stocks(Map<String, Stock> stocks) {
		this.stocks = stocks;
	}

	public static Stocks from(Map<String, Stock> stocks) {
		return new Stocks(stocks);
	}

	public Map<String, Stock> getStocks() {
		return stocks;
	}
}
