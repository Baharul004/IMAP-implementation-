package main.imapserver.message;

import java.util.ArrayList;
import java.util.List;

public class CreateDraftMessage {
	List<DraftMessage> draftMes = new ArrayList<>();

	public List<DraftMessage> getDraftMes() {
		return draftMes;
	}

	public void setDraftMes(List<DraftMessage> messages) {
		this.draftMes = messages;
	}

	public void createDraftMessage() {
		DraftMessage msg1 = new DraftMessage("aa@gmail.com", "10.12.2018", "aa@gmail.com", "Header 1",
				"hello message 1", true, false);
		DraftMessage msg2 = new DraftMessage("bb@gmail.com", "10.13.2018", "ab@gmail.com", "Header 2",
				"hello message 2", false, false);
		DraftMessage msg3 = new DraftMessage("cc@gmail.com", "10.14.2018", "ac@gmail.com", "Header 3",
				"hello message 3", false, false);
		DraftMessage msg4 = new DraftMessage("dd@gmail.com", "10.15.2018", "ad@gmail.com", "Header 4",
				"hello message 4", false, false);
		DraftMessage msg5 = new DraftMessage("ee@gmail.com", "10.16.2018", "ae@gmail.com", "Header 5",
				"hello message 5", false, false);
		draftMes.add(msg1);
		draftMes.add(msg2);
		draftMes.add(msg3);
		draftMes.add(msg4);
		draftMes.add(msg5);
	}
}
