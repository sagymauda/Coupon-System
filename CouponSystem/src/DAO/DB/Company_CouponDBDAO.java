package DAO.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import ConnectionPool.ConnectionPool;
import DAO.Interfaces.Company_CouponDAO;
import Exceptions.CouponSystemException;
import Exceptions.connectionException;
import javaBeans.Company_Coupon;

public class Company_CouponDBDAO implements Company_CouponDAO {

	private ConnectionPool pool;

	/**
	 * Constructs an Company_CouponDBDAO instance and loads the connection pool.
	 * 
	 * @exception connectionException
	 *                if the database is down or a connection couldn't be
	 *                established.
	 */

	public Company_CouponDBDAO() throws connectionException {

		this.pool = ConnectionPool.getInstance();

	}

	/**
	 * createCompany_Coupon enters a Company- coupon record into the Company-
	 * coupon table in the database
	 * 
	 * @param compID
	 *            identifier of the company that issued the coupon
	 * @param CouponID
	 *            the identifier of the coupon.
	 * @throws connectionException
	 *             if a connection could not be retrieved
	 * @throws CouponSystemException
	 *             if a database access error occurs
	 * 
	 */
	@Override
	public void createCompany_Coupon(long compID, long CouponID) throws CouponSystemException {

		Connection con = null;
		try {
			con = pool.getConnection();
			PreparedStatement prep = con.prepareStatement("INSERT INTO company_coupon VALUES(?,?)");

			prep.setLong(1, compID);
			prep.setLong(2, CouponID);

			prep.executeUpdate();
		} catch (connectionException e) {

			throw e;
		} catch (SQLException e) {
			throw new CouponSystemException("company_coupon was not added. error accessing the database", e);
		} finally {
			pool.returnConnection(con);

		}

	}

	/**
	 * getAll retrieves all of the Company_Coupon-coupon records from the
	 * Company_Coupon table in the database.
	 * 
	 * @return a set of all the Company_Coupon records in the database.
	 * @throws connectionException
	 *             if a connection could not be retrieved
	 * @throws CouponSystemException
	 *             if a database access error occurs
	 * 
	 */

	@Override
	public Set<Company_Coupon> getAll() throws CouponSystemException {
		Connection con = null;
		Set<Company_Coupon> compCoupSet = new HashSet<>();
		try {
			con = pool.getConnection();
			Statement getAll = con.createStatement();
			ResultSet result = getAll.executeQuery("SELECT * FROM company_coupon ");
			while (result.next()) {
				compCoupSet.add(ResultSetToCompany_Coupon(result));

			}
			return compCoupSet;
		} catch (connectionException e) {

			throw e;
		} catch (SQLException e) {
			throw new CouponSystemException("error accessing the database", e);

		} finally {
			pool.returnConnection(con);

		}

	}

	/**
	 * removeCompany_CouponByCompId removes all of the records in the
	 * Company_Coupon table that match the provided company id.
	 * 
	 * @param companyId
	 *            the identifier of the company who's issued coupons should be
	 *            deleted from the table.
	 * @throws connectionException
	 *             if a connection could not be retrieved
	 * @throws CouponSystemException
	 *             if a database access error occurs
	 * 
	 */

	@Override
	public void removeCompany_CouponByCompId(Long companyId) throws CouponSystemException {
		Connection con = null;
		try {
			con = pool.getConnection();
			PreparedStatement prep = con.prepareStatement("DELETE FROM company_coupon WHERE comp_id=?");
			prep.setLong(1, companyId);
			prep.executeUpdate();
		} catch (connectionException e) {

			throw e;
		} catch (SQLException e) {
			throw new CouponSystemException("removal failed. error accessing the database", e);

		} finally {
			pool.returnConnection(con);
		}
	}

	/**
	 * removeCompany_CouponByCouponId removes all of the records in the
	 * Company_Coupon table that match the provided Coupon id.
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
	public void removeCompany_CouponByCouponId(Long couponId) throws CouponSystemException {
		Connection con = null;
		try {
			con = pool.getConnection();
			PreparedStatement prep = con.prepareStatement("DELETE FROM company_coupon WHERE coupon_id=?");
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

	/**
	 * ResultSetToCompany_Coupon( converts the first line of a ResultSet to a
	 * Company_Coupon object. the cursor should already point to the relevant
	 * line when provoking this method
	 * 
	 * @param result
	 *            a ResultSet of a query in the DB customer table.
	 * @return a Company-coupon object representing a Company and a coupon
	 *         issued by it. which represents the current line of the ResultSet
	 * @throws SQLException
	 *             if the result set is closed.
	 */
	public static Company_Coupon ResultSetToCompany_Coupon(ResultSet result) throws SQLException {

		Company_Coupon company_coupon = new Company_Coupon();
		company_coupon.setComp_id(result.getLong("comp_id"));
		company_coupon.setCoupon_id(result.getLong("coupon_id"));

		return company_coupon;
	}

	// @Override
	// public Company_Coupon getCompany_Coupon(long Comp_id) throws
	// CouponSystemException {
	// Connection con = null;
	// Company_Coupon company_coupon = new Company_Coupon();
	// try {
	// con = pool.getConnection();
	//
	// PreparedStatement getC = con.prepareStatement("SELECT *FROM
	// Company_Coupon WHERE comp_id=?");
	// getC.setLong(1, Comp_id);
	// ResultSet result = getC.executeQuery();
	// if (result.next()) {
	// company_coupon = ResultSetToCompany_Coupon(result);
	// return company_coupon;
	// } else {
	// throw new DAOException("no record found with that company id");
	// }
	// } catch (connectionException | SQLException e) {
	// throw new DAOException("cannot get Company_Coupon", e);
	//
	// } finally {
	// pool.returnConnection(con);
	//
	// }
	// }

}
