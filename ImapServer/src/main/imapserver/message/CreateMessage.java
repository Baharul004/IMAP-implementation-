package main.imapserver.message;

import java.util.ArrayList;
import java.util.List;

public class CreateMessage {
	/**
	 * ArrayList for EMAIL
	 */
	List<Message> messages = new ArrayList<>();

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	/**
	 * Client Static message in Server for Client
	 */
	public void createMessage() {
		Message msg1 = new Message("aa@gmail.com", "10.12.2018", "aa@gmail.com", "Header 1", "hello message 1", true,
				false);
		Message msg2 = new Message("bb@gmail.com", "10.13.2018", "ab@gmail.com", "Header 2", "hello message 2", false,
				false);
		Message msg3 = new Message("cc@gmail.com", "10.14.2018", "ac@gmail.com", "Header 3", "hello message 3", false,
				false);
		Message msg4 = new Message("dd@gmail.com", "10.15.2018", "ad@gmail.com", "Header 4", "hello message 4", false,
				false);
		Message msg5 = new Message("ee@gmail.com", "10.16.2018", "ae@gmail.com", "Header 5", "hello message 5", false,
				false);
		/**
		 * Add Email in List
		 */
		messages.add(msg1);
		messages.add(msg2);
		messages.add(msg3);
		messages.add(msg4);
		messages.add(msg5);
	}

}
