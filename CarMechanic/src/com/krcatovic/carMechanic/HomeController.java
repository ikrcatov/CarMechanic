package com.krcatovic.carMechanic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({ "sessionUsername", "sessionPassword", "idApplicationUser" })
public class HomeController {
	private static final Logger logger = LoggerFactory
			.getLogger(HomeController.class);
	private DataSource dataSource;
	private String ADMINISTRATOR_ROLE = "ADMINISTRATOR";
	private String MECHANIC_ROLE = "MECHANIC";
	private String CLIENT_ROLE = "CLIENT";

	public DataSource getDataSource() {
		return this.dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@ModelAttribute("sessionUsername")
	public String populateUserName() {
		return "";
	}

	@ModelAttribute("sessionPassword")
	public String populatePassword() {
		return "";
	}

	@ModelAttribute("idApplicationUser")
	public Integer populateIdApplicationUser() {
		return Integer.valueOf(0);
	}

	@RequestMapping(value = { "/" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String home(Locale locale, Model model) {
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(1, 1, locale);
		String formattedDate = dateFormat.format(date);

		logger.info("Index Page Opened", formattedDate);

		return "home";
	}

	@RequestMapping({ "/loginScreen" })
	public ModelAndView get(
			@RequestParam(required = false, value = "usernameID") String usernameID,
			@RequestParam(required = false, value = "passwordId") String passwordId,
			@RequestParam(required = false, value = "rememberId") String rememberId,
			Model model,
			@ModelAttribute("sessionUsername") String sessionUsername,
			@ModelAttribute("sessionPassword") String sessionPassword) {
		Clients clientForm = new Clients();
		List<Client> clients = new ArrayList();

		Tasks tasks = new Tasks();
		List<Task> taskList = new ArrayList();

		String username = "";
		String password = "";
		String cBox = rememberId;
		if ((!Helper.isStringNullEmpty(usernameID).booleanValue())
				&& (!Helper.isStringNullEmpty(passwordId).booleanValue())) {
			username = usernameID;
			password = passwordId;
		} else if ((!Helper.isStringNullEmpty(sessionUsername).booleanValue())
				&& (!Helper.isStringNullEmpty(sessionPassword).booleanValue())) {
			username = sessionUsername;
			password = sessionPassword;
		}
		if ((!Helper.isStringNullEmpty(username).booleanValue())
				&& (!Helper.isStringNullEmpty(password).booleanValue())) {
			model.addAttribute("sessionUsername", username);
			model.addAttribute("sessionPassword", password);

			Integer idApplicationUserFetched = fetchIdApplicationUserByUsernameAndPassword(
					username.trim(), password.trim());
			if (idApplicationUserFetched != null) {
				model.addAttribute("idApplicationUser",
						idApplicationUserFetched);
			}
			String applicationUserRole = fetchApplicationUserRoleByUsernameAndPassword(
					username.trim(), password.trim());
			if ((!Helper.isStringNullEmpty(applicationUserRole).booleanValue())
					&& ((applicationUserRole.equals(this.ADMINISTRATOR_ROLE)) || (applicationUserRole
							.equals(this.MECHANIC_ROLE)))) {
				clients = fetchAllClientsFromDatabase(username.trim(),
						password.trim());
				if ((clients != null) && (clients.size() > 0)) {
					clientForm.setClients(clients);
					if (!Helper.isStringNullEmpty(cBox).booleanValue()) {
						clientForm.setPassword(password);
						clientForm.setUsername(username);
						clientForm.setRememberCbox(cBox);
					}
					logger.info("Client List Opened");
					return new ModelAndView("loginScreen", "clientForm",
							clientForm);
				}
				clientForm.setClients(clients);

				logger.info("Client List Opened");
				return new ModelAndView("loginScreen", "clientForm", clientForm);
			}
			if ((!Helper.isStringNullEmpty(applicationUserRole).booleanValue())
					&& (applicationUserRole.equals(this.CLIENT_ROLE))) {
				taskList = fetchAllTasksFromDatabase(username.trim(),
						password.trim());
				if ((taskList != null) && (taskList.size() > 0)) {
					tasks.setTasks(taskList);
					if (!Helper.isStringNullEmpty(cBox).booleanValue()) {
						tasks.setPassword(password);
						tasks.setUsername(username);
						tasks.setRememberCbox(cBox);
					}
					logger.info("Task List Opened");
					return new ModelAndView("clientTasks", "clientTaskModel",
							tasks);
				}
				tasks.setTasks(taskList);
				logger.info("Task List Opened");
				return new ModelAndView("clientTasks", "clientTaskModel", tasks);
			}
			model.addAttribute("loginError",
					"Incorrect login credentials! Please try again!");
		}
		logger.info("Returned to Index");
		return new ModelAndView("home", "homeModelAttribute", clientForm);
	}

	private Integer fetchIdApplicationUserByUsernameAndPassword(
			String userName, String password) {
		String getRoleNameByUsernamePasswordQuery = "SELECT ID FROM APPLICATION_USER AU WHERE AU.USERNAME = '"
				+ userName
				+ "' "
				+ "AND AU.PASSWORD = '"
				+ password
				+ "' AND AU.DELETED = 0 ORDER BY ID";

		Integer idAppUser = Integer.valueOf(0);

		Connection conn = null;
		try {
			conn = this.dataSource.getConnection();
			PreparedStatement ps = conn
					.prepareStatement(getRoleNameByUsernamePasswordQuery);

			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					idAppUser = Integer.valueOf(rs.getInt("ID"));
				}
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		}
		if (idAppUser != null) {
			return idAppUser;
		}
		return Integer.valueOf(0);
	}

	private String fetchApplicationUserRoleByUsernameAndPassword(
			String username, String password) {
		String getRoleNameByUsernamePasswordQuery = "SELECT NAME FROM APPLICATION_USER_ROLE AUR INNER JOIN APPLICATION_USER AU ON AU.ID_APPLICATION_USER_ROLE = AUR.ID WHERE AU.USERNAME = '"
				+ username
				+ "' AND AU.PASSWORD = '"
				+ password
				+ "' "
				+ "AND AUR.ID = AU.ID_APPLICATION_USER_ROLE  AND AUR.DELETED = 0 AND AU.DELETED = 0 ORDER BY NAME";

		String rola = "";

		Connection conn = null;
		try {
			conn = this.dataSource.getConnection();
			PreparedStatement ps = conn
					.prepareStatement(getRoleNameByUsernamePasswordQuery);

			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					rola = rs.getString("NAME");
				}
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		}
		if (!Helper.isStringNullEmpty(rola).booleanValue()) {
			return rola;
		}
		return "";
	}

	private List<Client> fetchAllClientsFromDatabase(String username,
			String password) {
		String getAllClientsForByUsernamePasswordQuerys = "SELECT C.ID, C.FIRST_NAME, C.LAST_NAME, C.PHONE FROM CLIENT C LEFT JOIN APPLICATION_USER AU ON C.ID_APPLICATION_USER = AU.ID WHERE AU.USERNAME = '"
				+

				username
				+ "' AND AU.PASSWORD = '"
				+ password
				+ "' AND C.DELETED = 0 "
				+ "AND AU.DELETED = 0 ORDER BY C.ID, C.FIRST_NAME, C.LAST_NAME";

		List<Client> clients = new ArrayList();

		Connection conn = null;
		try {
			conn = this.dataSource.getConnection();
			PreparedStatement ps = conn
					.prepareStatement(getAllClientsForByUsernamePasswordQuerys);

			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					clients.add(new Client(rs.getInt("ID"), rs
							.getString("FIRST_NAME"),
							rs.getString("LAST_NAME"), rs.getString("PHONE")));
				}
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		}
		if ((clients != null) && (!clients.isEmpty())) {
			return clients;
		}
		return new ArrayList();
	}

