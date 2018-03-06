package main.imapserver.message;

import java.util.ArrayList;
import java.util.List;

public class CreateDraftMessage {
	/**
	 * Create an Array for Draft email
	 */
	List<DraftMessage> draftMes = new ArrayList<>();

	public List<DraftMessage> getDraftMes() {
		return draftMes;
	}

	public void setDraftMes(List<DraftMessage> messages) {
		this.draftMes = messages;
	}

	/**
	 * Create Draft Message
	 */

	public void createDraftMessage() {
		DraftMessage msg1 = new DraftMessage("", "10.12.2018", "aa@gmail.com", "Header 1", "hello message 1", true,
				false);
		DraftMessage msg2 = new DraftMessage("bb@gmail.com", "10.13.2018", "ab@gmail.com", "Header 2",
				"hello message 2", true, false);
		DraftMessage msg3 = new DraftMessage("cc@gmail.com", "10.14.2018", "", "Header 3", "", true, false);
		DraftMessage msg4 = new DraftMessage("dd@gmail.com", "10.15.2018", "ad@gmail.com", "Header 4",
				"hello message 4", false, false);
		DraftMessage msg5 = new DraftMessage("", "10.16.2018", "ae@gmail.com", "Header 5", "hello message 5", true,
				false);

		/**
		 * Add draft email to list
		 */
		draftMes.add(msg1);
		draftMes.add(msg2);
		draftMes.add(msg3);
		draftMes.add(msg4);
		draftMes.add(msg5);
	}
}
