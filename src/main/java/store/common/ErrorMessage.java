package store.common;

public enum ErrorMessage {

	ERROR_SIGN("[ERROR] "),
	CAN_NOT_READ_FILE("파일을 읽어오는데 실패했습니다."),
	INCREMENT_NOT_NATURAL_NUMBER("수량 증가는 0 초과의 숫자만 입력가능합니다.");

	private final String message;

	ErrorMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return ERROR_SIGN.message + message;
	}
}
