package store.io.output;

import java.time.LocalDateTime;
import java.util.Map;

import store.domain.Product;
import store.domain.Receipt;
import store.domain.Receipts;
import store.domain.Stock;
import store.domain.Stocks;

public class OutputHandler {

	private static final String WELCOME_MESSAGE = "안녕하세요. W편의점입니다.";
	private static final String PROMOTION_STOCK_FORMAT = "- %s %,d원 %s %s\n";
	private static final String GENERAL_STOCK_FORMAT = "- %s %,d원 %s\n";
	private static final String STOCK_ANNOUNCE_COMMENT = "현재 보유하고 있는 상품입니다.";
	private static final String COUNT_COMMENT = "개";
	private static final String EMPTY_QUANTITY_COMMENT = "재고 없음";
	private static final String START_RECEIPT_COMMENT = "==============W 편의점================";
	private static final String PURCHASE_DETAIL_HEADER = "상품명  \\t\\t\\t  수량  \\t\\t\\t 금액";
	private static final String PURCHASE_DETAIL_FORMAT = "%-15s %5d %10s\\n";
	private static final String PROMOTION_DETAIL_DELIMITER = "=============증      정===============";
	private static final String PROMOTION_DETAIL_FORMAT = "%-10s \t\t %d\n";
	private static final String PAYMENT_RESULT_DELIMITER = "====================================";
	private static final String TOTAL_PRICE_FROMAT = "총구매액   \t\t %d \t\t\t %,d\n";
	private static final String TOTAL_PROMOTION_PRICE_FROMAT = "행사할인   \t\t\t\t\t -%,d\n";
	private static final String MEMBERSHIP_DISCOUNT_FROMAT = "멤버십할인 \t\t\t\t\t -%,d\n";
	private static final String PAYMENT_RESULT_FROMAT = "내실돈     \t\t\t\t\t %,d\n";

	public void showWelcomeMessage() {
		System.out.println(WELCOME_MESSAGE);
	}

	public void showStocks(Stocks stocks, LocalDateTime now) {
		showStockAnnounceComment();

		Map<String, Stock> stockMap = stocks.getStocks();
		for (Stock stock : stockMap.values()) {
			Product product = stock.getProduct();
			showEachStock(now, stock, product);
		}
		System.out.println();
	}

	private void showStockAnnounceComment() {
		System.out.println(STOCK_ANNOUNCE_COMMENT);
		System.out.println();
	}

	private void showEachStock(LocalDateTime now, Stock stock, Product product) {
		if (product.hasActivePromotion(now)) {
			showPromotionStock(stock, product);
		}
		showGeneralStock(stock, product);
	}

	private void showPromotionStock(Stock stock, Product product) {
		String count = stock.getPromotionQuantity() + COUNT_COMMENT;
		if (stock.getPromotionQuantity() == 0) {
			count = EMPTY_QUANTITY_COMMENT;
		}

		System.out.printf(PROMOTION_STOCK_FORMAT,
			product.getName(), product.getPrice(), count, product.getPromotion().getName());
	}

	private void showGeneralStock(Stock stock, Product product) {
		String count = stock.getGeneralQuantity() + COUNT_COMMENT;
		if (stock.getGeneralQuantity() == 0) {
			count = EMPTY_QUANTITY_COMMENT;
		}

		System.out.printf(GENERAL_STOCK_FORMAT, product.getName(), product.getPrice(), count);
	}

	public void showReceipt(Receipts receipts) {
		showReceiptHeader();
		showPurchaseDetail(receipts);

		showPromotionDelimiter();
		showPromotionDetail(receipts);

		showPaymentDelimiter();
		showPaymentResult(receipts);
	}

	private void showReceiptHeader() {
		System.out.println();
		System.out.println(START_RECEIPT_COMMENT);
		System.out.println(PURCHASE_DETAIL_HEADER);
	}

	private void showPurchaseDetail(Receipts receipts) {
		for (Receipt receipt : receipts.getReceipts()) {
			String productName = receipt.getProduct().getName();
			long totalQuantity = receipt.getTotalQuantity();
			long purchasedPrice = receipt.calculatePurchasedPrice();

			String formattedPurchasePrice = String.format("%,d", purchasedPrice);
			System.out.printf(PURCHASE_DETAIL_FORMAT, productName, totalQuantity, formattedPurchasePrice);
		}
	}

	private void showPromotionDelimiter() {
		System.out.println(PROMOTION_DETAIL_DELIMITER);
	}

	private void showPromotionDetail(Receipts receipts) {
		for (Receipt receipt : receipts.getReceipts()) {
			if (receipt.getFreeQuantity() > 0) {
				String productName = receipt.getProduct().getName();
				int freeQuantity = receipt.getFreeQuantity();

				System.out.printf(PROMOTION_DETAIL_FORMAT, productName, freeQuantity);
			}
		}
	}

	private void showPaymentDelimiter() {
		System.out.println(PAYMENT_RESULT_DELIMITER);
	}

	private void showPaymentResult(Receipts receipts) {
		long totalQuantity = receipts.calculateTotalQuantity();
		long totalPrice = receipts.calculateTotalPrice();
		long promotionDiscount = receipts.calculateTotalPromotionDiscount();
		long memberShipDiscount = receipts.calculateMemberShipDiscount();
		long totalPayment = receipts.calculateTotalPayment();

		System.out.printf(TOTAL_PRICE_FROMAT, totalQuantity, totalPrice);
		System.out.printf(TOTAL_PROMOTION_PRICE_FROMAT, promotionDiscount);
		System.out.printf(MEMBERSHIP_DISCOUNT_FROMAT, memberShipDiscount);
		System.out.printf(PAYMENT_RESULT_FROMAT, totalPayment);
	}

	public void showErrorMessage(String message) {
		System.out.println(message);
	}
}
