package DAO.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import ConnectionPool.ConnectionPool;
import DAO.Interfaces.CustomerDAO;
import Exceptions.CouponSystemException;
import Exceptions.connectionException;
import javaBeans.Coupon;
import javaBeans.CouponType;
import javaBeans.Customer;

public class CustomerDBDAO implements CustomerDAO {
	private ConnectionPool pool;

	/**
	 * Constructs an CustomerDBDAO instance and loads the connection pool.
	 * 
	 * @exception connectionException
	 *                if the database is down or a connection couldn't be
	 *                established.
	 */

	public CustomerDBDAO() throws connectionException {
		pool = ConnectionPool.getInstance();
	}

	/**
	 * createCustomer enters a Customer object into the Customer table in the
	 * database
	 * 
	 * @param Customer
	 *            is a Customer object
	 * @throws connectionException
	 *             if a connection could not be retrieved
	 * @throws CouponSystemException
	 *             if a database access error occurs
	 * 
	 */
	@Override
	public void createCustomer(Customer cst) throws CouponSystemException {
		Connection con = null;
		try {
			con = pool.getConnection();
			PreparedStatement prep = con.prepareStatement("INSERT INTO customer(cust_name,password) VALUES(?,?)");
			prep.setString(1, cst.getCust_name());
			prep.setString(2, cst.getPassword());
			prep.executeUpdate();
		} catch (connectionException e) {

			throw e;
		} catch (SQLException e) {
			throw new CouponSystemException(" createCustomer failed. error accessing the database", e);

		} finally {
			pool.returnConnection(con);

		}

	}

	/**
	 * removeCustomer removes a specified record of a customer from the customer
	 * table in the database.
	 * 
	 * @param customer
	 *            is a customer object representing a record to be removed
	 * @throws connectionException
	 *             if a connection could not be retrieved
	 * @throws CouponSystemException
	 *             if a database access error occurs
	 * 
	 */

	@Override
	public void removeCustomer(Customer cst) throws CouponSystemException {
		Connection con = null;
		try {
			con = pool.getConnection();
			PreparedStatement prep = con.prepareStatement("DELETE FROM customer WHERE id=?");
			prep.setLong(1, cst.getCust_id());
			prep.executeUpdate();
		} catch (connectionException e) {

			throw e;
		} catch (SQLException e) {
			throw new CouponSystemException(" createCustomer failed. error accessing the database", e);

		} finally {
			pool.returnConnection(con);
		}

	}

	/**
	 * updateCustomer updates the values of a customer record in the customer
	 * table in the database. the id field cannot be updated
	 * 
	 * @param customer
	 *            a customer object representing an existing customer record in
	 *            the DB.
	 * @throws connectionException
	 *             if a connection could not be retrieved
	 * @throws CouponSystemException
	 *             if a database access error occurs
	 */

	@Override
	public void updateCustomer(Customer cst) throws CouponSystemException {
		Connection con = null;
		try {
			con = pool.getConnection();
			PreparedStatement prep = con.prepareStatement("UPDATE  customer SET cust_name=?,password=? WHERE id=?");

			prep.setString(1, cst.getCust_name());
			prep.setString(2, cst.getPassword());
			prep.setLong(3, cst.getCust_id());
			prep.executeUpdate();

		} catch (connectionException e) {

			throw e;
		} catch (SQLException e) {
			throw new CouponSystemException("update Customer failed. error accessing the database", e);

		} finally {
			pool.returnConnection(con);

		}

	}

	/**
	 * getCustomer retrieves a specified customer record from the customer table
	 * in the database.
	 * 
	 * @param id
	 *            the identifier for the desired customer
	 * @return a customer object representing the desired customer record
	 * 
	 * @throws connectionException
	 *             if a connection could not be retrieved
	 * @throws CouponSystemException
	 *             if a database access error occurs
	 */
	@Override
	public Customer getCustomer(long id) throws CouponSystemException {
		Connection con = null;
		Customer cst = new Customer();
		try {
			con = pool.getConnection();

			PreparedStatement getC = con.prepareStatement("SELECT *FROM customer WHERE id=?");
			getC.setLong(1, id);
			ResultSet result = getC.executeQuery();
			if (result.next()) {
				cst = ResultSetToCustomer(result);
				return cst;
			} else {
				throw new CouponSystemException("no customer matching this id was found");
			}
		} catch (connectionException e) {

			throw e;
		} catch (SQLException e) {
			throw new CouponSystemException("update Customer failed. error accessing the database", e);

		} finally {
			pool.returnConnection(con);

		}

	}

