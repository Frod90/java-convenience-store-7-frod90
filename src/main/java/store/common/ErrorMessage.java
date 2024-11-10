package store.common;

public enum ErrorMessage {

	ERROR_SIGN("[ERROR] "),
	CAN_NOT_READ_FILE("파일을 읽어오는데 실패했습니다."),
	INCREMENT_NOT_NATURAL_NUMBER("수량 증가는 0 초과의 숫자만 입력가능합니다."),
	NOT_EXIST_PRODUCT("존재하지 않는 상품입니다. 다시 입력해 주세요."),
	OVER_FLOW_STOCK_QUANTITY("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."),
	INCORRECT_INPUT_FORMAT("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");

	private final String message;

	ErrorMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return ERROR_SIGN.message + message;
	}
}
