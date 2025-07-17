package de.wxdb.wxdb_masterthesis.dto;

/**
 * API Response DTO.
 * 
 * @author Kaan Mustafa Celik
 */
public class WxdbApiResponse {

	private Exception exception;
	private String response = "Successful";
	private String errorMessage;

	public Exception getException() {
		return exception;
	}

	public void setException(RuntimeException exception) {
		this.exception = exception;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String toString() {
		return "WdxbApiResponse [statusCode=" + exception + ", response=" + response + ", errorMessage=" + errorMessage
				+ "]";
	}

	public WxdbApiResponse(Exception exception, String response, String errorMessage) {
		super();
		this.exception = exception;
		this.response = response;
		this.errorMessage = errorMessage;
	}

	public WxdbApiResponse() {
		super();
	}

}