	private List<Task> fetchAllTasksFromDatabase(String username,
			String password) {
		String getAllTasksForByUsernamePasswordQuerys = "SELECT T.ID, T.NAME, T.REMARK, T.STATUS FROM TASK T WHERE ID_CLIENT = (SELECT AU.ID_CLIENT FROM APPLICATION_USER AU WHERE AU.USERNAME = '"
				+ username
				+ "' AND AU.PASSWORD = '"
				+ password
				+ "' "
				+ "AND AU.DELETED = 0) AND T.DELETED = 0 ORDER BY T.ID, T.NAME, T.REMARK";

		List<Task> taskList = new ArrayList();

		Connection conn = null;
		try {
			conn = this.dataSource.getConnection();
			PreparedStatement ps = conn
					.prepareStatement(getAllTasksForByUsernamePasswordQuerys);

			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					taskList.add(new Task(rs.getInt("ID"),
							rs.getString("NAME"), rs.getString("REMARK"), rs
							.getString("STATUS")));
				}
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		}
		if ((taskList != null) && (!taskList.isEmpty())) {
			return taskList;
		}
		return new ArrayList();
	}

	@RequestMapping({ "/logout" })
	public String logout(
			Model model,
			@RequestParam String usernameHidden,
			@RequestParam String passwordHidden,
			@RequestParam(required = false, value = "rememberHidden") String rememberHidden) {
		String username = usernameHidden;
		String password = passwordHidden;
		String cBox = rememberHidden;
		if (!Helper.isStringNullEmpty(cBox).booleanValue()) {
			model.addAttribute("usernameValue", username);
			model.addAttribute("passwordValue", password);
			model.addAttribute("cBoxValue", cBox);
			model.addAttribute("logOutFlag", "true");
		}
		logger.info("LogOut Executed");
		return "home";
	}

	@RequestMapping({ "/openCLientTaskForMechanicAndAdministrator" })
	public ModelAndView openCLientTaskForMechanicAndAdministrator(
			@RequestParam String rowIdClientHidden) {
		List<Task> taskList = new ArrayList();
		Tasks tasks = new Tasks();
		Connection conn = null;
		if (!Helper.isStringNullEmpty(rowIdClientHidden).booleanValue()) {
			String getAllTasksByRowId = "SELECT T.ID, T.NAME, T.REMARK, T.STATUS FROM TASK T WHERE T.ID_CLIENT IN (SELECT ID FROM CLIENT C WHERE C.ID = "
					+ rowIdClientHidden
					+ " AND C.DELETED = 0 ) AND T.DELETED = 0 "
					+ "ORDER BY T.ID, T.NAME, T.REMARK, T.STATUS";
			try {
				conn = this.dataSource.getConnection();
				PreparedStatement ps = conn
						.prepareStatement(getAllTasksByRowId);

				ResultSet rs = ps.executeQuery();
				if (rs != null) {
					while (rs.next()) {
						taskList.add(new Task(rs.getInt("ID"), rs
								.getString("NAME"), rs.getString("REMARK"), rs
								.getString("STATUS")));
					}
				}
				rs.close();
				ps.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						throw new RuntimeException(e);
					}
				}
			}
			if ((taskList != null) && (!taskList.isEmpty())) {
				tasks.setTasks(taskList);
				tasks.setRowIdClient(Integer.valueOf(rowIdClientHidden));

				logger.info("Task List For Mechanic By Client Opened");
				return new ModelAndView("clientTasksForMechanic",
						"clientTaskForMechanicModel", tasks);
			}
			tasks.setTasks(taskList);
			tasks.setRowIdClient(Integer.valueOf(rowIdClientHidden));

			logger.info("Task List For Mechanic By Client Opened");
			return new ModelAndView("clientTasksForMechanic",
					"clientTaskForMechanicModel", tasks);
		}
		tasks.setTasks(taskList);

		logger.info("Task List For Mechanic By Client Opened");
		return new ModelAndView("clientTasksForMechanic",
				"clientTaskForMechanicModel", tasks);
	}

	@RequestMapping(value = { "/deleteItemByRowId" }, consumes = { "application/json" }, headers = { "content-type=application/x-www-form-urlencoded" })
	public String deleteItemByRowId(@RequestParam String RowId) {
		if (!Helper.isStringNullEmpty(RowId).booleanValue()) {
			String deleteRowByIdQuery = "UPDATE TASK SET DELETED = 1 WHERE ID = "
					+ RowId;

			Connection conn = null;
			try {
				conn = this.dataSource.getConnection();
				PreparedStatement ps = conn
						.prepareStatement(deleteRowByIdQuery);
				ps.executeUpdate();
				ps.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						throw new RuntimeException(e);
					}
				}
			}
			return "clientTasksForMechanic";
		}
		return "clientTasksForMechanic";
	}

	@RequestMapping(value = { "/saveItemByRowId" }, consumes = { "application/json" }, headers = { "content-type=application/x-www-form-urlencoded" })
	public String saveItemByRowId(
			@RequestParam(required = false, value = "parsedDataByDelimiters") String parsedDataByDelimiters,
			@RequestParam(required = false, value = "rowIdClientHidden") String rowIdClientHidden,
			@ModelAttribute("idApplicationUser") Integer idApplicationUser) {
		if (!Helper.isStringNullEmpty(parsedDataByDelimiters).booleanValue()) {
			Connection conn = null;

			String rowId = "";
			String taskName = "";
			String taskDescription = "";
			String taskStatus = "";

			String mainDelimiter = ";";
			String secondaryDelimiter = ":";

			String[] mainRows = parsedDataByDelimiters.split(mainDelimiter);
			String[] itemsBetweenRow = null;

			String saveRowByIdQuery = "";
			if ((mainRows != null) && (mainRows.length > 0)) {
				for (int i = 0; i < mainRows.length; i++) {
					itemsBetweenRow = mainRows[i].split(secondaryDelimiter);
					if ((itemsBetweenRow != null)
							&& (itemsBetweenRow.length > 0)) {
						for (int j = 0; j < itemsBetweenRow.length; j++) {
							if (j == 0) {
								rowId = itemsBetweenRow[j];
							} else if (j == 1) {
								taskName = itemsBetweenRow[j];
							} else if (j == 2) {
								taskDescription = itemsBetweenRow[j];
							} else if (j == 3) {
								taskStatus = itemsBetweenRow[j];
							}
						}
						if (Helper.isStringNullEmpty(rowId).booleanValue()) {
							saveRowByIdQuery = "INSERT INTO TASK (ID_CLIENT, ID_APPLICATION_USER, NAME, REMARK, STATUS, DELETED) VALUES("
									+ rowIdClientHidden
									+ ", "
									+ idApplicationUser
									+ ", '"
									+ taskName
									+ "', '"
									+ taskDescription
									+ "', '"
									+ taskStatus + "', 0)";
						} else {
							saveRowByIdQuery = "UPDATE TASK T SET T.NAME = '"
									+ taskName + "', T.REMARK = '"
									+ taskDescription + "', " + "T.STATUS = '"
									+ taskStatus + "' WHERE ID = " + rowId;
						}
						try {
							conn = this.dataSource.getConnection();
							PreparedStatement ps = conn
									.prepareStatement(saveRowByIdQuery);
							ps.executeUpdate();
							ps.close();
						} catch (SQLException e) {
							throw new RuntimeException(e);
						} finally {
							if (conn != null) {
								try {
									conn.close();
								} catch (SQLException e) {
									throw new RuntimeException(e);
								}
							}
						}
					}
				}
			}
			return "clientTasksForMechanic";
		}
		return "clientTasksForMechanic";
	}

	@RequestMapping(value = { "/deleteClientByRowId" }, consumes = { "application/json" }, headers = { "content-type=application/x-www-form-urlencoded" })
	public String deleteClientByRowId(@RequestParam String RowId) {
		if (!Helper.isStringNullEmpty(RowId).booleanValue()) {
			String deleteRowByIdQuery = "UPDATE CLIENT SET DELETED = 1 WHERE ID = "
					+ RowId;

			Connection conn = null;
			try {
				conn = this.dataSource.getConnection();
				PreparedStatement ps = conn
						.prepareStatement(deleteRowByIdQuery);
				ps.executeUpdate();
				ps.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						throw new RuntimeException(e);
					}
				}
			}
			String deleteCredentialsRowByIdQuery = "UPDATE USER_CREDENTIALS SET DELETED = 1 WHERE ID_CLIENT = "
					+ RowId;
			try {
				conn = this.dataSource.getConnection();
				PreparedStatement ps = conn
						.prepareStatement(deleteCredentialsRowByIdQuery);
				ps.executeUpdate();
				ps.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						throw new RuntimeException(e);
					}
				}
			}
			String deleteTasksRowByIdQuery = "UPDATE TASK SET DELETED = 1 WHERE ID_CLIENT = "
					+ RowId;
			try {
				conn = this.dataSource.getConnection();
				PreparedStatement ps = conn
						.prepareStatement(deleteTasksRowByIdQuery);
				ps.executeUpdate();
				ps.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						throw new RuntimeException(e);
					}
				}
			}
			String deleteApplicationUserRowByIdQuery = "UPDATE APPLICATION_USER SET DELETED = 1 WHERE ID_CLIENT = "
					+ RowId;
			try {
				conn = this.dataSource.getConnection();
				PreparedStatement ps = conn
						.prepareStatement(deleteApplicationUserRowByIdQuery);
				ps.executeUpdate();
				ps.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						throw new RuntimeException(e);
					}
				}
			}
			return "loginScreen";
		}
		return "loginScreen";
	}

	@RequestMapping(value = { "/saveClientsByRowId" }, consumes = { "application/json" }, headers = { "content-type=application/x-www-form-urlencoded" })
	public String saveClientsByRowId(
			@RequestParam(required = false, value = "parsedDataByDelimiters") String parsedDataByDelimiters,
			@ModelAttribute("idApplicationUser") Integer idApplicationUser) {
		if (!Helper.isStringNullEmpty(parsedDataByDelimiters).booleanValue()) {
			Connection conn = null;

			String mainDelimiter = ";";
			String secondaryDelimiter = ":";

			String[] mainRows = parsedDataByDelimiters.split(mainDelimiter);
			String[] itemsBetweenRow = null;

			String saveRowByIdQuery = "";
			if ((mainRows != null) && (mainRows.length > 0)) {
				for (int i = 0; i < mainRows.length; i++) {
					itemsBetweenRow = mainRows[i].split(secondaryDelimiter);

					String rowId = "";
					String firstName = "";
					String lastName = "";
					String phone = "";
					if ((itemsBetweenRow != null)
							&& (itemsBetweenRow.length > 0)) {
						for (int j = 0; j < itemsBetweenRow.length; j++) {
							if (j == 0) {
								rowId = itemsBetweenRow[j];
							} else if (j == 1) {
								firstName = itemsBetweenRow[j];
							} else if (j == 2) {
								lastName = itemsBetweenRow[j];
							} else if (j == 3) {
								phone = itemsBetweenRow[j];
							}
						}
						if (Helper.isStringNullEmpty(rowId).booleanValue()) {
							saveRowByIdQuery = "INSERT INTO CLIENT (ID_APPLICATION_USER, FIRST_NAME, LAST_NAME, PHONE,DELETED) VALUES("
									+ idApplicationUser
									+ ", '"
									+ firstName
									+ "', '"
									+ lastName
									+ "', '"
									+ phone
									+ "', 0)";
							try {
								conn = this.dataSource.getConnection();
								PreparedStatement ps = conn
										.prepareStatement(saveRowByIdQuery);
								ps.executeUpdate();
								ps.close();
							} catch (SQLException e) {
								throw new RuntimeException(e);
							} finally {
								if (conn != null) {
									try {
										conn.close();
									} catch (SQLException e) {
										throw new RuntimeException(e);
									}
								}
							}
						}
						if (!Helper.isStringNullEmpty(firstName).booleanValue()) {
							if (((Helper.isStringNullEmpty(lastName)
									.booleanValue() ? 0 : 1) & (Helper
											.isStringNullEmpty(phone).booleanValue() ? 0
													: 1)) != 0) {
								saveRowByIdQuery = "UPDATE CLIENT C SET C.FIRST_NAME = '"
										+ firstName
										+ "', "
										+ "C.LAST_NAME = '"
										+ lastName
										+ "', C.PHONE = '"
										+ phone
										+ "' WHERE ID = " + rowId;
								try {
									conn = this.dataSource.getConnection();
									PreparedStatement ps = conn
											.prepareStatement(saveRowByIdQuery);
									ps.executeUpdate();
									ps.close();
								} catch (SQLException e) {
									throw new RuntimeException(e);
								} finally {
									if (conn != null) {
										try {
											conn.close();
										} catch (SQLException e) {
											throw new RuntimeException(e);
										}
									}
								}
							}
						}
					}
				}
			}
			return "loginScreen";
		}
		return "loginScreen";
	}

	@RequestMapping(value = { "/getClientCredentialsByRowId" }, consumes = { "application/json" }, headers = { "content-type=application/x-www-form-urlencoded" })
	@ResponseBody
	public ClientCredentials getClientCredentialsByRowId(
			@RequestParam(required = false, value = "RowId") String RowId,
			@ModelAttribute("idApplicationUser") Integer idApplicationUser) {
		ClientCredentials credentials = new ClientCredentials();
		List<ClientCredential> clientData = new ArrayList();
		Connection conn = null;
		if (!Helper.isStringNullEmpty(RowId).booleanValue()) {
			String selectRowByIdQuery = "SELECT AU.FIRST_NAME, AU.LAST_NAME, AU.USERNAME, AU.PASSWORD FROM APPLICATION_USER AU LEFT JOIN CLIENT C LEFT JOIN USER_CREDENTIALS UC ON C.ID = UC.ID_CLIENT ON AU.ID = UC.ID_APPLICATION_USER WHERE AU.ID = UC.ID_APPLICATION_USER AND C.ID = UC.ID_CLIENT AND C.ID = "
					+

					RowId
					+ " AND AU.DELETED = 0 AND C.DELETED = 0 AND UC.DELETED = 0 ";
			try {
				conn = this.dataSource.getConnection();
				PreparedStatement ps = conn
						.prepareStatement(selectRowByIdQuery);

				ResultSet rs = ps.executeQuery();
				if (rs != null) {
					while (rs.next()) {
						clientData.add(new ClientCredential(rs
								.getString("FIRST_NAME"), rs
								.getString("LAST_NAME"), rs
								.getString("USERNAME"), rs
								.getString("PASSWORD")));
					}
				}
				ps.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						throw new RuntimeException(e);
					}
				}
			}
			if ((clientData != null) && (!clientData.isEmpty())) {
				credentials.setClientCredentials(clientData);

				return credentials;
			}
			credentials.setClientCredentials(clientData);

			return credentials;
		}
		credentials.setClientCredentials(clientData);

		return credentials;
	}

	@RequestMapping(value = { "/saveUserCredentials" }, consumes = { "application/json" }, headers = { "content-type=application/x-www-form-urlencoded" })
	public String saveUserCredentials(
			@RequestParam(required = false, value = "RowId") String RowId,
			@RequestParam(required = false, value = "usernameInputHidden") String usernameInput,
			@RequestParam(required = false, value = "passwordInputHidden") String passwordInput,
			@ModelAttribute("idApplicationUser") Integer idApplicationUser) {
		if ((!Helper.isStringNullEmpty(RowId).booleanValue())
				&& (!Helper.isStringNullEmpty(usernameInput).booleanValue())
				&& (!Helper.isStringNullEmpty(passwordInput).booleanValue())) {
			String firstName = "";
			String lastName = "";
			String idApplicationUserClient = "";

			String selectRowByIdQuery = "SELECT C.FIRST_NAME, C.LAST_NAME FROM CLIENT C WHERE C.DELETED = 0 AND C.ID = "
					+ RowId;
			Connection conn = null;
			try {
				conn = this.dataSource.getConnection();
				PreparedStatement ps = conn
						.prepareStatement(selectRowByIdQuery);

				ResultSet rs = ps.executeQuery();
				if (rs != null) {
					while (rs.next()) {
						firstName = rs.getString("FIRST_NAME");
						lastName = rs.getString("LAST_NAME");
					}
				}
				ps.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						throw new RuntimeException(e);
					}
				}
			}
			if ((!Helper.isStringNullEmpty(firstName).booleanValue())
					&& (!Helper.isStringNullEmpty(lastName).booleanValue())) {
				List<ClientCredential> clientData = new ArrayList();

				String selectItemFromAppUserQuery = "SELECT AU.FIRST_NAME, AU.LAST_NAME, AU.USERNAME, AU.PASSWORD FROM APPLICATION_USER AU LEFT JOIN CLIENT C LEFT JOIN USER_CREDENTIALS UC ON C.ID = UC.ID_CLIENT ON AU.ID = UC.ID_APPLICATION_USER WHERE AU.ID = UC.ID_APPLICATION_USER AND C.ID = UC.ID_CLIENT AND C.ID = "
						+

						RowId
						+ " AND AU.DELETED = 0 AND C.DELETED = 0 AND UC.DELETED = 0 ";
				try {
					conn = this.dataSource.getConnection();
					PreparedStatement ps = conn
							.prepareStatement(selectItemFromAppUserQuery);

					ResultSet rs = ps.executeQuery();
					if (rs != null) {
						while (rs.next()) {
							clientData.add(new ClientCredential(rs
									.getString("FIRST_NAME"), rs
									.getString("LAST_NAME"), rs
									.getString("USERNAME"), rs
									.getString("PASSWORD")));
						}
					}
					ps.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				} finally {
					if (conn != null) {
						try {
							conn.close();
						} catch (SQLException e) {
							throw new RuntimeException(e);
						}
					}
				}
				if ((clientData != null) && (!clientData.isEmpty())) {
					String selectQueryId = "SELECT AU.ID FROM APPLICATION_USER AU WHERE AU.DELETED = 0 AND AU.FIRST_NAME = '"
							+ ((ClientCredential) clientData.get(0))
							.getFirstname()
							+ "' AND AU.LAST_NAME = '"
							+ ((ClientCredential) clientData.get(0))
							.getLastname() + "'";
					try {
						conn = this.dataSource.getConnection();
						PreparedStatement ps = conn
								.prepareStatement(selectQueryId);
						ResultSet rs = ps.executeQuery();
						if (rs != null) {
							while (rs.next()) {
								idApplicationUserClient = rs.getString("ID");
							}
						}
						ps.close();
					} catch (SQLException e) {
						throw new RuntimeException(e);
					} finally {
						if (conn != null) {
							try {
								conn.close();
							} catch (SQLException e) {
								throw new RuntimeException(e);
							}
						}
					}
					String updateQuery = "UPDATE APPLICATION_USER AU SET AU.USERNAME = '"
							+ usernameInput
							+ "', "
							+ "AU.PASSWORD = '"
							+ passwordInput
							+ "', AU.ID_CLIENT = '"
							+ RowId
							+ "' WHERE ID = " + idApplicationUserClient;
					try {
						conn = this.dataSource.getConnection();
						PreparedStatement ps = conn
								.prepareStatement(updateQuery);
						ps.executeUpdate();
						ps.close();
					} catch (SQLException e) {
						throw new RuntimeException(e);
					} finally {
						if (conn != null) {
							try {
								conn.close();
							} catch (SQLException e) {
								throw new RuntimeException(e);
							}
						}
					}
				}
				Connection conn1 = null;

				String insertQuery = "INSERT INTO APPLICATION_USER (ID_APPLICATION_USER_ROLE, FIRST_NAME, LAST_NAME, USERNAME, PASSWORD, DELETED, ID_CLIENT)VALUES (3, '"
						+ firstName
						+ "', '"
						+ lastName
						+ "', '"
						+ usernameInput
						+ "', '"
						+ passwordInput
						+ "', 0, "
						+ RowId + ")";

				String selectQueryId = "SELECT AU.ID FROM APPLICATION_USER AU WHERE AU.DELETED = 0 AND AU.FIRST_NAME = '"
						+ firstName + "' AND AU.LAST_NAME = '" + lastName + "'";
				try {
					conn1 = this.dataSource.getConnection();
					PreparedStatement ps = conn1.prepareStatement(insertQuery);
					ps.executeUpdate();
					ps.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				} finally {
					if (conn1 != null) {
						try {
							conn1.close();
						} catch (SQLException e) {
							throw new RuntimeException(e);
						}
					}
				}
				try {
					conn = this.dataSource.getConnection();
					PreparedStatement ps = conn.prepareStatement(selectQueryId);
					ResultSet rs = ps.executeQuery();
					if (rs != null) {
						while (rs.next()) {
							idApplicationUserClient = rs.getString("ID");
						}
					}
					ps.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				} finally {
					if (conn != null) {
						try {
							conn.close();
						} catch (SQLException e) {
							throw new RuntimeException(e);
						}
					}
				}
				try {
					String insertQuery2 = "INSERT INTO USER_CREDENTIALS (ID_APPLICATION_USER, ID_CLIENT, DELETED)VALUES ("
							+ idApplicationUserClient + ", " + RowId + ", 0)";

					conn = this.dataSource.getConnection();
					PreparedStatement ps = conn.prepareStatement(insertQuery2);
					ps.executeUpdate();
					ps.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				} finally {
					if (conn != null) {
						try {
							conn.close();
						} catch (SQLException e) {
							throw new RuntimeException(e);
						}
					}
				}
			}
		}
		return "loginScreen";
	}
}