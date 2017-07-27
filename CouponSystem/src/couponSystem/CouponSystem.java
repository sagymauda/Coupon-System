package couponSystem;

import ConnectionPool.ConnectionPool;
import DAO.DB.CompanyDBDAO;
import DAO.DB.CustomerDBDAO;
import Exceptions.CouponSystemException;
import Exceptions.connectionException;
import Facades.AdminFacade;
import Facades.ClientCouponFacade;
import Facades.CompanyFacade;
import Facades.CustomerFacade;
import couponSystem.Threads.DailyCouponExpirationTask;

public class CouponSystem {

	private DailyCouponExpirationTask runnableDailyCouponExpirationTask;
	private CompanyDBDAO companyDBDAO;
	private CustomerDBDAO customerDBDAO;
	private Thread dailyCouponExpirationTask;
	private static CouponSystem instance;

	/***
	 * constructor initializes the coupon system by loading local DAOs and
	 * starting the daily thread
	 * 
	 * private constructor insures the coupon system will remain a singleton and
	 * will only be instantiated once within the class by the getInstance
	 * method.
	 * 
	 * @throws connectionException
	 *             if the coupon system could not start up due to a connection
	 *             issue
	 */
	private CouponSystem() throws connectionException {
		companyDBDAO = new CompanyDBDAO();
		customerDBDAO = new CustomerDBDAO();
		runnableDailyCouponExpirationTask = new DailyCouponExpirationTask();
		dailyCouponExpirationTask = new Thread(runnableDailyCouponExpirationTask, "dailyCouponExpirationTask");
		runnableDailyCouponExpirationTask.setThread(dailyCouponExpirationTask);
		dailyCouponExpirationTask.start();
	}

	/***
	 * getInstance returns the only instance of the coupon system. if one has
	 * not yet been initialized this method will do so.
	 * 
	 * @return the only instance of the CouponSystem singleton
	 * @throws connectionException
	 *             if the coupon system could not start up due to a connection
	 *             issue.
	 */
	public static CouponSystem getInstance() throws connectionException {
		if (instance == null) {
			instance = new CouponSystem();
		}
		return instance;
	}

	/***
	 * login methods allows a user to connect to the coupon system by checking
	 * if the username and password provided match these credentials in the data
	 * base according to the provided client type and client id. if the
	 * parameters passed in to this method match those in the database the
	 * method will return the relevant client facade (according to the provided
	 * clientType)
	 * 
	 * @param id
	 *            identifier of the user requesting login assigned to the user
	 *            upon sign up to the coupon system..
	 * @param username
	 *            the customer or company name used upon sign up to the coupon
	 *            system. admin username is simply "admin".
	 * @param password
	 *            the password of the user.
	 * @param clientType
	 *            the type of login that is required, options are
	 *            currently:ADMIN, COMPANY, CUSTOMER.
	 * @return ClientCouponFacade a user facade specified by the provided
	 *         clientType.
	 * @throws CouponSystemException
	 *             if one or more of the parameters is empty or incorrect
	 */

	public ClientCouponFacade login(long id, String username, String password, ClientType clientType)
			throws CouponSystemException {
		switch (clientType) {
		case ADMIN:
			if (username.equals("admin") && password.equals("1234")) {
				return new AdminFacade();
			}
			break;
		case COMPANY:
			if (companyDBDAO.login(id, username, password)) {
				return new CompanyFacade(id);
			}
			break;
		case CUSTOMER:
			if (customerDBDAO.login(id, username, password)) {
				return new CustomerFacade(id);
			}
			break;
		default:
			throw new CouponSystemException("cannot login,client type is incorrect or empty.");
		}
		throw new CouponSystemException(
				"cannot login, username and password are incorrect or do not match the specified client type.");

	}

	/***
	 * 
	 * shutDown turns off the entire coupon system by closing all connections in
	 * the connection pool, and stopping the daily thread tasked with deleting
	 * expired coupons.
	 * 
	 * @throws connectionException
	 *             if the coupon system could not shut down properly due to a
	 *             connection issue
	 */

	public void shutDown() throws CouponSystemException {
		runnableDailyCouponExpirationTask.stopTask();
		ConnectionPool.getInstance().closeAllConnections();
	}

}
