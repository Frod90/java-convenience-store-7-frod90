package store.io.input.validation;

import static store.common.ErrorMessage.*;

import java.util.Arrays;
import java.util.regex.Pattern;

public class UserInputValidation {

	private static final Pattern NATURAL_NUMBER_PATTERN = Pattern.compile("^0*[1-9][0-9]*$");

	public void validateBlankInputOrder(String inputOrder) {
		if (inputOrder.isBlank()) {
			throw new IllegalArgumentException(INCORRECT_INPUT_FORMAT.getMessage());
		}
	}

	public void validateInputOrder(String[] inputOrders, String productNameAndQuantityDelimiter,
		char orderPrefix, char orderSuffix) {
		Arrays.stream(inputOrders)
			.map(String::trim)
			.forEach(inputOrder -> {
				validateInputOrder(inputOrder, orderPrefix, orderSuffix);
				validateProductNameAndQuantityDelimiter(inputOrder, productNameAndQuantityDelimiter);
				validateQuantity(inputOrder, productNameAndQuantityDelimiter, orderSuffix);
			});
	}

	private void validateProductNameAndQuantityDelimiter(String inputOrder, String productNameAndQuantityDelimiter) {
		if (!inputOrder.contains(productNameAndQuantityDelimiter)) {
			throw new IllegalArgumentException(INCORRECT_INPUT_FORMAT.getMessage());
		}
	}

	private void validateInputOrder(String inputOrder, char orderPrefix, char orderSuffix) {
		if (inputOrder.charAt(0) != orderPrefix || inputOrder.charAt(inputOrder.length() - 1) != orderSuffix) {
			throw new IllegalArgumentException(INCORRECT_INPUT_FORMAT.getMessage());
		}
	}

	private void validateQuantity(String inputOrder, String productNameAndQuantityDelimiter, char orderSuffix) {
		int numberStartIndex =
			inputOrder.indexOf(productNameAndQuantityDelimiter) + productNameAndQuantityDelimiter.length();
		int endNumberIndex = inputOrder.indexOf(orderSuffix);
		String inputQuantity = inputOrder.substring(numberStartIndex, endNumberIndex);

		if (!NATURAL_NUMBER_PATTERN.matcher(inputQuantity).matches()) {
			throw new IllegalArgumentException(INCORRECT_INPUT_FORMAT.getMessage());
		}
	}

	public void validateAcceptanceAndRefuseSign(String userInput, String acceptanceSign, String refuseSign) {
		if (!acceptanceSign.equalsIgnoreCase(userInput) && !refuseSign.equalsIgnoreCase(userInput)) {
			throw new IllegalArgumentException(INCORRECT_INPUT_FORMAT.getMessage());
		}
	}

}
