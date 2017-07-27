package DAO.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import ConnectionPool.ConnectionPool;
import DAO.Interfaces.Customer_CouponDAO;
import Exceptions.CouponSystemException;
import Exceptions.connectionException;
import javaBeans.Customer_Coupon;

public class Customer_CouponDBDAO implements Customer_CouponDAO {

	private ConnectionPool pool;

	/**
	 * Constructs an Customer_CouponDAODB instance and loads the connection
	 * pool.
	 * 
	 * @exception connectionException
	 *                if the database is down or a connection couldn't be
	 *                established.
	 */

	public Customer_CouponDBDAO() throws connectionException {

		this.pool = ConnectionPool.getInstance();

	}

	/**
	 * createCustomer_Coupon enters a Customer- coupon record into the Customer-
	 * coupon table in the database
	 * 
	 * @param custID
	 *            the identifier for the customer who purchased the coupon
	 * @param CouponId
	 *            the identifier for the coupon that was purchased
	 * 
	 * @throws connectionException
	 *             if a connection could not be retrieved
	 * @throws CouponSystemException
	 *             if a database access error occurs
	 * 
	 */

	@Override
	public void createCustomer_Coupon(long custID, long CouponId)
			throws CouponSystemException, SQLIntegrityConstraintViolationException {
		Connection con = null;
		try {
			con = pool.getConnection();
			PreparedStatement prep = con.prepareStatement("INSERT INTO customer_coupon VALUES(?,?)");

			prep.setLong(1, custID);
			prep.setLong(2, CouponId);

			prep.executeUpdate();
		} catch (connectionException | SQLIntegrityConstraintViolationException e) {

			throw e;
		} catch (SQLException e) {
			throw new CouponSystemException("error accessing the database", e);
		} finally {
			pool.returnConnection(con);

		}

	}

	/**
	 * getAll retrieves all of the customer-coupon records from the
	 * customer_coupon table in the database.
	 * 
	 * @return a set of all the customer-coupon records in the database.
	 * @throws connectionException
	 *             if a connection could not be retrieved
	 * @throws CouponSystemException
	 *             if a database access error occurs
	 * 
	 */
	@Override
	public Set<Customer_Coupon> getAll() throws CouponSystemException {
		Connection con = null;
		Set<Customer_Coupon> custCoupSet = new HashSet<>();
		try {
			con = pool.getConnection();
			Statement getAll = con.createStatement();
			ResultSet result = getAll.executeQuery("SELECT  * FROM customer_coupon ");
			while (result.next()) {
				custCoupSet.add(ResultSetToCustomer_Coupon(result));

			}
			return custCoupSet;
		} catch (connectionException e) {

			throw e;
		} catch (SQLException e) {
			throw new CouponSystemException("error accessing the database", e);

		} finally {
			pool.returnConnection(con);

		}
	}

	/**
	 * removeCustomer_CouponByCustomerId removes a record in the customer coupon
	 * table that matches the provided customer id and coupon id.
	 * 
	 * @param customerId
	 *            the identifier of the customer
	 * @param couponId
	 *            the identifier of the coupon
	 * @throws connectionException
	 *             if a connection could not be retrieved
	 * @throws CouponSystemException
	 *             if a database access error occurs
	 * 
	 */
	@Override
	public void removeCustomer_Coupon(long customerId, long couponId) throws CouponSystemException {
		Connection con = null;
		try {
			con = pool.getConnection();
			PreparedStatement prep = con
					.prepareStatement("DELETE FROM customer_coupon WHERE cust_id=? AND coupon_id=?");
			prep.setLong(1, customerId);
			prep.setLong(2, couponId);
			prep.executeUpdate();
		} catch (connectionException e) {

			throw e;
		} catch (SQLException e) {
			throw new CouponSystemException("error accessing the database", e);

		} finally {
			pool.returnConnection(con);
		}
	}

	public static Customer_Coupon ResultSetToCustomer_Coupon(ResultSet result) throws SQLException {

		Customer_Coupon customer_coupon = new Customer_Coupon();
		customer_coupon.setCust_id(result.getLong("cust_id"));
		customer_coupon.setCoupon_id(result.getLong("coupon_id"));

		return customer_coupon;
	}

	//
	// /**
	// * getCustomer_Coupon retrieves a Customer- coupon object from the
	// * Customer- coupon table in the database
	// *
	// * @param cust_id
	// * is the idetifier of the customer
	// * @throws connectionException
	// * if a connection could not be retrieved
	// * @throws CouponSystemException
	// * if a database access error occurs
	// *
	// */
	//
	// @Override
	// public Set<Customer_Coupon> getCustomer_Coupon(long cust_id) throws
	// CouponSystemException {
	// Connection con = null;
	// Customer_Coupon customer_coupon = new Customer_Coupon();
	// try {
	// con = pool.getConnection();
	//
	// PreparedStatement getC = con.prepareStatement("SELECT *FROM
	// customer_coupon WHERE cust_id=?");
	// getC.setLong(1, cust_id);
	// ResultSet result = getC.executeQuery();
	// if (result.next()) {
	// customer_coupon = ResultSetToCustomer_Coupon(result);
	// return customer_coupon;
	// } else {
	// throw new CouponSystemException("no record found with that customer id");
	// }
	// } catch (connectionException e) {
	//
	// throw e;
	// } catch (SQLException e) {
	// throw new CouponSystemException("error accessing the database", e);
	//
	// } finally {
	// pool.returnConnection(con);
	//
	// }
	// }

	/**
	 * removeCustomer_CouponByCustomerId removes all of the records in the
	 * customer coupon table that match the provided customer id.
	 * 
	 * @param customerId
	 *            the identifier of the customer who's purchases should be
	 *            deleted from the table.
	 * @throws connectionException
	 *             if a connection could not be retrieved
	 * @throws CouponSystemException
	 *             if a database access error occurs
	 * 
	 */
	@Override
	public void removeCustomer_CouponByCustomerId(Long customerId) throws CouponSystemException {
		Connection con = null;
		try {
			con = pool.getConnection();
			PreparedStatement prep = con.prepareStatement("DELETE FROM customer_coupon WHERE cust_id=?");
			prep.setLong(1, customerId);
			prep.executeUpdate();
		} catch (connectionException e) {

			throw e;
		} catch (SQLException e) {
			throw new CouponSystemException("error accessing the database", e);

		} finally {
			pool.returnConnection(con);
		}
	}

	/**
	 * removeCustomer_CouponByCouponId removes all of the records in the
	 * customer coupon table that match the provided Coupon id.
	 * 
	 * @param couponId
	 *            the identifier of the coupon.all records containing this
	 *            identifier will be removed
	 * @throws connectionException
	 *             if a connection could not be retrieved
	 * @throws CouponSystemException
	 *             if a database access error occurs
	 * 
	 */
	@Override
	public void removeCustomer_CouponByCouponId(Long couponId) throws CouponSystemException {
		Connection con = null;
		try {
			con = pool.getConnection();
			PreparedStatement prep = con.prepareStatement("DELETE FROM customer_coupon WHERE coupon_id=?");
			prep.setLong(1, couponId);
			prep.executeUpdate();
		} catch (connectionException e) {

			throw e;
		} catch (SQLException e) {
			throw new CouponSystemException(" removal failed. error accessing the database", e);

		} finally {
			pool.returnConnection(con);
		}
	}

}
