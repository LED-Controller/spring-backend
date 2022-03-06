package de.dhbw.ledcontroller.payload.response;

import de.dhbw.ledcontroller.util.ResponseType;
import lombok.Data;

@Data
public class MessageResponse {

	private String message;
	private ResponseType type;

	public static class MessageResponseBuilder {

		private String message;
		private ResponseType type;

		public MessageResponseBuilder() {
		}

		MessageResponseBuilder(String message, ResponseType type) {
			this.message = message;
			this.type = type;
		}

		public MessageResponseBuilder message(String message) {
			this.message = message;
			return MessageResponseBuilder.this;
		}

		public MessageResponseBuilder type(ResponseType type) {
			this.type = type;
			return MessageResponseBuilder.this;
		}

		public MessageResponse build() {
			return new MessageResponse(this);
		}
	}

	private MessageResponse(MessageResponseBuilder builder) {
		this.message = builder.message;
		this.type = builder.type;
	}

	public static MessageResponse getLoginError() {
		return new MessageResponse.MessageResponseBuilder().message("please log in").type(ResponseType.ERROR).build();
	}

	public static MessageResponse getSecurityError() {
		return new MessageResponse.MessageResponseBuilder().message("not authorized to perform this operation")
				.type(ResponseType.ERROR).build();
	}

}
