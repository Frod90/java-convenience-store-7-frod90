package store.io.input;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import camp.nextstep.edu.missionutils.Console;
import store.domain.Product;
import store.io.input.validation.UserInputValidation;

public class InputHandler {

	private static final String ORDER_DELIMITER = ",";
	private static final char ORDER_PREFIX = '[';
	private static final char ORDER_SUFFIX = ']';
	private static final String PRODUCT_NAME_AND_QUANTITY_DELIMITER = "-";
	private static final int PRODUCT_NAME_INDEX = 0;
	private static final int QUANTITY_INDEX = 1;
	private static final String INPUT_ORDERS_COMMENT = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
	private static final String INPUT_ADDITIONAL_PURCHASE_ABOUT_EXTRA_QUANTITY_COMMENT_FORMAT = "현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)\n";
	private static final String INPUT_ADDITIONAL_PURCHASE_UN_APPLICABLE_QUANTITY_COMMENT_FORMAT = "현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)\n";
	private static final String INPUT_MEMBERSHIP_COMMENT = "멤버십 할인을 받으시겠습니까? (Y/N)";
	private static final String INPUT_REPURCHASE_COMMENT = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";
	private static final String ACCEPTANCE_SIGN = "y";
	private static final String REFUSE_SIGN = "n";

	private final UserInputValidation userInputValidation;

	public InputHandler(UserInputValidation userInputValidation) {
		this.userInputValidation = userInputValidation;
	}

	public Map<String, Integer> getOrder() {
		System.out.println(INPUT_ORDERS_COMMENT);
		String userInput = Console.readLine();
		userInputValidation.validateBlankInputOrder(userInput);

		String[] splitUserInput = userInput.split(ORDER_DELIMITER);
		userInputValidation.validateInputOrder(splitUserInput, PRODUCT_NAME_AND_QUANTITY_DELIMITER,
			ORDER_PREFIX, ORDER_SUFFIX);

		return toOrderMap(splitUserInput);
	}

	private Map<String, Integer> toOrderMap(String[] splitUserInput) {
		return Arrays.stream(splitUserInput)
			.map(String::trim)
			.map(split -> split.substring(split.indexOf(ORDER_PREFIX) + 1, split.indexOf(ORDER_SUFFIX)))
			.map(split -> split.split(PRODUCT_NAME_AND_QUANTITY_DELIMITER))
			.collect(Collectors.toMap(
				split -> split[PRODUCT_NAME_INDEX],
				split -> Integer.parseInt(split[QUANTITY_INDEX]),
				Integer::sum,
				LinkedHashMap::new
			));
	}

	public boolean hasAdditionalPurchaseAboutExtraQuantity(Product product, int extraQuantity) {
		System.out.println();
		System.out.printf(INPUT_ADDITIONAL_PURCHASE_ABOUT_EXTRA_QUANTITY_COMMENT_FORMAT,
			product.getName(),
			extraQuantity);
		String userInput = Console.readLine();

		userInputValidation.validateAcceptanceAndRefuseSign(userInput, ACCEPTANCE_SIGN, REFUSE_SIGN);
		return ACCEPTANCE_SIGN.equalsIgnoreCase(userInput);
	}

	public boolean hasNotAdditionalPurchaseUnApplicableQuantity(Product product, int unApplicableQuantity) {
		System.out.println();
		System.out.printf(INPUT_ADDITIONAL_PURCHASE_UN_APPLICABLE_QUANTITY_COMMENT_FORMAT,
			product.getName(),
			unApplicableQuantity);
		String userInput = Console.readLine();

		userInputValidation.validateAcceptanceAndRefuseSign(userInput, ACCEPTANCE_SIGN, REFUSE_SIGN);
		return REFUSE_SIGN.equalsIgnoreCase(userInput);
	}

	public boolean hasMemberShip() {
		System.out.println();
		System.out.println(INPUT_MEMBERSHIP_COMMENT);
		String userInput = Console.readLine();

		userInputValidation.validateAcceptanceAndRefuseSign(userInput, ACCEPTANCE_SIGN, REFUSE_SIGN);
		return ACCEPTANCE_SIGN.equalsIgnoreCase(userInput);
	}

	public boolean hasRepurchase() {
		System.out.println();
		System.out.println(INPUT_REPURCHASE_COMMENT);
		String userInput = Console.readLine();

		userInputValidation.validateAcceptanceAndRefuseSign(userInput, ACCEPTANCE_SIGN, REFUSE_SIGN);
		return ACCEPTANCE_SIGN.equalsIgnoreCase(userInput);
	}
}
