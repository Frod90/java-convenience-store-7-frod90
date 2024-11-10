package store.io.input.validation;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class UserInputValidationTest {

	@DisplayName("주문을 입력 받을 수 있다.")
	@Test
	void validateInputOrderWithoutError() {

		UserInputValidation userInputValidation = new UserInputValidation();
		String[] userInputs = {"[콜라-3]", "[사이다-5]"};

		assertDoesNotThrow(() ->
			userInputValidation.validateInputOrder(userInputs, "-", '[', ']'));
	}

	@DisplayName("주문 입력시 공백은 허용하지 않는다")
	@Test
	void validateBlankInputOrder() {

		// given
		UserInputValidation userInputValidation = new UserInputValidation();

		// when // then
		assertThatThrownBy(() -> userInputValidation.validateBlankInputOrder("    "))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
	}

	@DisplayName("prefix, suffix 사용해서 주문을 입력해야한다.")
	@ParameterizedTest(name = "{0}은 허용하지 않는다.")
	@ValueSource(strings = {"콜라-3", "[콜라-3}", "{콜라-1}"})
	void validateInputOrderWithWrongPrefixAndSuffix(String inputOrder) {

		UserInputValidation userInputValidation = new UserInputValidation();

		assertThatThrownBy(() ->
			userInputValidation.validateInputOrder(new String[] {inputOrder}, "-", '[', ']'))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
	}

	@DisplayName("제품 이름과 수량 사이는 지정된 구분자로 구별해야 한다.")
	@CsvSource({
		"[콜라+3]",
		"[사이다=5]"
	})
	@ParameterizedTest(name = "{0}은 허용하지 않는다.")
	void validateProductNameAndQuantityDelimiter(String inputOrder) {

		UserInputValidation userInputValidation = new UserInputValidation();

		assertThatThrownBy(() -> userInputValidation.validateInputOrder(new String[] {inputOrder}, "-", '[', ']'))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
	}

	@DisplayName("지정된 위치에 수량을 입력해야한다.")
	@CsvSource({
		"[콜라-]",
		"[5-사이다]"
	})
	@ParameterizedTest(name = "{0}은 허용하지 않는다.")
	void validateQuantity(String inputOrder) {

		UserInputValidation userInputValidation = new UserInputValidation();

		assertThatThrownBy(() -> userInputValidation.validateInputOrder(new String[] {inputOrder}, "-", '[', ']'))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
	}

	@DisplayName("대소문자 구분 없이 Y 또는 N을 입력하여 의사결정을 입력할 수 있다.")
	@CsvSource({
		"Y, Y, N",
		"N, Y, N",
		"y, Y, N",
		"n, Y, N",
		"Y, y, N",
		"Y, y, n",
	})
	@ParameterizedTest(name = "{0}은 허용한다.")
	void validateAcceptanceAndRefuseSignWithoutError(String userInput, String acceptanceSign, String refuseSign) {

		UserInputValidation userInputValidation = new UserInputValidation();

		assertDoesNotThrow(
			() -> userInputValidation.validateAcceptanceAndRefuseSign(userInput, acceptanceSign, refuseSign));
	}

	@DisplayName("의사 결정 입력시 Y/N을 제외한 입력은 허용하지 않는다.")
	@ValueSource(strings = {"A", "yes", "no"})
	@ParameterizedTest(name = "{0}은 허용하지 않는다.")
	void validateAcceptanceAndRefuseSign(String userInput) {

		UserInputValidation userInputValidation = new UserInputValidation();

		assertThatThrownBy(() -> userInputValidation.validateAcceptanceAndRefuseSign(userInput, "Y", "N"))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
	}
}
