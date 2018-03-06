package main.imapserver;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

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

		/**
		 * server socket set up
		 */
		ServerSocket socket = new ServerSocket(1143);
		System.out.println("Server Started(IMAP4rev1)");
		/**
		 * server Accept socket
		 */
		Socket acceptedData = socket.accept();
		byte[] receivedRequest;
		/**
		 * Data Transfer and receiver
		 */
		BufferedInputStream in = new BufferedInputStream(acceptedData.getInputStream());
		DataInputStream incomingData = new DataInputStream(in);
		DataOutputStream outgoingData = new DataOutputStream(acceptedData.getOutputStream());
		String responseData = "";
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		/**
		 * Create Inbox message
		 */
		CreateMessage createMessage = new CreateMessage();
		createMessage.createMessage();
		messages = createMessage.getMessages();
		/**
		 * Create Draft message
		 */
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
			String logOutReq = xpath.evaluate("/imap/imap.line_tree/imap.requestl", doc);
			String folderReq = xpath.evaluate("/imap/imap.request", doc);
			String messageTransfer = xpath.evaluate("/imap/imap.line_tree/imap.requestm", doc);
			String folderDelete = xpath.evaluate("/imap/imap.line_tree/imap.requestf", doc);
			// System.out.println(folderReq);
			System.out.println(logOutReq);
			/**
			 * Capability response
			 */
			if (capabilityReq.equalsIgnoreCase("a001 capability")) {

				responseData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + "<imap>\r\n"
						+ "	<imap.isrequest>0</imap.isrequest>\r\n"
						+ "	<imap.line>. OK Pre-login capabilities listed, post-login capabilities have more.\r\n"
						+ "</imap.line>\r\n" + "	<imap.line_tree>\r\n"
						+ "		<imap.response_tag>.</imap.response_tag>\r\n"
						+ "		<imap.response.status>OK</imap.response.status>\r\n"
						+ "		<imap.response>OK Pre-login capabilities listed, post-login capabilities have more.</imap.response>\r\n"
						+ "	</imap.line_tree>\r\n" + "</imap>\r\n" + "";
				byte[] data = responseData.getBytes("UTF-8");
				outgoingData.writeInt(data.length);
				outgoingData.write(data);
			}
			/**
			 * Start TLS response
			 */
			if (startTLSReq.equalsIgnoreCase("a002 STARTTLS")) {
				responseData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + "<imap>\r\n"
						+ "	<imap.isrequest>0</imap.isrequest>\r\n" + "	<imap.line>. OK Begin TLS negotiation now.\r\n"
						+ "</imap.line>\r\n" + "	<imap.line_tree>\r\n"
						+ "		<imap.response_tag>.</imap.response_tag>\r\n"
						+ "		<imap.response.status>OK</imap.response.status>\r\n"
						+ "		<imap.response>OK Begin TLS negotiation now.</imap.response>\r\n"
						+ "	</imap.line_tree>\r\n" + "</imap>\r\n" + "";

				byte[] data = responseData.getBytes("UTF-8");
				outgoingData.writeInt(data.length);
				outgoingData.write(data);

			}

			// System.out.println("Sending to Client " + responseData);
			/**
			 * Username Check and Response
			 */
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

			/**
			 * Password check and response
			 */
			if (passwordReq.equals("jav")) {
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
			/**
			 * INBOX message
			 */
			if (inboxReq.equalsIgnoreCase("inbox")) {
				int unRead = getNumberOfUnreadMessage(messages);
				int total = getNumberOfMessage(messages);
				// System.out.println("Total " + total);
				/**
				 * show all message from inbox
				 */

				showMessages(messages);

				String xresponseData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + "<imap>\r\n"
						+ "	<imap.isrequest>0</imap.isrequest>\r\n" + "	<imap.line>. OK Begin INBOX MESSAGE.\r\n"
						+ "</imap.line>\r\n" + "	<imap.line_tree>\r\n"
						+ "		<imap.response_tag>.</imap.response_tag>\r\n"
						+ "		<imap.response.status>OK</imap.response.status>\r\n" + "		<imap.response>"
						+ "Total MESSAGE IN INBOX :" + total + "</imap.response>\r\n" + "	</imap.line_tree>\r\n"
						+ "</imap>\r\n" + "";
				/**
				 * Send response to Client
				 */
				byte[] xdata = xresponseData.getBytes("UTF-8");

				outgoingData.writeInt(xdata.length);
				outgoingData.write(xdata);

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
			/**
			 * DRAFT Message
			 */
			if (draftReq.equalsIgnoreCase("draft")) {

				// System.out.println("Unread Message : " + getNumberOfUnreadMessage(messages));
				int total = getNumberOfDraftMessage(draftMessages);
				System.out.println("Total " + total);
				showDraftMessages(draftMessages);

				String xresponseData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + "<imap>\r\n"
						+ "	<imap.isrequest>0</imap.isrequest>\r\n" + "	<imap.line>. OK Begin INBOX MESSAGE.\r\n"
						+ "</imap.line>\r\n" + "	<imap.line_tree>\r\n"
						+ "		<imap.response_tag>.</imap.response_tag>\r\n"
						+ "		<imap.response.status>OK</imap.response.status>\r\n" + "		<imap.response>"
						+ "Total MESSAGE IN DRAFT :" + total + "</imap.response>\r\n" + "	</imap.line_tree>\r\n"
						+ "</imap>\r\n" + "";

				byte[] xdata = xresponseData.getBytes("UTF-8");
				outgoingData.writeInt(xdata.length);
				outgoingData.write(xdata);

			}
			/**
			 * All Message
			 */
			if (allReq.equalsIgnoreCase("all")) {
				int total = getNumberOfMessage(messages) + getNumberOfDraftMessage(draftMessages);
				System.out.println("Total " + total);

				String xresponseData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + "<imap>\r\n"
						+ "	<imap.isrequest>0</imap.isrequest>\r\n" + "	<imap.line>. OK Begin INBOX MESSAGE.\r\n"
						+ "</imap.line>\r\n" + "	<imap.line_tree>\r\n"
						+ "		<imap.response_tag>.</imap.response_tag>\r\n"
						+ "		<imap.response.status>OK</imap.response.status>\r\n" + "		<imap.response>"
						+ "Total MESSAGE  :" + total + "</imap.response>\r\n" + "	</imap.line_tree>\r\n"
						+ "</imap>\r\n" + "";

				byte[] xdata = xresponseData.getBytes("UTF-8");

				outgoingData.writeInt(xdata.length);
				outgoingData.write(xdata);

			}
			/**
			 * Specific message response
			 */
			if (singleMessgaeReq.equals("0") || singleMessgaeReq.equals("1") || singleMessgaeReq.equals("2")
					|| singleMessgaeReq.equals("3") || singleMessgaeReq.equals("4")) {
				System.out.println("Unread Message : " + getNumberOfUnreadMessage(messages));
				showMessages(messages);
				int msgNumber = Integer.valueOf(singleMessgaeReq);
				System.out.println();
				if (msgNumber < messages.size()) {
					System.out.println("Request to see message no: " + singleMessgaeReq);
					messages.get(msgNumber).setSeen(true);
					// System.out.println(messages.get(msgNumber).getSubject());
					showSpecificMessage(messages.get(msgNumber));
					/**
					 * Send to client for Specific message
					 */
					responseData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + "<imap>\r\n"
							+ "	<imap.isrequest>0</imap.isrequest>\r\n" + "	<imap.line>. OK Begin Single MESSGAE.\r\n"
							+ "</imap.line>\r\n" + "	<imap.line_tree>\r\n"
							+ "		<imap.response_tag>.</imap.response_tag>\r\n"
							+ "		<imap.response.status>OK</imap.response.status>\r\n" + "		<imap.response.to>"
							+ "Selected Message Number is " + msgNumber + "  To : " + messages.get(msgNumber).getTo()
							+ "</imap.response.to>\r\n" + "		<imap.response.date>" + "Date" + "  : "
							+ messages.get(msgNumber).getDate() + "</imap.response.date>\r\n"
							+ "	<imap.response.from>" + "From" + "  : " + messages.get(msgNumber).getFrom()
							+ "</imap.response.from>\r\n" + "	<imap.response.subject>" + "Subject" + "  : "
							+ messages.get(msgNumber).getSubject() + "</imap.response.subject>\r\n"
							+ "	<imap.response.body>" + "Body" + "  : " + messages.get(msgNumber).getMsg()
							+ "</imap.response.body>\r\n" + "	</imap.line_tree>\r\n" + "</imap>\r\n" + "";

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
				}

			}

			/**
			 * Folder Create response
			 */

			if (folderReq.length() != 0) {
				System.out.println("yes");
				File file = new File(folderReq);
				String filename = file + "/" + file + ".txt";
				File file2 = new File(filename);
				// String fileName = "lalu.txt";
				// File f = new File("lalu.txt");

				FileWriter writefile = new FileWriter("lalu.txt", true);
				BufferedWriter bwr = new BufferedWriter(writefile);
				bwr.write(folderReq);
				bwr.close();

				System.out.println(file);
				if (!file.exists()) {
					if (file.mkdir()) {
						System.out.println("Folder created");
						if (file.exists()) {
							System.out.println("Folder Exits");
							if (file2.createNewFile()) {
								System.out.println("Txt File created");
							}
						}

						System.out.println("Directory is created!");
					} else {
						System.out.println("Failed to create directory!");
					}
				}

				responseData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + "<imap>\r\n"
						+ "	<imap.isrequest>0</imap.isrequest>\r\n" + "	<imap.line>. TRANSFER.\r\n" + "</imap.line>\r\n"
						+ "	<imap.line_tree>\r\n" + "		<imap.response_tag>.</imap.response_tag>\r\n"
						+ "		<imap.response.status>OK</imap.response.status>\r\n" + "		<imap.responsfolder>"
						+ folderReq + "  has been Created" + "</imap.responsfolder>\r\n" + "	</imap.line_tree>\r\n"
						+ "</imap>\r\n" + "";

				System.out.println("Transferred response" + responseData);

				byte[] data = responseData.getBytes("UTF-8");

				outgoingData.writeInt(data.length);
				outgoingData.write(data);
			}
			/**
			 * Transfer Message response
			 */

			if (messageTransfer.equals("0") || messageTransfer.equals("1") || messageTransfer.equals("2")
					|| messageTransfer.equals("3") || messageTransfer.equals("4")) {

				int msgNumber = Integer.valueOf(messageTransfer);
				// System.out.println("Name of the folder " + folderReq);
				System.out.println("Message Number " + msgNumber);
				if (msgNumber < messages.size()) {
					System.out.println("Request to send message no: " + messageTransfer);
					String msg = messages.get(msgNumber).getMsg();
					String filename = "";
					String filefromfile = "";
					try (Scanner scanner = new Scanner(new File("lalu.txt"))) {
						while (scanner.hasNext()) {
							filefromfile = scanner.nextLine();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}

					System.out.println("Foldername:" + filefromfile);

					if (filefromfile.length() != 0) {
						filename = filefromfile + "\\" + filefromfile + ".txt"; // file + "/" + file + ".txt"
					} else {
						System.out.println("The File is not created");
					}
					// System.out.println(messages.get(msgNumber));
					FileWriter write = new FileWriter(filename, true);
					BufferedWriter bw = new BufferedWriter(write);

					// mes sent
					bw.write(msg);

					bw.close();

					PrintWriter writer = new PrintWriter("lalu.txt");
					writer.print("");
					writer.close();
					responseData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + "<imap>\r\n"
							+ "	<imap.isrequest>0</imap.isrequest>\r\n" + "	<imap.line>. TRANSFER.\r\n"
							+ "</imap.line>\r\n" + "	<imap.line_tree>\r\n"
							+ "		<imap.response_tag>.</imap.response_tag>\r\n"
							+ "		<imap.response.status>OK</imap.response.status>\r\n" + "		<imap.responsedf>"
							+ messages.get(msgNumber).getMsg() + " Number EMAIL has been Transfered"
							+ "</imap.responsedf>\r\n" + "	</imap.line_tree>\r\n" + "</imap>\r\n" + "";

					System.out.println("Transferred response" + responseData);

					byte[] data = responseData.getBytes("UTF-8");

					outgoingData.writeInt(data.length);
					outgoingData.write(data);

				}
			}
			/**
			 * Delete Message response
			 */
			if (deleteMessgaeReq.equals("0") || deleteMessgaeReq.equals("1") || deleteMessgaeReq.equals("2")
					|| deleteMessgaeReq.equals("3") || deleteMessgaeReq.equals("4")) {
				System.out.println("Total Messages : " + getNumberOfMessage(messages));
				int msgNumber = Integer.valueOf(deleteMessgaeReq);

				if (msgNumber <= messages.size()) {
					System.out.println("Request to delete message no: " + deleteMessgaeReq);
					messages.get(msgNumber).setDelete(true);
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
							+ "Total MESSAGE IN INBOX after DELETED:" + getNumberOfMessage(messages)
							+ "</imap.respons>\r\n" + "	</imap.line_tree>\r\n" + "</imap>\r\n" + "";

					byte[] xdata = xresponseData.getBytes("UTF-8");

					outgoingData.writeInt(xdata.length);
					outgoingData.write(xdata);

					System.out.println("Total Message after deleted: " + getNumberOfMessage(messages));

				} else {
					System.out.println("No EMAIL has been found for delete : " + msgNumber);
				}

			}
			/**
			 * Folder Delete response
			 */
			if (folderDelete.length() != 0) {

				Path rootPath = Paths.get(folderDelete);
				Files.walk(rootPath, FileVisitOption.FOLLOW_LINKS).sorted(Comparator.reverseOrder()).map(Path::toFile)
						.forEach(File::delete);

				responseData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + "<imap>\r\n"
						+ "	<imap.isrequest>0</imap.isrequest>\r\n" + "	<imap.line>. LogIn for UserName.\r\n"
						+ "</imap.line>\r\n" + "	<imap.line_tree>\r\n"
						+ "		<imap.response_tag>.</imap.response_tag>\r\n"
						+ "		<imap.response.status>OK</imap.response.status>\r\n" + "		<imap.responsedelete>"
						+ folderDelete + " folder has been Deleted." + "</imap.responsedelete>\r\n"
						+ "	</imap.line_tree>\r\n" + "</imap>\r\n" + "";

				System.out.println("DEl :" + responseData);

				byte[] xdata = responseData.getBytes("UTF-8");

				outgoingData.writeInt(xdata.length);
				outgoingData.write(xdata);

			}
			/**
			 * Logout Response
			 */
			if (logOutReq.equalsIgnoreCase("logout")) {
				String xresponseData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + "<imap>\r\n"
						+ "	<imap.isrequest>0</imap.isrequest>\r\n" + "	<imap.line>. Log Out.\r\n" + "</imap.line>\r\n"
						+ "	<imap.line_tree>\r\n" + "		<imap.response_tag>.</imap.response_tag>\r\n"
						+ "		<imap.response.status>OK</imap.response.status>\r\n"
						+ "		<imap.responsel>Log Out</imap.responsel>\r\n" + "	</imap.line_tree>\r\n"
						+ "</imap>\r\n" + "";

				byte[] xdata = xresponseData.getBytes("UTF-8");
				outgoingData.writeInt(xdata.length);
				outgoingData.write(xdata);
			}
			System.out.println("Sending to Client " + responseData);

		}

	}

	/**
	 * Total number of message
	 */
	public static int getNumberOfMessage(List<Message> msg) {
		int totalMsg = 0;
		for (Message message : msg) {
			if (message.isDelete() == false) {
				totalMsg++;
			}
		}
		return totalMsg;
	}

	/**
	 * number of unread message
	 */
	public static int getNumberOfUnreadMessage(List<Message> msg) {
		int unreadMsg = 0;
		for (Message message : msg) {
			if (message.isSeen() == false) {
				unreadMsg++;
			}
		}
		return unreadMsg;
	}

	/**
	 * message Show
	 */
	public static void showMessages(List<Message> messages) {
		for (Message message : messages) {
			if (message.isSeen() == false || message.isSeen() == true) {
				System.out.println(message);
			}
		}

	}

	/**
	 * Show specific message
	 */
	public static String showSpecificMessage(Message messages) {
		System.out.println("TO : " + messages.getTo());
		System.out.println("Date : " + messages.getDate());
		System.out.println("From : " + messages.getFrom());
		System.out.println("Subject : " + messages.getSubject());
		System.out.println("Body : " + messages.getMsg());
		return "TO : " + messages.getTo() + " Date : " + messages.getDate() + " From : " + messages.getFrom()
				+ " Subject : " + messages.getSubject() + " Body : " + messages.getMsg();

	}

	/**
	 * draftmessage Show
	 */
	public static void showDraftMessages(List<DraftMessage> messages) {
		for (DraftMessage message : messages) {
			if (message.isSeen() == true) {
				System.out.println(message);
			}
		}

	}

	/**
	 * Total number of Draft message
	 */

	public static int getNumberOfDraftMessage(List<DraftMessage> msg) {
		int totalDraftMsg = 0;
		for (DraftMessage message : msg) {

			totalDraftMsg++;

		}
		return totalDraftMsg;
	}

}
