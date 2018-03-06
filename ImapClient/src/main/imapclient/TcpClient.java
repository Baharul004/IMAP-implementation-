package main.imapclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class TcpClient {

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException, XPathExpressionException {
		// TODO Auto-generated method stub
		/**
		 * CREATE A SOCKET FOR CLIENT
		 */

		Socket socket = new Socket("127.0.0.1", 1143);
		String connect = "Connected to server";

		String requestType = "";
		String requestFolder = "";
		/**
		 * Scanner for taking value
		 */

		Scanner scanner = new Scanner(System.in);
		/**
		 * Data send and Receive
		 */
		DataOutputStream clientData = new DataOutputStream(socket.getOutputStream());
		DataInputStream serverData = new DataInputStream(socket.getInputStream());
		byte[] receivedResponse;
		System.out.println(connect);
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		if (connect.equalsIgnoreCase("Connected to server")) {
			// BufferedReader responseChannel = new BufferedReader(new
			// InputStreamReader(socket.getInputStream()));
			// PrintStream requestChannel = new PrintStream(socket.getOutputStream(), true);
			while (true) {
				System.out.println("1-Request for CAPABILITY");
				System.out.println("2-Request for STARTTLS");
				System.out.println("3-Request for USERNAME");
				// System.out.println("4-Request for PASSWORD");
				requestType = scanner.next();

				if (requestType.equalsIgnoreCase("1") || requestType.equalsIgnoreCase("capability")) {

					/**
					 * capability Request
					 */

					String dataToSend = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n"
							+ "					<imap>\r\n"
							+ "						<imap.isrequest>1</imap.isrequest>\r\n"
							+ "						<imap.line>. CAPABILITY\r\n"
							+ "					</imap.line>\r\n" + "						<imap.line_tree>\r\n"
							+ "							<imap.request_tag>.</imap.request_tag>\r\n"
							+ "							<imap.request.command>CAPABILITY</imap.request.command>\r\n"
							+ "							<imap.request>a001 capability</imap.request>\r\n"
							+ "						</imap.line_tree>\r\n" + "					</imap>";

					byte[] data = dataToSend.getBytes("UTF-8");

					clientData.writeInt(data.length);
					clientData.write(data);

					System.out.println("+++++ Response from Server for capability +++++");
					/**
					 * capability Response
					 */
					int lengthOfData = serverData.readInt();
					// System.out.println(lengthOfData);
					receivedResponse = new byte[lengthOfData];
					serverData.readFully(receivedResponse);
					String responseDataFromServer = new String(receivedResponse, "UTF-8");

					System.out.println(responseDataFromServer);

				} else if (requestType.equalsIgnoreCase("2") || requestType.equalsIgnoreCase("STARTTLS")) {
					/**
					 * Start TLS Request
					 */

					String dataToSend = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + "<imap>\r\n"
							+ "	<imap.isrequest>1</imap.isrequest>\r\n" + "	<imap.line>. STARTTLS\r\n"
							+ "</imap.line>\r\n" + "	<imap.line_tree>\r\n"
							+ "		<imap.request_tag>.</imap.request_tag>\r\n"
							+ "		<imap.request.command>STARTTLS</imap.request.command>\r\n"
							+ "		<imap.request>a002 STARTTLS</imap.request>\r\n" + "	</imap.line_tree>\r\n"
							+ "</imap>\r\n" + "";
					byte[] data = dataToSend.getBytes("UTF-8");

					clientData.writeInt(data.length);
					clientData.write(data);

					System.out.println("+++++ Response from Server for START TLS +++++");
					/**
					 * Start TLS Response
					 */
					int lengthOfData = serverData.readInt();
					// System.out.println(lengthOfData);
					receivedResponse = new byte[lengthOfData];
					serverData.readFully(receivedResponse);
					String responseDataFromServer = new String(receivedResponse, "UTF-8");

					System.out.println(responseDataFromServer);

				} else if (requestType.equalsIgnoreCase("3") || requestType.equalsIgnoreCase("java")) {

					/**
					 * Request for Username
					 */
					String dataToSend = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n"
							+ "					<imap>\r\n"
							+ "						<imap.isrequest>1</imap.isrequest>\r\n"
							+ "						<imap.line>. USERNAME\r\n" + "					</imap.line>\r\n"
							+ "						<imap.line_tree>\r\n"
							+ "							<imap.request_tag>.</imap.request_tag>\r\n"
							+ "							<imap.request.command>USERNAME</imap.request.command>\r\n"
							+ "							<imap.request>JAVA</imap.request>\r\n"
							+ "						</imap.line_tree>\r\n" + "					</imap>";
					byte[] data = dataToSend.getBytes("UTF-8");
					clientData.writeInt(data.length);
					clientData.write(data);
					System.out.println("UserName response from Server");
					/**
					 * Response for Username
					 */
					int lengthOfData = serverData.readInt();
					// System.out.println(lengthOfData);
					receivedResponse = new byte[lengthOfData];
					serverData.readFully(receivedResponse);
					String responseDataFromServer = new String(receivedResponse, "UTF-8");

					// System.out.println(responseDataFromServer);
					InputSource source = new InputSource(new StringReader(responseDataFromServer));
					Document doc = (Document) xpath.evaluate("/", source, XPathConstants.NODE);
					String value = xpath.evaluate("/imap/imap.line_tree/imap.response", doc);
					System.out.println(value);

					/**
					 * Request for Password
					 */
					System.out.println("4-Request for PASSWORD");
					String requiredInfo = scanner.next();
					if (requiredInfo.equalsIgnoreCase("4") || requiredInfo.equals("jav")) {
						// System.out.println("yes");

						dataToSend = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + "<imap>\r\n"
								+ "	<imap.isrequest>1</imap.isrequest>\r\n" + "	<imap.line>. PASSWORD\r\n"
								+ "</imap.line>\r\n" + "	<imap.line_tree>\r\n"
								+ "		<imap.request_tag>.</imap.request_tag>\r\n"
								+ "		<imap.request.command>PASSWORD</imap.request.command>\r\n"
								+ "		<imap.request>jav</imap.request>\r\n" + "	</imap.line_tree>\r\n"
								+ "</imap>\r\n" + "";
						data = dataToSend.getBytes("UTF-8");
						clientData.writeInt(data.length);
						clientData.write(data);

						/**
						 * Response for Password
						 */

						System.out.println("Password response from Server");
						lengthOfData = serverData.readInt();
						// System.out.println(lengthOfData);
						receivedResponse = new byte[lengthOfData];
						serverData.readFully(receivedResponse);
						responseDataFromServer = new String(receivedResponse, "UTF-8");

						// System.out.println(responseDataFromServer);
						source = new InputSource(new StringReader(responseDataFromServer));
						doc = (Document) xpath.evaluate("/", source, XPathConstants.NODE);
						value = xpath.evaluate("/imap/imap.line_tree/imap.response", doc);
						System.out.println(value);
						/**
						 * After Authenticated USERName and Password
						 */

						if (value.contains("a004 OK LOGIN completed")) {
							while (true) {
								System.out.println("1: Select INBOX");
								System.out.println("2: Select DRAFTS");
								System.out.println("3: Select SINGLE MESSAGE FROM INBOX");
								System.out.println("4: Select DELETE MESSAGE FROM INBOX");
								System.out.println("5: Select ALL MESSAGES");
								System.out.println("6: MAKE A FOLDER");
								System.out.println("7: Transfer a message to A FOLDER");
								System.out.println("8: DELETE A FOLDER");
								System.out.println("9: Select LOG OUT");
								String requiredBox = scanner.next();
								if (requiredBox.equals("6") || requiredBox.equalsIgnoreCase("inboxMes")) {

								}
								/**
								 * Request for Inbox
								 */
								if (requiredBox.equals("1") || requiredBox.equalsIgnoreCase("inbox")) {
									// dataToSend = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<imap>\r\n" +
									// "<line>"
									// + "inbox" + "</line>\r\n" + "</imap>\r\n";
									dataToSend = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + "<imap>\r\n"
											+ "	<imap.isrequest>1</imap.isrequest>\r\n" + "	<imap.line>. INBOX\r\n"
											+ "</imap.line>\r\n" + "	<imap.line_tree>\r\n"
											+ "		<imap.request_tag>.</imap.request_tag>\r\n"
											+ "		<imap.request.command>INBOX</imap.request.command>\r\n"
											+ "		<imap.request>INBOX</imap.request>\r\n" + "	</imap.line_tree>\r\n"
											+ "</imap>\r\n" + "";

									data = dataToSend.getBytes("UTF-8");
									clientData.writeInt(data.length);
									clientData.write(data);

									System.out.println("Response from Server FOR INBOX Total MESSAGE");

									lengthOfData = serverData.readInt();

									receivedResponse = new byte[lengthOfData];
									serverData.readFully(receivedResponse);
									responseDataFromServer = new String(receivedResponse, "UTF-8");

									// System.out.println(responseDataFromServer);
									source = new InputSource(new StringReader(responseDataFromServer));
									doc = (Document) xpath.evaluate("/", source, XPathConstants.NODE);

									value = xpath.evaluate("/imap/imap.line_tree/imap.response", doc);
									System.out.println(value);

									/**
									 * Response for Unread mEssage
									 */
									System.out.println("Response from Server For UNREAD MESSAGE");
									lengthOfData = serverData.readInt();
									// System.out.println(lengthOfData);
									receivedResponse = new byte[lengthOfData];
									serverData.readFully(receivedResponse);
									responseDataFromServer = new String(receivedResponse, "UTF-8");
									source = new InputSource(new StringReader(responseDataFromServer));
									doc = (Document) xpath.evaluate("/", source, XPathConstants.NODE);
									String xvalue = xpath.evaluate("/imap/imap.line_tree/imap.response", doc);

									// System.out.println("Total Message in INBOX :" + value);
									System.out.println(xvalue);

								} /**
									 * Request for Drafts
									 */

								else if (requiredBox.equals("2") || requiredBox.equalsIgnoreCase("draft")) {

									dataToSend = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + "<imap>\r\n"
											+ "	<imap.isrequest>1</imap.isrequest>\r\n" + "	<imap.line>. INBOX\r\n"
											+ "</imap.line>\r\n" + "	<imap.line_tree>\r\n"
											+ "		<imap.request_tag>.</imap.request_tag>\r\n"
											+ "		<imap.request.command>draft</imap.request.command>\r\n"
											+ "		<imap.request>DRAFT</imap.request>\r\n" + "	</imap.line_tree>\r\n"
											+ "</imap>\r\n" + "";

									data = dataToSend.getBytes("UTF-8");
									clientData.writeInt(data.length);
									clientData.write(data);

									System.out.println("Response from Server FOR DRAFTS MESSAGE");
									lengthOfData = serverData.readInt();
									receivedResponse = new byte[lengthOfData];
									serverData.readFully(receivedResponse);
									responseDataFromServer = new String(receivedResponse, "UTF-8");
									// System.out.println(responseDataFromServer);
									source = new InputSource(new StringReader(responseDataFromServer));
									doc = (Document) xpath.evaluate("/", source, XPathConstants.NODE);
									value = xpath.evaluate("/imap/imap.line_tree/imap.response", doc);
									System.out.println(value);

								}
								/**
								 * Client Request for Single email
								 */
								else if (requiredBox.equals("3") || requiredBox.equalsIgnoreCase("single")) {

									while (true) {
										System.out.println(
												"Print message number to check or type 9 to go to the Menus: ");
										String messageNumber = scanner.nextLine();

										if (messageNumber.equals("9")) {

											break;
										}
										System.out.println("Selected Message Number : " + messageNumber);

										String xdataToSend = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n"
												+ "<imap>\r\n" + "	<imap.isrequest>1</imap.isrequest>\r\n"
												+ "	<imap.line>. SINGLE MESSAGE\r\n" + "</imap.line>\r\n"
												+ "	<imap.line_tree>\r\n"
												+ "		<imap.request_tag>.</imap.request_tag>\r\n"
												+ "		<imap.request.command>SINGLE MESSAGE</imap.request.command>\r\n"
												+ "		<imap.request>" + messageNumber + "</imap.request>\r\n"
												+ "	</imap.line_tree>\r\n" + "</imap>\r\n" + "";
										data = xdataToSend.getBytes("UTF-8");
										clientData.writeInt(data.length);
										clientData.write(data);
										if (messageNumber.equalsIgnoreCase("0") || messageNumber.equalsIgnoreCase("1")
												|| messageNumber.equalsIgnoreCase("2")
												|| messageNumber.equalsIgnoreCase("3")
												|| messageNumber.equalsIgnoreCase("4")) {

											System.out.println("Response from Server FOR SPECIFIC MESSAGE");
											lengthOfData = serverData.readInt();
											receivedResponse = new byte[lengthOfData];
											serverData.readFully(receivedResponse);
											responseDataFromServer = new String(receivedResponse, "UTF-8");
											source = new InputSource(new StringReader(responseDataFromServer));
											doc = (Document) xpath.evaluate("/", source, XPathConstants.NODE);
											/**
											 * Receive details for Single email
											 */
											value = xpath.evaluate("/imap/imap.line_tree/imap.response.to", doc);
											String datevalue = xpath.evaluate("/imap/imap.line_tree/imap.response.date",
													doc);
											String fromValue = xpath.evaluate("/imap/imap.line_tree/imap.response.from",
													doc);
											String subjectValue = xpath
													.evaluate("/imap/imap.line_tree/imap.response.subject", doc);
											String bodyValue = xpath.evaluate("/imap/imap.line_tree/imap.response.body",
													doc);
											System.out.println(value);
											System.out.println(datevalue);
											System.out.println(fromValue);
											System.out.println(subjectValue);
											System.out.println(bodyValue);

											System.out.println("Response from Server For UNREAD MESSAGE");
											lengthOfData = serverData.readInt();
											receivedResponse = new byte[lengthOfData];
											serverData.readFully(receivedResponse);
											responseDataFromServer = new String(receivedResponse, "UTF-8");
											source = new InputSource(new StringReader(responseDataFromServer));
											doc = (Document) xpath.evaluate("/", source, XPathConstants.NODE);
											String xvalue = xpath.evaluate("/imap/imap.line_tree/imap.response", doc);
											System.out.println(xvalue);
										}

									}
								}
								/**
								 * delete email
								 */
								else if (requiredBox.equals("4") || requiredBox.equalsIgnoreCase("delete")) {
									System.out.println("Select message number to DELETE : ");
									while (true) {
										System.out.println(
												"Print message number to check or type 9 to go to the Menus: ");
										String deleteMessageNumber = scanner.nextLine();

										if (deleteMessageNumber.equals("9")) {
											break;
										}
										/**
										 * Client Request for delete a email
										 */
										System.out.println("Selected Message Number : " + deleteMessageNumber);
										dataToSend = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n"
												+ "										<imap>\r\n"
												+ "											<imap.isrequest>1</imap.isrequest>\r\n"
												+ "											<imap.line>. DELETE\r\n"
												+ "										</imap.line>\r\n"
												+ "											<imap.line_tree>\r\n"
												+ "												<imap.request_tag>.</imap.request_tag>\r\n"
												+ "												<imap.request.command>DELETE</imap.request.command>\r\n"
												+ "												<imap.requestt>"
												+ deleteMessageNumber + "</imap.requestt>\r\n"
												+ "											</imap.line_tree>\r\n"
												+ "										</imap>";

										data = dataToSend.getBytes("UTF-8");
										clientData.writeInt(data.length);
										clientData.write(data);
										if (deleteMessageNumber.equalsIgnoreCase("0")
												|| deleteMessageNumber.equalsIgnoreCase("1")
												|| deleteMessageNumber.equalsIgnoreCase("2")
												|| deleteMessageNumber.equalsIgnoreCase("3")
												|| deleteMessageNumber.equalsIgnoreCase("4")) {
											System.out.println("Response from Server FOR DELETE SPECIFIC MESSAGE");
											lengthOfData = serverData.readInt();
											receivedResponse = new byte[lengthOfData];
											serverData.readFully(receivedResponse);
											responseDataFromServer = new String(receivedResponse, "UTF-8");
											source = new InputSource(new StringReader(responseDataFromServer));
											doc = (Document) xpath.evaluate("/", source, XPathConstants.NODE);
											value = xpath.evaluate("/imap/imap.line_tree/imap.respons", doc);
											System.out.println(value);
											lengthOfData = serverData.readInt();
											receivedResponse = new byte[lengthOfData];
											serverData.readFully(receivedResponse);
											responseDataFromServer = new String(receivedResponse, "UTF-8");
											source = new InputSource(new StringReader(responseDataFromServer));
											doc = (Document) xpath.evaluate("/", source, XPathConstants.NODE);
											String xvalue = xpath.evaluate("/imap/imap.line_tree/imap.respons", doc);
											System.out.println(xvalue);
										}

									}

								}
								/**
								 * Check all message for client
								 */
								else if (requiredBox.equals("5") || requiredBox.equalsIgnoreCase("all")) {
									dataToSend = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + "<imap>\r\n"
											+ "	<imap.isrequest>1</imap.isrequest>\r\n" + "	<imap.line>. INBOX\r\n"
											+ "</imap.line>\r\n" + "	<imap.line_tree>\r\n"
											+ "		<imap.request_tag>.</imap.request_tag>\r\n"
											+ "		<imap.request.command>INBOX</imap.request.command>\r\n"
											+ "		<imap.request>ALL</imap.request>\r\n" + "	</imap.line_tree>\r\n"
											+ "</imap>\r\n" + "";

									data = dataToSend.getBytes("UTF-8");
									clientData.writeInt(data.length);
									clientData.write(data);
									System.out.println("Response from Server FOR ALL MESSAGES");
									lengthOfData = serverData.readInt();
									receivedResponse = new byte[lengthOfData];
									serverData.readFully(receivedResponse);
									responseDataFromServer = new String(receivedResponse, "UTF-8");
									source = new InputSource(new StringReader(responseDataFromServer));
									doc = (Document) xpath.evaluate("/", source, XPathConstants.NODE);
									value = xpath.evaluate("/imap/imap.line_tree/imap.response", doc);
									System.out.println(value);

								}
								/**
								 * Create a folder
								 */
								else if (requiredBox.equals("6") || requiredBox.equalsIgnoreCase("folder")) {
									// System.out.println("Write a folder name");
									while (true) {
										System.out.println("Write a folder name or Print 9 to go to the Menus: ");
										String folderName = scanner.nextLine();
										if (folderName.equals("9")) {
											break;
										} else {
											dataToSend = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n"
													+ "<imap>\r\n" + "	<imap.isrequest>1</imap.isrequest>\r\n"
													+ "	<imap.line>. INBOX\r\n" + "</imap.line>\r\n"
													+ "	<imap.line_tree>\r\n"
													+ "		<imap.request_tag>.</imap.request_tag>\r\n"
													+ "		<imap.request.command>INBOX</imap.request.command>\r\n"

													+ "	</imap.line_tree>\r\n" + "		<imap.request>" + folderName
													+ "</imap.request>\r\n" + "</imap>\r\n" + "";

											data = dataToSend.getBytes("UTF-8");
											clientData.writeInt(data.length);
											clientData.write(data);
											// System.out.println("Response from Server FOR Transfer MESSAGE");
											// lengthOfData = serverData.readInt();
											// receivedResponse = new byte[lengthOfData];
											// serverData.readFully(receivedResponse);
											// responseDataFromServer = new String(receivedResponse, "UTF-8");
											// source = new InputSource(new StringReader(responseDataFromServer));
											// doc = (Document) xpath.evaluate("/", source, XPathConstants.NODE);
											// value = xpath.evaluate("/imap/imap.line_tree/imap.responsfolder", doc);
											// System.out.println("Folder Name" + value);
										}

									}

								}
								/**
								 * Move a message to a folder
								 */
								else if (requiredBox.equals("7") || requiredBox.equalsIgnoreCase("messageTransfer")) {
									while (true) {
										System.out.println(
												"Type message number to Transfer or type 9 to go to the Menus: ");
										String messageTransfer = scanner.nextLine();

										if (messageTransfer.equals("9")) {
											break;
										}
										System.out.println("Selected Message Number : " + messageTransfer);
										String xdataToSend = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n"
												+ "<imap>\r\n" + "	<imap.isrequest>1</imap.isrequest>\r\n"
												+ "	<imap.line>. SINGLE MESSAGE\r\n" + "</imap.line>\r\n"
												+ "	<imap.line_tree>\r\n"
												+ "		<imap.request_tag>.</imap.request_tag>\r\n"
												+ "		<imap.request.command>SINGLE MESSAGE</imap.request.command>\r\n"
												+ "		<imap.requestm>" + messageTransfer + "</imap.requestm>\r\n"
												+ "	</imap.line_tree>\r\n" + "</imap>\r\n" + "";
										data = xdataToSend.getBytes("UTF-8");
										clientData.writeInt(data.length);
										clientData.write(data);
										if (messageTransfer.equalsIgnoreCase("0")
												|| messageTransfer.equalsIgnoreCase("1")
												|| messageTransfer.equalsIgnoreCase("2")
												|| messageTransfer.equalsIgnoreCase("3")
												|| messageTransfer.equalsIgnoreCase("4")) {
											System.out.println("Response from Server FOR Transfer MESSAGE");
											// lengthOfData = serverData.readInt();
											// receivedResponse = new byte[lengthOfData];
											// serverData.readFully(receivedResponse);
											// responseDataFromServer = new String(receivedResponse, "UTF-8");
											// source = new InputSource(new StringReader(responseDataFromServer));
											// doc = (Document) xpath.evaluate("/", source, XPathConstants.NODE);
											// value = xpath.evaluate("/imap/imap.line_tree/imap.responsedf", doc);
											// System.out.println("Transfered Number" + value);
										}

									}
								}
								/**
								 * Delete a Folder
								 */
								else if (requiredBox.equals("8") || requiredBox.equalsIgnoreCase("deleteFolder")) {
									while (true) {
										System.out
												.println("Write folder name to DELETE or type 9 to go to the Menus: ");
										String folderDelete = scanner.nextLine();

										if (folderDelete.equals("9")) {

											break;
										}
										System.out.println("Selected Folder Name : " + folderDelete);
										/**
										 * Send Request to server for delete a email
										 */
										dataToSend = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + "<imap>\r\n"
												+ "	<imap.isrequest>1</imap.isrequest>\r\n"
												+ "	<imap.line>. SINGLE MESSAGE\r\n" + "</imap.line>\r\n"
												+ "	<imap.line_tree>\r\n"
												+ "		<imap.request_tag>.</imap.request_tag>\r\n"
												+ "		<imap.request.command>SINGLE MESSAGE</imap.request.command>\r\n"
												+ "		<imap.requestf>" + folderDelete + "</imap.requestf>\r\n"
												+ "	</imap.line_tree>\r\n" + "</imap>\r\n" + "";
										data = dataToSend.getBytes("UTF-8");
										clientData.writeInt(data.length);
										clientData.write(data);
									}
								}
								/**
								 * Request for Logout
								 */
								else if (requiredBox.equals("9") || requiredBox.equalsIgnoreCase("logout")) {
									dataToSend = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + "<imap>\r\n"
											+ "	<imap.isrequest>1</imap.isrequest>\r\n" + "	<imap.line>. Log Out\r\n"
											+ "</imap.line>\r\n" + "	<imap.line_tree>\r\n"
											+ "		<imap.request_tag>.</imap.request_tag>\r\n"
											+ "		<imap.request.command>Log Out</imap.request.command>\r\n"
											+ "		<imap.requestl>logout</imap.requestl>\r\n"
											+ "	</imap.line_tree>\r\n" + "</imap>\r\n" + "";

									data = dataToSend.getBytes("UTF-8");
									clientData.writeInt(data.length);
									clientData.write(data);

									System.out.println("Response from Server FOR Logout MESSAGES");
									lengthOfData = serverData.readInt();
									receivedResponse = new byte[lengthOfData];
									serverData.readFully(receivedResponse);
									responseDataFromServer = new String(receivedResponse, "UTF-8");
									// System.out.println(responseDataFromServer);
									source = new InputSource(new StringReader(responseDataFromServer));
									doc = (Document) xpath.evaluate("/", source, XPathConstants.NODE);
									value = xpath.evaluate("/imap/imap.line_tree/imap.responsel", doc);
									System.out.println(value);
									if (value.equalsIgnoreCase("Log Out")) {
										System.out.println("Log out successfully");
										System.exit(0);

									}

								}

							}
						}

					} else {
						System.out.println("Password is not Correct");
					}

				}

			}

		}

	}

}
