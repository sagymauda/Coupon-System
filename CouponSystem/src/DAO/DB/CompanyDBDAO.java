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
import DAO.Interfaces.CompanyDAO;
import Exceptions.CouponSystemException;
import Exceptions.connectionException;
import javaBeans.Company;
import javaBeans.Coupon;
import javaBeans.CouponType;

public class CompanyDBDAO implements CompanyDAO {
	private ConnectionPool pool;

	public CompanyDBDAO() throws connectionException {
		this.pool = ConnectionPool.getInstance();
	}

	/**
	 * createCompany enters a company object into the company table in the
	 * database
	 * 
	 * @param company
	 *            is a company object
	 * 
	 * @throws CouponSystemException
	 *             if there was a database issue or connection problem
	 * 
	 */
	@Override
	public void createCompany(Company company) throws CouponSystemException {
		Connection con = null;
		try {
			con = pool.getConnection();
			PreparedStatement prep = con
					.prepareStatement("INSERT INTO company(comp_name,password,email) VALUES(?,?,?)");
			prep.setString(1, company.getCompName());
			prep.setString(2, company.getPassword());
			prep.setString(3, company.getEmail());
			prep.executeUpdate();
		} catch (connectionException e) {

			throw e;

		} catch (SQLException e) {
			throw new CouponSystemException("company addition failed. error accessing the database", e);
		} finally {
			pool.returnConnection(con);

		}

	}

	/**
	 * removeCompany removes a specified record of a company from the company
	 * table in the database.
	 * 
	 * @param company
	 *            is a company object representing a record to be removed
	 * @exception CouponSystemException
	 */

	@Override
	public void removeCompany(Company company) throws CouponSystemException {
		Connection con = null;
		try {
			con = pool.getConnection();
			PreparedStatement prep = con.prepareStatement("DELETE FROM company WHERE id=?");
			prep.setLong(1, company.getId());
			prep.executeUpdate();
		} catch (connectionException e) {

			throw e;
		} catch (SQLException e) {
			throw new CouponSystemException("company removal failed.error accessing the database", e);
		} finally {
			pool.returnConnection(con);
		}

	}

	/**
	 * updateCompany updates the values of a company record in the company table
	 * in the database. the id field cannot be updated
	 * 
	 * @param company
	 *            a company object representing an existing company record in
	 *            the DB.
	 * @exception CouponSystemException
	 */

	@Override
	public void updateCompany(Company company) throws CouponSystemException {
		Connection con = null;
		try {
			con = pool.getConnection();
			PreparedStatement prep = con
					.prepareStatement("UPDATE  company SET comp_name=?,password=?,email=? WHERE id=?");

			prep.setString(1, company.getCompName());
			prep.setString(2, company.getPassword());
			prep.setString(3, company.getEmail());
			prep.setLong(4, company.getId());
			prep.executeUpdate();

		} catch (connectionException e) {

			throw e;
		} catch (SQLException e) {
			throw new CouponSystemException("company update failed.error accessing the database", e);
		} finally {
			pool.returnConnection(con);

		}

	}

	/**
	 * getCompany retrieves a specified company record from the company table in
	 * the database.
	 * 
	 * @param id
	 *            the identifier for the desired company
	 * @return a company object representing the desired company record
	 * 
	 * @exception CouponSystemException
	 */

	@Override
	public Company getCompany(long id) throws CouponSystemException {
		Connection con = null;
		Company company = new Company();
		try {
			con = pool.getConnection();

			PreparedStatement getC = con.prepareStatement("SELECT *FROM company WHERE id=?");
			getC.setLong(1, id);
			ResultSet result = getC.executeQuery();
			if (result.next()) {
				company = ResultSetToCompany(result);

				return company;
			} else {
				throw new CouponSystemException("no record found with that company id");
			}
		} catch (connectionException e) {

			throw e;
		} catch (SQLException e) {
			throw new CouponSystemException("company removal failed.error accessing the database", e);
		} finally {
			pool.returnConnection(con);

		}

	}

	/**
	 * getAllCompanies returns all of the company records from the company table
	 * in the database.
	 * 
	 * @return hashSet of company objects representing the records in the
	 *         company table
	 * 
	 * @exception CouponSystemException
	 */

	@Override
	public Set<Company> getAllCompanies() throws CouponSystemException {
		Connection con = null;
		Set<Company> compSet = new HashSet<>();
		try {
			con = pool.getConnection();
			Statement getAll = con.createStatement();
			ResultSet result = getAll.executeQuery("SELECT  * FROM company ");
			while (result.next()) {
				Company comp = ResultSetToCompany(result);
				compSet.add(comp);

			}
			return compSet;
		} catch (connectionException e) {

			throw e;
		} catch (SQLException e) {
			throw new CouponSystemException("cannot produce list.error accessing the database", e);
		} finally {
			pool.returnConnection(con);

		}

	}

	/**
	 * getCoupons returns a set of all coupons associated with the company id in
	 * the company_coupon table combined with all of the coupon information from
	 * the coupon table.
	 * 
	 * @param id
	 *            the company identifier
	 * @return set of coupon objects representing coupons associated with the
	 *         company id
	 * @throws CouponSystemException
	 * 
	 */
	@Override
	public Set<Coupon> getCoupons(long id) throws CouponSystemException {
		Connection con = null;

		try {
			con = pool.getConnection();
			PreparedStatement join = con
					.prepareStatement("SELECT * FROM coupon  JOIN company_coupon ON coupon_id = id WHERE comp_id = ?");
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
			throw new CouponSystemException("coupon retrieval failed.error accessing the database", e);
		} finally {
			pool.returnConnection(con);

		}
	}

