package gov.nist.healthcare.tools.core.transport;

public class TransportServerException extends Exception {
	private static final long serialVersionUID = 1L;

	public TransportServerException() {
		super();
	}

	public TransportServerException(String error) {
		super(error);
	}

	public TransportServerException(String s, Throwable ex) {
		super(s, ex);
	}

	public TransportServerException(String s, Throwable ex, Object msg) {
		super(s, ex);
	}
}
