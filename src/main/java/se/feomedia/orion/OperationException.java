package se.feomedia.orion;

public class OperationException extends RuntimeException {
	public OperationException() {
	}

	public OperationException(String message) {
		super(message);
	}

	public OperationException(String message, Throwable cause) {
		super(message, cause);
	}

	public OperationException(Throwable cause) {
		super(cause);
	}
}
