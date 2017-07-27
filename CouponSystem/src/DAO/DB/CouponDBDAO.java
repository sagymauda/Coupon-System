package DAO.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import ConnectionPool.ConnectionPool;
import DAO.Interfaces.CouponDAO;
import Exceptions.CouponSystemException;
import Exceptions.connectionException;
import javaBeans.Coupon;
import javaBeans.CouponType;

public class CouponDBDAO implements CouponDAO {

	ConnectionPool pool;

	public CouponDBDAO() throws connectionException {

		this.pool = ConnectionPool.getInstance();
	}

	/**
	 * createCoupon enters a coupon object into the coupon table in the database
	 * 
	 * @param coupon
	 *            is a coupon object
	 * @exception CouponSystemException
	 */

	@Override
	public void createCoupon(Coupon coupon) throws CouponSystemException {
		Connection con = null;
		try {
			con = pool.getConnection();
			PreparedStatement pst = con.prepareStatement(
					"INSERT INTO coupon(title,start_date,end_date,amount,type,message,price,image) VALUES(?,?,?,?,?,?,?,?)");
			pst.setString(1, coupon.getTitle());
			pst.setDate(2, new java.sql.Date(coupon.getStart_date().getTime()));
			pst.setDate(3, new java.sql.Date(coupon.getEnd_date().getTime()));
			pst.setInt(4, coupon.getAmount());
			pst.setString(5, coupon.getType().toString());
			pst.setString(6, coupon.getMessage());
			pst.setDouble(7, coupon.getPrice());
			pst.setString(8, coupon.getImage());
			pst.executeUpdate();
		} catch (connectionException e) {

			throw e;
		} catch (SQLException e) {
			throw new CouponSystemException("coupon addition failed. error accessing the database", e);
		} finally {
			pool.returnConnection(con);
		}

	}

	/**
	 * removeCoupon removes a specified record of a coupon from the coupon table
	 * in the database.
	 * 
	 * @param coupon
	 *            is a coupon object representing a record to be removed
	 * @exception CouponSystemException
	 */
	@Override
	public void removeCoupon(Coupon coupon) throws CouponSystemException {
		Connection con = null;
		try {
			con = pool.getConnection();
			PreparedStatement prep = con.prepareStatement("DELETE FROM coupon WHERE id=?");
			prep.setLong(1, coupon.getCoupon_id());
			prep.executeUpdate();
		} catch (connectionException e) {

			throw e;
		} catch (SQLException e) {
			throw new CouponSystemException("coupon removal failed. error accessing the database", e);
		} finally {
			pool.returnConnection(con);
		}

	}

	/**
	 * updateCoupon updates the values of a coupon record in the coupon table in
	 * the database. the id field cannot be updated
	 * 
	 * @param coupon
	 *            a coupon object representing an existing coupon record in the
	 *            DB.
	 * @exception CouponSystemException
	 */

	@Override
	public void updateCoupon(Coupon coupon) throws CouponSystemException {
		Connection con = null;
		try {
			con = pool.getConnection();
			PreparedStatement prep = con.prepareStatement(
					"UPDATE coupon SET title=?,start_date =?,end_date=?,amount=?,type=?,message=?,price=?,image=? WHERE id=?");

			prep.setString(1, coupon.getTitle());
			prep.setDate(2, new java.sql.Date(coupon.getStart_date().getTime()));
			prep.setDate(3, new java.sql.Date(coupon.getEnd_date().getTime()));
			prep.setInt(4, coupon.getAmount());
			prep.setString(5, coupon.getType().toString());
			prep.setString(6, coupon.getMessage());
			prep.setDouble(7, coupon.getPrice());
			prep.setString(8, coupon.getImage());
			prep.setLong(9, coupon.getCoupon_id());
			prep.executeUpdate();

		} catch (connectionException e) {

			throw e;
		} catch (SQLException e) {
			throw new CouponSystemException("coupon update failed. error accessing the database", e);
		} finally {
			pool.returnConnection(con);

		}

	}

	/**
	 * getCoupon retrieves a specified coupon record from the coupon table in
	 * the database.
	 * 
	 * @param id
	 *            the identifier for the desired coupon
	 * @return a coupon object representing the desired coupon record
	 * 
	 * @exception CouponSystemException
	 */

	@Override
	public Coupon getCoupon(long id) throws CouponSystemException {
		Connection con = null;
		Coupon coupon = new Coupon();
		try {
			con = pool.getConnection();

			PreparedStatement getC = con.prepareStatement("SELECT *FROM coupon WHERE id=?");
			getC.setLong(1, id);
			ResultSet result = getC.executeQuery();
			if (result.next()) {
				coupon = ResultSetToCoupon(result);

				return coupon;
			} else {
				throw new CouponSystemException("no record found with that coupon id");
			}
		} catch (connectionException e) {

			throw e;
		} catch (SQLException e) {
			throw new CouponSystemException("coupon retrieval failed. error accessing the database", e);
		} finally {
			pool.returnConnection(con);

		}
	}

