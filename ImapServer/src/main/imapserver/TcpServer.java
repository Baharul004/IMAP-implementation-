package main.imapserver;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

//import javax.swing.text.Document;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import main.imapserver.message.CreateDraftMessage;
import main.imapserver.message.CreateMessage;
import main.imapserver.message.DraftMessage;
import main.imapserver.message.Message;

public class TcpServer {
	static Connect c = new Connect();
	public static List<Message> messages = new ArrayList<>();
	public static List<DraftMessage> draftMessages = new ArrayList<>();

	public static void main(String[] args) throws IOException, InterruptedException, XPathExpressionException {
		// TODO Auto-generated method stub
		ServerSocket socket = new ServerSocket(143);
		System.out.println("Server Started(IMAP4rev1)");
		Socket acceptedData = socket.accept();
		byte[] receivedRequest;
		BufferedInputStream in = new BufferedInputStream(acceptedData.getInputStream());
		DataInputStream incomingData = new DataInputStream(in);
		DataOutputStream outgoingData = new DataOutputStream(acceptedData.getOutputStream());
		String responseData = "";
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();

		CreateMessage createMessage = new CreateMessage();
		createMessage.createMessage();
		messages = createMessage.getMessages();

		CreateDraftMessage createDraftMessage = new CreateDraftMessage();
		createDraftMessage.createDraftMessage();
		draftMessages = createDraftMessage.getDraftMes();

		while (true) {
			int lengthOfData = incomingData.readInt();
			receivedRequest = new byte[lengthOfData];
			incomingData.readFully(receivedRequest);
			String obtainedData = new String(receivedRequest, "UTF-8");
			System.out.println("Obtain from Client " + obtainedData);
			InputSource source = new InputSource(new StringReader(obtainedData));
			Document doc = (Document) xpath.evaluate("/", source, XPathConstants.NODE);
			// Document docc = (Document) xpath.evaluate("/", source, XPathConstants.NODE);
			String capabilityReq = xpath.evaluate("/imap/imap.line_tree/imap.request", doc);
			String startTLSReq = xpath.evaluate("/imap/imap.line_tree/imap.request", doc);
			String userNameReq = xpath.evaluate("/imap/imap.line_tree/imap.request", doc);
			String passwordReq = xpath.evaluate("/imap/imap.line_tree/imap.request", doc);
			String inboxReq = xpath.evaluate("/imap/imap.line_tree/imap.request", doc);
			String draftReq = xpath.evaluate("/imap/imap.line_tree/imap.request", doc);
			String singleMessgaeReq = xpath.evaluate("/imap/imap.line_tree/imap.request", doc);
			String deleteMessgaeReq = xpath.evaluate("/imap/imap.line_tree/imap.requestt", doc);
			String allReq = xpath.evaluate("/imap/imap.line_tree/imap.request", doc);
			String logOutReq = xpath.evaluate("/imap/imap.line_tree/imap.request", doc);
			// System.out.println("Original value " + userNameReq);

			if (capabilityReq.equalsIgnoreCase("a001 capability")) {

				responseData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + "<imap>\r\n"
						+ "	<imap.isrequest>0</imap.isrequest>\r\n"
						+ "	<imap.line>. OK Pre-login capabilities listed, post-login capabilities have more.\r\n"
						+ "</imap.line>\r\n" + "	<imap.line_tree>\r\n"
						+ "		<imap.response_tag>.</imap.response_tag>\r\n"
						+ "		<imap.response.status>OK</imap.response.status>\r\n"
						+ "		<imap.response>OK Pre-login capabilities listed, post-login capabilities have more.</imap.response>\r\n"
						+ "	</imap.line_tree>\r\n" + "</imap>\r\n" + "";
				// responseData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<imap>\r\n" +
				// "<line>"
				// + "CAPABILITY IMAP4rev1 STARTTLS LOGINDISABLED" + "</line>\r\n" + "</imap>";

				byte[] data = responseData.getBytes("UTF-8");

				outgoingData.writeInt(data.length);
				outgoingData.write(data);

			}

			// System.out.println("Sending to Client " + responseData);

			if (startTLSReq.equalsIgnoreCase("a002 STARTTLS")) {

				responseData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + "<imap>\r\n"
						+ "	<imap.isrequest>0</imap.isrequest>\r\n" + "	<imap.line>. OK Begin TLS negotiation now.\r\n"
						+ "</imap.line>\r\n" + "	<imap.line_tree>\r\n"
						+ "		<imap.response_tag>.</imap.response_tag>\r\n"
						+ "		<imap.response.status>OK</imap.response.status>\r\n"
						+ "		<imap.response>OK Begin TLS negotiation now.</imap.response>\r\n"
						+ "	</imap.line_tree>\r\n" + "</imap>\r\n" + "";
				// responseData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<imap>\r\n" +
				// "<line>"
				// + "a002 OK Begin TLS negotiation now" + "</line>\r\n" + "</imap>";
				// responseData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<imap>\r\n" +
				// "<line>"
				// + "CAPABILITY IMAP4rev1 STARTTLS LOGINDISABLED" + "</line>\r\n" + "</imap>";

				byte[] data = responseData.getBytes("UTF-8");

				outgoingData.writeInt(data.length);
				outgoingData.write(data);

			}

			// System.out.println("Sending to Client " + responseData);
			if (userNameReq.equalsIgnoreCase(c.getUsername())) {
				responseData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + "<imap>\r\n"
						+ "	<imap.isrequest>0</imap.isrequest>\r\n" + "	<imap.line>. LogIn for UserName.\r\n"
						+ "</imap.line>\r\n" + "	<imap.line_tree>\r\n"
						+ "		<imap.response_tag>.</imap.response_tag>\r\n"
						+ "		<imap.response.status>OK</imap.response.status>\r\n"
						+ "		<imap.response>a003 OK UserName Is Done.</imap.response>\r\n"
						+ "	</imap.line_tree>\r\n" + "</imap>\r\n" + "";

				byte[] data = responseData.getBytes("UTF-8");

				outgoingData.writeInt(data.length);
				outgoingData.write(data);
			}
			// else {
			// responseData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" +
			// "<imap>\r\n"
			// + " <imap.isrequest>0</imap.isrequest>\r\n" + " <imap.line>. LogIn for
			// UserName.\r\n"
			// + "</imap.line>\r\n" + " <imap.line_tree>\r\n"
			// + " <imap.response_tag>.</imap.response_tag>\r\n"
			// + " <imap.response.status>OK</imap.response.status>\r\n"
			// + " <imap.response>OK UserName Is Not Correct.</imap.response>\r\n"
			// + " </imap.line_tree>\r\n" + "</imap>\r\n" + "";
			//
			// byte[] data = responseData.getBytes("UTF-8");
			//
			// outgoingData.writeInt(data.length);
			// outgoingData.write(data);
			// }

			if (passwordReq.equalsIgnoreCase("jav")) {
				// responseData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<imap>\r\n" +
				// "<line>"
				// + "a004 OK LOGIN completed" + "</line>\r\n" + "</imap>";

				responseData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + "<imap>\r\n"
						+ "	<imap.isrequest>0</imap.isrequest>\r\n" + "	<imap.line>. OK Begin PASSWORD .\r\n"
						+ "</imap.line>\r\n" + "	<imap.line_tree>\r\n"
						+ "		<imap.response_tag>.</imap.response_tag>\r\n"
						+ "		<imap.response.status>OK</imap.response.status>\r\n"
						+ "		<imap.response>a004 OK LOGIN completed.</imap.response>\r\n"
						+ "	</imap.line_tree>\r\n" + "</imap>\r\n" + "";

				byte[] data = responseData.getBytes("UTF-8");

				outgoingData.writeInt(data.length);
				outgoingData.write(data);

			}
			// }
			// System.out.println("Sending to Client " + responseData);
			if (inboxReq.equalsIgnoreCase("inbox")) {

				// System.out.println("Unread Message : " + getNumberOfUnreadMessage(messages));
				int unRead = getNumberOfUnreadMessage(messages);
				int total = getNumberOfMessage(messages);
				System.out.println("Total " + total);
				// op.println(m);
				// String cc = showMessages(messages);

				String xresponseData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + "<imap>\r\n"
						+ "	<imap.isrequest>0</imap.isrequest>\r\n" + "	<imap.line>. OK Begin INBOX MESSAGE.\r\n"
						+ "</imap.line>\r\n" + "	<imap.line_tree>\r\n"
						+ "		<imap.response_tag>.</imap.response_tag>\r\n"
						+ "		<imap.response.status>OK</imap.response.status>\r\n" + "		<imap.response>"
						+ "Total MESSAGE IN INBOX :" + total + "</imap.response>\r\n" + "	</imap.line_tree>\r\n"
						+ "</imap>\r\n" + "";
				// String xresponseData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
				// "<imap>\r\n" + "<line>" + total
				// + "</line>\r\n" + "</imap>";
				byte[] xdata = xresponseData.getBytes("UTF-8");

				outgoingData.writeInt(xdata.length);
				outgoingData.write(xdata);

				// responseData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<imap>\r\n" +
				// "<line>" + "<line>"
				// + unRead + "</line>\r\n" + "</line>\r\n" + "</imap>";
				responseData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + "<imap>\r\n"
						+ "	<imap.isrequest>0</imap.isrequest>\r\n" + "	<imap.line>. OK Begin TLS negotiation now.\r\n"
						+ "</imap.line>\r\n" + "	<imap.line_tree>\r\n"
						+ "		<imap.response_tag>.</imap.response_tag>\r\n"
						+ "		<imap.response.status>OK</imap.response.status>\r\n" + "		<imap.response>"
						+ "Total UNSEEN MESSAGE IN INBOX :" + unRead + "</imap.response>\r\n"
						+ "	</imap.line_tree>\r\n" + "</imap>\r\n" + "";

				byte[] data = responseData.getBytes("UTF-8");

				outgoingData.writeInt(data.length);
				outgoingData.write(data);

			}
			if (draftReq.equalsIgnoreCase("draft")) {

				// System.out.println("Unread Message : " + getNumberOfUnreadMessage(messages));

				int total = getNumberOfDraftMessage(draftMessages);
				System.out.println("Total " + total);
				// op.println(m);
				// String cc = showMessages(messages);

				String xresponseData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + "<imap>\r\n"
						+ "	<imap.isrequest>0</imap.isrequest>\r\n" + "	<imap.line>. OK Begin INBOX MESSAGE.\r\n"
						+ "</imap.line>\r\n" + "	<imap.line_tree>\r\n"
						+ "		<imap.response_tag>.</imap.response_tag>\r\n"
						+ "		<imap.response.status>OK</imap.response.status>\r\n" + "		<imap.response>"
						+ "Total MESSAGE IN DRAFT :" + total + "</imap.response>\r\n" + "	</imap.line_tree>\r\n"
						+ "</imap>\r\n" + "";
				// String xresponseData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
				// "<imap>\r\n" + "<line>" + total
				// + "</line>\r\n" + "</imap>";
				byte[] xdata = xresponseData.getBytes("UTF-8");

				outgoingData.writeInt(xdata.length);
				outgoingData.write(xdata);

			}
			if (allReq.equalsIgnoreCase("all")) {

				// System.out.println("Unread Message : " + getNumberOfUnreadMessage(messages));

				int total = getNumberOfMessage(messages) + getNumberOfDraftMessage(draftMessages);
				System.out.println("Total " + total);
				// op.println(m);
				// String cc = showMessages(messages);

				String xresponseData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + "<imap>\r\n"
						+ "	<imap.isrequest>0</imap.isrequest>\r\n" + "	<imap.line>. OK Begin INBOX MESSAGE.\r\n"
						+ "</imap.line>\r\n" + "	<imap.line_tree>\r\n"
						+ "		<imap.response_tag>.</imap.response_tag>\r\n"
						+ "		<imap.response.status>OK</imap.response.status>\r\n" + "		<imap.response>"
						+ "Total MESSAGE  :" + total + "</imap.response>\r\n" + "	</imap.line_tree>\r\n"
						+ "</imap>\r\n" + "";
				// String xresponseData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
				// "<imap>\r\n" + "<line>" + total
				// + "</line>\r\n" + "</imap>";
				byte[] xdata = xresponseData.getBytes("UTF-8");

				outgoingData.writeInt(xdata.length);
				outgoingData.write(xdata);

			}

			if (singleMessgaeReq.equals("1") || singleMessgaeReq.equals("2") || singleMessgaeReq.equals("3")
					|| singleMessgaeReq.equals("4")) {

				System.out.println("Unread Message : " + getNumberOfUnreadMessage(messages));
				showMessages(messages);

				int msgNumber = Integer.valueOf(singleMessgaeReq);
				System.out.println();
				if (msgNumber < messages.size()) {
					System.out.println("Request to see message no: " + singleMessgaeReq);
					messages.get(msgNumber).setSeen(true);
					showSpecificMessage(messages.get(msgNumber));
					// responseData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<imap>\r\n" +
					// "<line>" + "<line>"
					// + showSpecificMessage(messages.get(msgNumber)) + "</line>\r\n" +
					// "</line>\r\n" + "</imap>";
					responseData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + "<imap>\r\n"
							+ "	<imap.isrequest>0</imap.isrequest>\r\n" + "	<imap.line>. OK Begin Single MESSGAE.\r\n"
							+ "</imap.line>\r\n" + "	<imap.line_tree>\r\n"
							+ "		<imap.response_tag>.</imap.response_tag>\r\n"
							+ "		<imap.response.status>OK</imap.response.status>\r\n" + "		<imap.response>"
							+ "Selected Message " + msgNumber + " Number is : "
							+ showSpecificMessage(messages.get(msgNumber)) + "</imap.response>\r\n"
							+ "	</imap.line_tree>\r\n" + "</imap>\r\n" + "";

					byte[] data = responseData.getBytes("UTF-8");

					outgoingData.writeInt(data.length);
					outgoingData.write(data);
					String xresponseData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + "<imap>\r\n"
							+ "	<imap.isrequest>0</imap.isrequest>\r\n" + "	<imap.line>. OK Begin INBOX MESSAGE.\r\n"
							+ "</imap.line>\r\n" + "	<imap.line_tree>\r\n"
							+ "		<imap.response_tag>.</imap.response_tag>\r\n"
							+ "		<imap.response.status>OK</imap.response.status>\r\n" + "		<imap.response>"
							+ "Total UNREAD MESSAGE IN INBOX :" + getNumberOfUnreadMessage(messages)
							+ "</imap.response>\r\n" + "	</imap.line_tree>\r\n" + "</imap>\r\n" + "";

					byte[] xdata = xresponseData.getBytes("UTF-8");

					outgoingData.writeInt(xdata.length);
					outgoingData.write(xdata);

					System.out.println("Unread Messagess : " + getNumberOfUnreadMessage(messages));
				} else {
					System.out.println("No message found for message no : " + msgNumber);
				}

			} else {
				System.out.println("No message found for message no : " + singleMessgaeReq);
			}
			if (deleteMessgaeReq.equals("1") || deleteMessgaeReq.equals("2") || deleteMessgaeReq.equals("3")
					|| deleteMessgaeReq.equals("4") || deleteMessgaeReq.equals("5")) {
				System.out.println("Total Messages : " + getNumberOfMessage(messages));
				int msgNumber = Integer.valueOf(deleteMessgaeReq);

				if (msgNumber <= messages.size()) {
					System.out.println("Request to delete message no: " + deleteMessgaeReq);
					messages.get(msgNumber).setDelete(true);
					// System.out.println("Total Message after deleted: " +
					// getNumberOfMessage(messages));

					// responseData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<imap>\r\n" +
					// "<line>" + "<line>"
					// + getNumberOfMessage(messages) + "</line>\r\n" + "</line>\r\n" + "</imap>";
					responseData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + "<imap>\r\n"
							+ "	<imap.isrequest>0</imap.isrequest>\r\n" + "	<imap.line>. DELETE.\r\n"
							+ "</imap.line>\r\n" + "	<imap.line_tree>\r\n"
							+ "		<imap.response_tag>.</imap.response_tag>\r\n"
							+ "		<imap.response.status>OK</imap.response.status>\r\n" + "		<imap.respons>"
							+ deleteMessgaeReq + " Number EMAIL has been DELETED" + "</imap.respons>\r\n"
							+ "	</imap.line_tree>\r\n" + "</imap>\r\n" + "";

					byte[] data = responseData.getBytes("UTF-8");

					outgoingData.writeInt(data.length);
					outgoingData.write(data);
					String xresponseData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + "<imap>\r\n"
							+ "	<imap.isrequest>0</imap.isrequest>\r\n" + "	<imap.line>. OK Begin INBOX MESSAGE.\r\n"
							+ "</imap.line>\r\n" + "	<imap.line_tree>\r\n"
							+ "		<imap.response_tag>.</imap.response_tag>\r\n"
							+ "		<imap.response.status>OK</imap.response.status>\r\n" + "		<imap.respons>"
							+ "Total UNREAD MESSAGE IN INBOX after DELETED:" + getNumberOfMessage(messages)
							+ "</imap.respons>\r\n" + "	</imap.line_tree>\r\n" + "</imap>\r\n" + "";

					byte[] xdata = xresponseData.getBytes("UTF-8");

					outgoingData.writeInt(xdata.length);
					outgoingData.write(xdata);

					System.out.println("Total Message after deleted: " + getNumberOfMessage(messages));

				} else {
					System.out.println("No EMAIL has been found for delete : " + msgNumber);
				}

			}
			if (draftReq.equalsIgnoreCase("logout")) {

				// System.out.println("Unread Message : " + getNumberOfUnreadMessage(messages));

				// op.println(m);
				// String cc = showMessages(messages);

				String xresponseData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + "<imap>\r\n"
						+ "	<imap.isrequest>0</imap.isrequest>\r\n" + "	<imap.line>. OK Begin INBOX MESSAGE.\r\n"
						+ "</imap.line>\r\n" + "	<imap.line_tree>\r\n"
						+ "		<imap.response_tag>.</imap.response_tag>\r\n"
						+ "		<imap.response.status>OK</imap.response.status>\r\n"
						+ "		<imap.response>Log Out</imap.response>\r\n" + "	</imap.line_tree>\r\n" + "</imap>\r\n"
						+ "";
				// String xresponseData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
				// "<imap>\r\n" + "<line>" + total
				// + "</line>\r\n" + "</imap>";
				byte[] xdata = xresponseData.getBytes("UTF-8");

				outgoingData.writeInt(xdata.length);
				outgoingData.write(xdata);

			}
			System.out.println("Sending to Client " + responseData);

		}

	}

