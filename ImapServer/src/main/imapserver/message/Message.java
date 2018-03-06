package main.imapserver.message;

public class Message {
	/**
	 * Email details with getter and Setter
	 */
	String subject;
	String msg;
	String To;
	String From;
	String date;

	boolean seen;

	public String getTo() {
		return To;
	}

	public void setTo(String to) {
		To = to;
	}

	public String getFrom() {
		return From;
	}

	public void setFrom(String from) {
		From = from;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	boolean delete;

	/**
	 * Email details for getting value
	 */
	public Message(String to, String date, String from, String subject, String msg, boolean seen, boolean delete) {
		super();
		this.To = to;
		this.date = date;
		this.From = from;
		this.subject = subject;
		this.msg = msg;
		this.seen = seen;
		this.delete = delete;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isSeen() {
		return seen;
	}

	public void setSeen(boolean seen) {
		this.seen = seen;
	}

	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}

	@Override
	public String toString() {
		return "Message [to=" + To + ", date=" + date + ", from=" + From + ", subject=" + subject + ", msg=" + msg
				+ ", seen=" + seen + "]";
	}
}
