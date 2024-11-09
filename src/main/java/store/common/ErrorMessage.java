package store.common;

public enum ErrorMessage {

	ERROR_SIGN("[ERROR] "),
	CAN_NOT_READ_FILE("파일을 읽어오는데 실패했습니다.");

	private final String message;

	ErrorMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return ERROR_SIGN.message + message;
	}
}