	public static int getNumberOfMessage(List<Message> msg) {
		int totalMsg = 0;
		for (Message message : msg) {
			if (message.isDelete() == false) {
				totalMsg++;
			}
		}
		return totalMsg;
	}

	//
	public static int getNumberOfUnreadMessage(List<Message> msg) {
		int unreadMsg = 0;
		for (Message message : msg) {
			if (message.isSeen() == false) {
				unreadMsg++;
			}
		}
		return unreadMsg;
	}

	//
	public static void showMessages(List<Message> messages) {
		for (Message message : messages) {
			if (message.isSeen() == false) {
				System.out.println(message);
			}
		}

	}

	public static String showSpecificMessage(Message messages) {
		System.out.println("TO : " + messages.getTo());
		System.out.println("Date : " + messages.getDate());
		System.out.println("From : " + messages.getFrom());
		System.out.println("Subject : " + messages.getSubject());
		System.out.println("Body : " + messages.getMsg());
		return "TO : " + messages.getTo() + " Date : " + messages.getDate() + " From : " + messages.getFrom()
				+ " Subject : " + messages.getSubject() + " Body : " + messages.getMsg();

	}

	public static int getNumberOfDraftMessage(List<DraftMessage> msg) {
		int totalDraftMsg = 0;
		for (DraftMessage message : msg) {

			totalDraftMsg++;

		}
		return totalDraftMsg;
	}

}
