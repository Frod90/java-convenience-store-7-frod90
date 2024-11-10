package store;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import camp.nextstep.edu.missionutils.DateTimes;
import store.domain.MemberShip;
import store.domain.Order;
import store.domain.Product;
import store.domain.PromotionResult;
import store.domain.Promotions;
import store.domain.Receipt;
import store.domain.Receipts;
import store.domain.Stocks;
import store.io.IOHandler;
import store.io.input.provider.PromotionProvider;
import store.io.input.provider.StockProvider;

public class ConvenienceStore {

	private static final String PROMOTION_FILE_PATH = "src/main/resources/promotions.md";
	private static final String PRODUCT_FILE_PATH = "src/main/resources/products.md";

	private final IOHandler ioHandler;
	private final PromotionProvider promotionProvider;
	private final StockProvider stockProvider;

	public ConvenienceStore(IOHandler ioHandler, PromotionProvider promotionProvider, StockProvider stockProvider) {
		this.ioHandler = ioHandler;
		this.promotionProvider = promotionProvider;
		this.stockProvider = stockProvider;
	}

	public void run() {
		LocalDateTime openDateTime = DateTimes.now();

		Promotions promotions = promotionProvider.provide(PROMOTION_FILE_PATH);
		Stocks stocks = stockProvider.provide(promotions, PRODUCT_FILE_PATH);

		openStore(stocks, openDateTime);
	}

	private void openStore(Stocks stocks, LocalDateTime openDateTime) {
		do {
			ioHandler.showStocks(stocks, openDateTime);
			Order order = createOrder(stocks);

			Receipts receipts = createReceipts(stocks, order, openDateTime);
			stocks.deductQuantity(receipts, openDateTime);

			ioHandler.showReceipt(receipts);
		} while (ioHandler.hasRepurchase());
	}

	private Order createOrder(Stocks stocks) {
		while (true) {
			try {
				Map<String, Integer> inputOrder = ioHandler.getOrder();
				return createOrder(inputOrder, stocks);

			} catch (IllegalArgumentException | IllegalStateException e) {
				ioHandler.showErrorMessage(e.getMessage());
			}
		}
	}

	private Order createOrder(Map<String, Integer> inputOrders, Stocks stocks) {
		Map<Product, Integer> orders = inputOrders.entrySet().stream()
			.map(inputOrder -> stocks.findAvailableProductBy(inputOrder.getKey(), inputOrder.getValue()))
			.collect(Collectors.toMap(
				product -> product,
				product -> inputOrders.get(product.getName()),
				Integer::sum,
				LinkedHashMap::new
			));
		return Order.create(orders);
	}

	private Receipts createReceipts(Stocks stocks, Order order, LocalDateTime openDateTime) {
		Map<Product, Integer> orders = order.getOrders();

		List<Receipt> receipts = orders.entrySet().stream()
			.map(orderEntry -> getReceipt(stocks, openDateTime, orderEntry.getKey(), orderEntry.getValue()))
			.toList();

		MemberShip memberShip = createMemberShip();
		return Receipts.from(receipts, memberShip);
	}

	private MemberShip createMemberShip() {
		boolean hasMemberShip = ioHandler.hasMemberShip();
		return MemberShip.of(hasMemberShip);
	}

	private Receipt getReceipt(Stocks stocks, LocalDateTime openDateTime, Product product, int purchasedQuantity) {
		PromotionResult promotionResult = stocks.calculatePromotion(product.getName(), purchasedQuantity, openDateTime);
		return createReceiptWithActivePromotion(promotionResult, product, purchasedQuantity);
	}

	private Receipt createReceiptWithActivePromotion(PromotionResult promotionResult, Product product,
		int purchasedQuantity) {

		if (promotionResult.hasExtraQuantity()) {
			return createReceiptWithExtraQuantityIntention(promotionResult, product, purchasedQuantity);
		}
		if (promotionResult.hasUnApplicableQuantity()) {
			return createReceiptWithUnApplicableQuantityIntention(promotionResult, product, purchasedQuantity);
		}
		return Receipt.of(product, purchasedQuantity, promotionResult.getFreeQuantity());
	}

	private Receipt createReceiptWithExtraQuantityIntention(PromotionResult promotionResult, Product product,
		int purchasedQuantity) {

		int freeQuantity = promotionResult.getFreeQuantity();
		int extraQuantity = promotionResult.getExtraQuantity();

		if (ioHandler.hasAdditionalPurchaseAboutExtraQuantity(product, extraQuantity)) {
			return Receipt.of(product, purchasedQuantity + extraQuantity, freeQuantity + extraQuantity);
		}
		return Receipt.of(product, purchasedQuantity, freeQuantity);
	}

	private Receipt createReceiptWithUnApplicableQuantityIntention(PromotionResult promotionResult, Product product,
		int purchasedQuantity) {

		int freeQuantity = promotionResult.getFreeQuantity();
		int unApplicableQuantity = promotionResult.getUnApplicableQuantity();

		if (ioHandler.hasNotAdditionalPurchaseUnApplicableQuantity(product, unApplicableQuantity)) {
			return Receipt.of(product, purchasedQuantity - unApplicableQuantity, freeQuantity);
		}
		return Receipt.of(product, purchasedQuantity, freeQuantity);
	}

}