	/**
	 * login checks if the company name and password parameters match the record
	 * in the company table in the database. returns true if such a record
	 * matching the id contains both the company name and password.
	 * 
	 * @param id
	 *            company identifier
	 * @param compName
	 *            company name
	 * @param password
	 *            company password
	 * 
	 * @return true if a record matching the id contains both the company name
	 *         and password, false otherwise.
	 * 
	 * @exception CouponSystemException
	 */
	@Override
	public boolean login(long id, String compName, String password) throws CouponSystemException {
		Connection con = null;
		try {
			con = pool.getConnection();
			PreparedStatement pst = con.prepareStatement("SELECT comp_name, password FROM company WHERE id=?");
			pst.setLong(1, id);
			ResultSet result = pst.executeQuery();
			while (result.getMetaData().getColumnCount() != 0 && result.next()) {
				String name = result.getString("comp_name");
				String pass = result.getString("password");
				if (pass.equals(password) && name.equals(compName)) {
					return true;
				}
			}
			return false;
		} catch (connectionException e) {

			throw e;
		} catch (SQLException e) {
			throw new CouponSystemException("login failed.error accessing the database", e);
		} finally {
			pool.returnConnection(con);
		}

	}

	/**
	 * getCouponsByType returns a set of all coupons matching the coupon type
	 * and associated with the company id in the company_coupon table combined
	 * with all of the coupon information from the coupon table.
	 * 
	 * @param id
	 *            the company identifier
	 * @param type
	 *            the desired coupon type category
	 * @return set of coupon objects representing coupons associated with the
	 *         company id and matching the type parameter.
	 * @throws CouponSystemException
	 * 
	 */
	@Override
	public Set<Coupon> getCouponsByType(long id, CouponType type) throws CouponSystemException {
		Connection con = null;

		try {
			con = pool.getConnection();
			PreparedStatement join = con.prepareStatement(
					"SELECT * FROM coupon  JOIN company_coupon ON coupon_id = id WHERE comp_id = ? AND type=?");
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
			throw new CouponSystemException("coupon retrieval failed.error accessing the database", e);
		} finally {
			pool.returnConnection(con);

		}
	}

	/**
	 * getCouponsUpToPrice returns a set of all coupons with a price lower than
	 * or equal to the price parameter, and associated with the company id in
	 * the company_coupon table combined with all of the coupon information from
	 * the coupon table.
	 * 
	 * @param id
	 *            the company identifier
	 * @param price
	 *            the desired maximum price of all returned coupons
	 * @return set of coupon objects representing coupons associated with the
	 *         company id and having a price up to the price parameter
	 * @throws CouponSystemException
	 * 
	 */

	public Set<Coupon> getCouponsUpToPrice(long id, double price) throws CouponSystemException {
		Connection con = null;

		try {
			con = pool.getConnection();
			PreparedStatement join = con.prepareStatement(
					"SELECT * FROM coupon  JOIN company_coupon ON coupon_id = id WHERE comp_id = ? AND price<=?");
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
			throw new CouponSystemException("coupon retrieval failed.error accessing the database", e);
		} finally {
			pool.returnConnection(con);

		}
	}

	/**
	 * getCouponsUntilEndDate returns a set of all coupons with an end date
	 * prior to or matching the date parameter, and associated with the company
	 * id in the company_coupon table combined with all of the coupon
	 * information from the coupon table.
	 * 
	 * @param id
	 *            the company identifier
	 * @param endDate
	 *            the desired maximum expiration date of the returned coupons
	 * @return set of coupon objects representing coupons associated with the
	 *         company id and containing an end date prior to or matching the
	 *         end date
	 * @throws CouponSystemException
	 * 
	 */

	public Set<Coupon> getCouponsUntilEndDate(long id, Date endDate) throws CouponSystemException {
		Connection con = null;

		try {
			con = pool.getConnection();
			PreparedStatement join = con.prepareStatement(
					"SELECT * FROM coupon  JOIN company_coupon ON coupon_id = id WHERE comp_id = ? AND end_date<=?");
			join.setLong(1, id);
			join.setDate(2, new java.sql.Date(endDate.getTime()));
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
			throw new CouponSystemException("coupon retrieval failed.error accessing the database", e);
		} finally {
			pool.returnConnection(con);

		}
	}

	/**
	 * getCompanyByName retrieves a specified company record matching the
	 * provided company name from the company table in the database. if no such
	 * company exists -the returned company will be null.
	 * 
	 * @param name
	 *            the name for the desired company
	 * @return a company object representing the desired company record
	 * 
	 * @exception CouponSystemException
	 */

	public Company getCompanyByName(String name) throws CouponSystemException {
		Connection con = null;
		Company company = null;
		try {
			con = pool.getConnection();

			PreparedStatement getC = con.prepareStatement("SELECT *FROM company WHERE comp_name=?");
			getC.setString(1, name);
			ResultSet result = getC.executeQuery();
			if (result.next()) {
				company = ResultSetToCompany(result);

			}
			return company;
		} catch (connectionException e) {

			throw e;
		} catch (SQLException e) {
			throw new CouponSystemException("company removal failed.error accessing the database", e);
		} finally {
			pool.returnConnection(con);

		}

	}

	/**
	 * ResultSetToCompany converts the first line of a ResultSet to a Company
	 * object. the cursor should already point to the relevant line when
	 * provoking this method
	 * 
	 * @param result
	 *            a ResultSet of a query in the DB company table.
	 * @return a company object which represents the current line of the
	 *         ResultSet
	 * @throws SQLException
	 */
	public static Company ResultSetToCompany(ResultSet result) throws SQLException {

		Company company = new Company();
		company.setId(result.getLong("id"));
		company.setCompName(result.getString("comp_name"));
		company.setPassword(result.getString("password"));
		company.setEmail(result.getString("email"));

		return company;
	}
}
