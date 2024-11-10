package store.io;

import java.time.LocalDateTime;
import java.util.Map;

import store.domain.Product;
import store.domain.Receipts;
import store.domain.Stocks;
import store.io.input.InputHandler;
import store.io.output.OutputHandler;

public class IOHandler {

	private final InputHandler inputHandler;
	private final OutputHandler outputHandler;

	public IOHandler(InputHandler inputHandler, OutputHandler outputHandler) {
		this.inputHandler = inputHandler;
		this.outputHandler = outputHandler;
	}

	public void showStocks(Stocks stocks, LocalDateTime openDateTime) {
		outputHandler.showWelcomeMessage();
		outputHandler.showStocks(stocks, openDateTime);
	}

	public void showReceipt(Receipts receipts) {
		outputHandler.showReceipt(receipts);
	}

	public boolean hasRepurchase() {
		while (true) {
			try {
				return inputHandler.hasRepurchase();
			} catch (IllegalArgumentException illegalArgumentException) {
				outputHandler.showErrorMessage(illegalArgumentException.getMessage());
			}
		}
	}

	public boolean hasMemberShip() {
		while (true) {
			try {
				return inputHandler.hasMemberShip();
			} catch (IllegalArgumentException illegalArgumentException) {
				outputHandler.showErrorMessage(illegalArgumentException.getMessage());
			}
		}
	}

	public boolean hasAdditionalPurchaseAboutExtraQuantity(Product product, int extraQuantity) {
		while (true) {
			try {
				return inputHandler.hasAdditionalPurchaseAboutExtraQuantity(product, extraQuantity);
			} catch (IllegalArgumentException illegalArgumentException) {
				outputHandler.showErrorMessage(illegalArgumentException.getMessage());
			}
		}
	}

	public boolean hasNotAdditionalPurchaseUnApplicableQuantity(Product product, int unApplicableQuantity) {
		while (true) {
			try {
				return inputHandler.hasNotAdditionalPurchaseUnApplicableQuantity(product, unApplicableQuantity);
			} catch (IllegalArgumentException illegalArgumentException) {
				outputHandler.showErrorMessage(illegalArgumentException.getMessage());
			}
		}
	}

	public Map<String, Integer> getOrder() {
		return inputHandler.getOrder();
	}

	public void showErrorMessage(String message) {
		outputHandler.showErrorMessage(message);
	}
}