	/**
	 * getAllCustomers returns all of the customer records from the customer
	 * table in the database.
	 * 
	 * @return hashSet of customer objects representing the records in the
	 *         customer table
	 * 
	 * @throws connectionException
	 *             if a connection could not be retrieved
	 * @throws CouponSystemException
	 *             if a database access error occurs
	 */
	@Override
	public Set<Customer> getAllCustomers() throws CouponSystemException {
		Connection con = null;
		Set<Customer> cstSet = new HashSet<>();
		try {
			con = pool.getConnection();
			Statement getAll = con.createStatement();
			ResultSet result = getAll.executeQuery("SELECT  * FROM customer ");
			while (result.next()) {
				Customer cst = ResultSetToCustomer(result);
				cstSet.add(cst);

			}
			return cstSet;
		} catch (connectionException e) {

			throw e;
		} catch (SQLException e) {
			throw new CouponSystemException("get customers failed. error accessing the database", e);

		} finally {
			pool.returnConnection(con);

		}
	}

	/**
	 * getCoupons returns a set of all coupons associated with the customer id
	 * in the customer_coupon table combined with all of the coupon information
	 * from the coupon table.
	 * 
	 * @param id
	 *            the customer identifier
	 * @return set of coupon objects representing coupons associated with the
	 *         customer id
	 * @throws connectionException
	 *             if a connection could not be retrieved
	 * @throws CouponSystemException
	 *             if a database access error occurs *
	 */
	@Override
	public Set<Coupon> getCoupons(long id) throws CouponSystemException {
		Connection con = null;

		try {
			con = pool.getConnection();
			PreparedStatement join = con
					.prepareStatement("SELECT * FROM coupon  JOIN customer_coupon ON coupon_id = id WHERE cust_id = ?");
			join.setLong(1, id);
			ResultSet result = join.executeQuery();
			Set<Coupon> set = new HashSet<>();
			while (result.next()) {
				Coupon coupon = CouponDBDAO.ResultSetToCoupon(result);
				set.add(coupon);
			}
			return set;

		} catch (connectionException e) {

			throw e;
		} catch (SQLException e) {
			throw new CouponSystemException("get customers failed. error accessing the database", e);

		} finally {
			pool.returnConnection(con);

		}
	}

	/**
	 * login checks if the customer name and password parameters match the
	 * record in the customer table in the database. returns true if such a
	 * record matching the id contains both the customer name and password.
	 * 
	 * @param id
	 *            customer identifier
	 * @param cust_name
	 *            customer name
	 * @param password
	 *            customer password
	 * 
	 * @return true if a record matching the id contains both the customer name
	 *         and password, false otherwise.
	 * 
	 * @throws connectionException
	 *             if a connection could not be retrieved
	 * @throws CouponSystemException
	 *             if a database access error occurs
	 */

	@Override
	public boolean login(long id, String cust_name, String password) throws CouponSystemException {
		Connection con = null;
		try {
			con = pool.getConnection();
			PreparedStatement pst = con.prepareStatement("SELECT cust_name, password FROM customer WHERE id=?");
			pst.setLong(1, id);
			ResultSet result = pst.executeQuery();
			while (result.getMetaData().getColumnCount() != 0 & result.next()) {
				String name = result.getString("cust_name");
				String pass = result.getString("password");
				if (pass.equals(password) && name.equals(cust_name)) {
					return true;
				}
			}
			return false;

		} catch (connectionException e) {

			throw e;
		} catch (SQLException e) {
			throw new CouponSystemException("login failed. error accessing the database", e);

		} finally {
			pool.returnConnection(con);
		}

	}

	/**
	 * getCouponsByType returns a set of all coupons associated with the
	 * customer id in the customer_coupon table and matching the type, combined
	 * with all of the coupon information from the coupon table.
	 * 
	 * @param id
	 *            the customer identifier
	 * @param type
	 *            the Coupon type category of the desired coupons
	 * @return set of coupon objects representing coupons associated with the
	 *         customer id and matching the coupon type
	 * @throws connectionException
	 *             if a connection could not be retrieved
	 * @throws CouponSystemException
	 *             if a database access error occurs *
	 */

