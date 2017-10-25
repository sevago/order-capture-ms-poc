package com.example.oc.mspoc.datatype;

public class ResponseMessage {
	
	private String messageType;
	private String messageText;
	
	public ResponseMessage(String messageType, String messageText){
		this.messageType = messageType;
		this.messageText = messageText;
	}
	
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getMessageText() {
		return messageText;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
}