	/**
	 * getAllCoupons returns all of the coupon records from the coupon table in
	 * the database.
	 * 
	 * @return hashSet of Coupon objects representing the records in the coupon
	 *         table
	 * 
	 * @exception CouponSystemException
	 */
	@Override
	public Set<Coupon> getAllCoupons() throws CouponSystemException {
		Connection con = null;
		Set<Coupon> set = new HashSet<>();
		try {
			con = pool.getConnection();
			Statement getAll = con.createStatement();
			ResultSet result = getAll.executeQuery("SELECT * FROM coupon");
			while (result.next()) {
				Coupon coupon = ResultSetToCoupon(result);
				set.add(coupon);
			}
			return set;
		} catch (connectionException e) {

			throw e;
		} catch (SQLException e) {
			throw new CouponSystemException("coupon retrieval failed. error accessing the database", e);
		} finally {
			pool.returnConnection(con);

		}

	}

	/**
	 * getCouponByType returns all of the coupon records of the specified type
	 * category from the coupon table in the database.
	 * 
	 * @param type
	 *            the required Coupon type (enum) to be retrieved
	 * @return hashSet of Coupon objects representing records matching the
	 *         specified type from the coupon table
	 * 
	 * @exception CouponSystemException
	 */
	@Override
	public Set<Coupon> getCouponByType(CouponType type) throws CouponSystemException {
		Connection con = null;
		Set<Coupon> set = new HashSet<>();
		try {
			con = pool.getConnection();
			PreparedStatement getBy = con.prepareStatement("SELECT * FROM coupon WHERE type=?");
			getBy.setString(1, type.toString());
			ResultSet result = getBy.executeQuery();
			while (result.next()) {
				Coupon coupon = ResultSetToCoupon(result);
				set.add(coupon);
			}
			return set;
		} catch (connectionException e) {

			throw e;
		} catch (SQLException e) {
			throw new CouponSystemException("coupon retrieval failed. error accessing the database", e);
		} finally {
			pool.returnConnection(con);

		}

	}

	/**
	 * getCouponIDByTitle returns a coupon id of a coupon matching the specified
	 * title (should be a unique field). the coupon id is generated
	 * automatically by the DB and can be retrieved using this method. if there
	 * is no coupon matching the provided title- the method returns -1.
	 * 
	 * 
	 * @param title
	 *            the title of the desired coupon
	 * @return the id of the coupon matching the title parameter. if there is no
	 *         coupon matching the provided title- the returns -1.
	 * 
	 * @exception CouponSystemException
	 */
	public long getCouponIDByTitle(String title) throws CouponSystemException {
		Connection con = null;
		try {
			con = pool.getConnection();
			PreparedStatement getC = con.prepareStatement("SELECT id FROM coupon WHERE title=?");
			getC.setString(1, title);
			ResultSet result = getC.executeQuery();
			if (result.next()) {
				return result.getLong("id");
			} else {
				return -1;
			}
		} catch (connectionException e) {

			throw e;
		} catch (SQLException e) {
			throw new CouponSystemException("coupon retrieval failed. error accessing the database", e);
		} finally {
			pool.returnConnection(con);

		}
	}

	/**
	 * getCouponUntillEndDate returns all of the coupon records containing an
	 * end date which is prior to specified end date.
	 * 
	 * 
	 * @param endDate
	 *            the maximal end date of all the returned coupons
	 * @return a set of all coupons with an end date prior to the specified end
	 *         date.
	 * 
	 * @exception CouponSystemException
	 */
	public Set<Coupon> getCouponUntillEndDate(Date endDate) throws CouponSystemException {
		Connection con = null;
		Set<Coupon> set = new HashSet<>();
		try {
			con = pool.getConnection();
			PreparedStatement getBy = con.prepareStatement("SELECT * FROM coupon WHERE end_date<?");
			getBy.setDate(1, new java.sql.Date(endDate.getTime()));
			ResultSet result = getBy.executeQuery();
			while (result.next()) {
				Coupon coupon = ResultSetToCoupon(result);
				set.add(coupon);
			}
			return set;
		} catch (connectionException e) {

			throw e;
		} catch (SQLException e) {
			throw new CouponSystemException("coupon retrieval failed. error accessing the database", e);
		} finally {
			pool.returnConnection(con);

		}

	}

	/**
	 * ResultSetToCoupon converts the first line of a ResultSet to a Coupon
	 * object. the cursor should already point to the relevant line when
	 * provoking this method
	 * 
	 * @param result
	 *            a ResultSet of a query in the DB coupon table.
	 * @return a Coupon object which represents the current line of the
	 *         ResultSet
	 * @throws SQLException
	 */
	public static Coupon ResultSetToCoupon(ResultSet result) throws SQLException {
	
		Coupon coupon = new Coupon();
		coupon.setCoupon_id(result.getLong("id"));
		coupon.setTitle(result.getString("title"));
		coupon.setStart_date(result.getDate("start_date"));
		coupon.setEnd_date(result.getDate("end_date"));
		coupon.setAmount(result.getInt("amount"));
		coupon.setType(CouponType.valueOf(result.getString("type")));
		coupon.setMessage(result.getString("message"));
		coupon.setPrice(result.getDouble("price"));
		coupon.setImage(result.getString("image"));
		return coupon;
	}

}