	public Set<Coupon> getCouponsByType(long id, CouponType type) throws CouponSystemException {
		Connection con = null;

		try {
			con = pool.getConnection();
			PreparedStatement join = con.prepareStatement(
					"SELECT * FROM coupon  JOIN customer_coupon ON coupon_id = id WHERE cust_id = ? AND type=?");
			join.setLong(1, id);
			join.setString(2, type.toString());
			ResultSet result = join.executeQuery();
			Set<Coupon> set = new HashSet<>();
			while (result.next()) {
				Coupon coupon = CouponDBDAO.ResultSetToCoupon(result);
				set.add(coupon);
			}
			return set;

		} catch (connectionException e) {

			throw e;
		} catch (SQLException e) {
			throw new CouponSystemException("get customers failed. error accessing the database", e);

		} finally {
			pool.returnConnection(con);

		}
	}

	/**
	 * getCouponsByPrice returns a set of all coupons associated with the
	 * customer id in the customer_coupon table and containing a price lower
	 * than or equal to the price parameter, combined with all of the coupon
	 * information from the coupon table.
	 * 
	 * @param id
	 *            the customer identifier
	 * @param price
	 *            the maximum price of the returned coupons
	 * @return set of coupon objects representing coupons associated with the
	 *         customer id and containing a price lower than or equal to the
	 *         price parameter
	 * @throws connectionException
	 *             if a connection could not be retrieved
	 * @throws CouponSystemException
	 *             if a database access error occurs
	 */

	public Set<Coupon> getCouponsByPrice(long id, double price) throws CouponSystemException {
		Connection con = null;

		try {
			con = pool.getConnection();
			PreparedStatement join = con.prepareStatement(
					"SELECT * FROM coupon  JOIN customer_coupon ON coupon_id = id WHERE cust_id = ? AND price<=?");
			join.setLong(1, id);
			join.setDouble(2, price);
			ResultSet result = join.executeQuery();
			Set<Coupon> set = new HashSet<>();
			while (result.next()) {
				Coupon coupon = CouponDBDAO.ResultSetToCoupon(result);
				set.add(coupon);
			}
			return set;

		} catch (connectionException e) {

			throw e;
		} catch (SQLException e) {
			throw new CouponSystemException("get customers failed. error accessing the database", e);

		} finally {
			pool.returnConnection(con);

		}
	}

	/**
	 * getCustomerByName retrieves a customer record matching the customer
	 * name(should be a unique field) from the customer table in the database.
	 * if there is no customer matching the provided name the method returns a
	 * null customer object.
	 * 
	 * @param id
	 *            the identifier for the desired customer
	 * @return a customer object representing the desired customer record . if
	 *         there is no customer matching the provided name the method
	 *         returns a null customer object.
	 * 
	 * @throws connectionException
	 *             if a connection could not be retrieved
	 * @throws CouponSystemException
	 *             if a database access error occurs
	 */

	public Customer getCustomerByName(String name) throws CouponSystemException {
		Connection con = null;
		Customer cst = null;
		try {
			con = pool.getConnection();

			PreparedStatement getC = con.prepareStatement("SELECT *FROM customer WHERE cust_name=?");
			getC.setString(1, name);
			ResultSet result = getC.executeQuery();
			if (result.next()) {
				cst = new Customer();
				cst = ResultSetToCustomer(result);
			}
			return cst;

		} catch (connectionException e) {

			throw e;
		} catch (SQLException e) {
			throw new CouponSystemException("update Customer failed. error accessing the database", e);

		} finally {
			pool.returnConnection(con);

		}

	}

	/**
	 * ResultSetToCustomer converts the first line of a ResultSet to a customer
	 * object. the cursor should already point to the relevant line when
	 * provoking this method
	 * 
	 * @param result
	 *            a ResultSet of a query in the DB customer table.
	 * @return a customer object which represents the current line of the
	 *         ResultSet
	 * @throws SQLException
	 *             if the result set is closed.
	 * 
	 */
	public static Customer ResultSetToCustomer(ResultSet result) throws SQLException {

		Customer customer = new Customer();
		customer.setCust_id(result.getLong("id"));
		customer.setCust_name(result.getString("cust_name"));
		customer.setPassword(result.getString("password"));
		return customer;
	}
}
