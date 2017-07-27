package Facades;

import java.util.Date;
import java.util.Set;

import DAO.DB.CompanyDBDAO;
import DAO.DB.Company_CouponDBDAO;
import DAO.DB.CouponDBDAO;
import DAO.DB.Customer_CouponDBDAO;
import Exceptions.CouponSystemException;
import Exceptions.connectionException;
import javaBeans.Company;
import javaBeans.Coupon;
import javaBeans.CouponType;

public class CompanyFacade implements ClientCouponFacade {
	private CompanyDBDAO companyDBDAO;
	private CouponDBDAO couponDBDAO;
	private Company_CouponDBDAO company_couponDBDAO;
	private Customer_CouponDBDAO customer_couponDAODB;
	private long companyId;

	/**
	 * Constructs a CompanyFacade instance with access to relevant company
	 * methods
	 * 
	 * @param companyId
	 *            the id of the company that logged into the company facade.
	 * 
	 * 
	 * @exception connectionException
	 *                if the database is down or a connection couldn't be
	 *                established.
	 */

	public CompanyFacade(long companyId) throws connectionException {
		this.companyDBDAO = new CompanyDBDAO();
		this.couponDBDAO = new CouponDBDAO();
		this.company_couponDBDAO = new Company_CouponDBDAO();
		this.customer_couponDAODB = new Customer_CouponDBDAO();
		this.companyId = companyId;

	}

	/**
	 * createCoupon adds a specified coupon to the coupon system only if: 1.a
	 * coupon by that title doesn't already exist in the system. 2. the amount
	 * field (amount available for sale) is higher than zero.3. the Start date
	 * is before the end date. 4.The end date has not passed.
	 * 
	 * the company_coupon table is affected by this addition as well.Total
	 * number of tables affected-2: coupon, company_coupon.
	 * 
	 * @param coupon
	 *            is a coupon object representing a coupon to be added to the
	 *            coupon system
	 * @exception CouponSystemException
	 *                if a coupon by that title already exists , if the amount
	 *                is set to zero , if the dates are not correct- the end
	 *                date before start date or if the coupon end date has
	 *                already past.
	 */

	public void createCoupon(Coupon coupon) throws CouponSystemException {
		if (coupon.getAmount() == 0) {
			throw new CouponSystemException(
					"cannot add a new coupon with amount of zero, available amount must be higher than zero!");

		}
		if (coupon.getStart_date().after(coupon.getEnd_date())) {
			throw new CouponSystemException(" cannot add coupon, the end date is before the start date");

		}
		if (coupon.getEnd_date().before(new Date())) {
			throw new CouponSystemException(
					" cannot add coupon, the end date has already past- this coupon has already expired!");
		}
		if (couponDBDAO.getCouponIDByTitle(coupon.getTitle()) != -1) {
			throw new CouponSystemException(" cannot add coupon, a Coupon by this title already exsits");

		}

		try {
			couponDBDAO.createCoupon(coupon);
			// because the id field was generated automatically by the DB and
			// not by
			// company who created the coupon, we must
			// retrieve this field in order to correctly enter into the
			// customer-coupon table-getCouponIDByTitle
			Long couponId = couponDBDAO.getCouponIDByTitle(coupon.getTitle());
			if (couponId != -1) {
				company_couponDBDAO.createCompany_Coupon(companyId, couponId);
			}

		} catch (CouponSystemException e) {
			throw e;
		}

	}

	/**
	 * removeCoupon removes a specified coupon from the coupon system. Total
	 * number of tables affected-3: coupon, company_coupon, customer-coupon.
	 * 
	 * @param coupon
	 *            is a coupon object representing a coupon to be deleted from
	 *            the entire coupon system
	 * @throws CouponSystemException
	 *             if the coupon could not be deleted from all tables.
	 */
	public void removeCoupon(Coupon coupon) throws CouponSystemException {
	
		couponDBDAO.removeCoupon(coupon);
		company_couponDBDAO.removeCompany_CouponByCouponId(coupon.getCoupon_id());
		customer_couponDAODB.removeCustomer_CouponByCouponId(coupon.getCoupon_id());
	
	}

	/**
	 * updateCoupon updates the coupon matching the provided coupon id in the
	 * following fields: price and end date. the end date must not be a date in
	 * the past. the rest of the coupon details do not change.
	 * 
	 * @param coupon
	 *            is a coupon object representing a coupon with updated
	 *            fields:price and end date. these fields will be saved as the
	 *            new company details.
	 * @exception CouponSystemException
	 *                if the provided end date has already passed or if there is
	 *                a connection issue.
	 */
	public void updateCoupon(Coupon coupon) throws CouponSystemException {
		Coupon tempCoupon = couponDBDAO.getCoupon(coupon.getCoupon_id());
		tempCoupon.setPrice(coupon.getPrice());
		if (coupon.getEnd_date().before(new Date())) {
			throw new CouponSystemException("the end date provided has already past. update failed!");

		} else {
			tempCoupon.setEnd_date(coupon.getEnd_date());
			couponDBDAO.updateCoupon(tempCoupon);
		}
	}

	/**
	 * getAllCoupons returns all of the coupons associated with the current
	 * company.
	 * 
	 * @return a set of all coupons associated with the company that logged into
	 *         the system.
	 * @exception CouponSystemException
	 *                if there is a connection issue
	 */

	public Set<Coupon> getAllCoupons() throws CouponSystemException {
		return companyDBDAO.getCoupons(companyId);

	}

	/**
	 * getCouponByType returns all of the coupons of the specified type category
	 * issued by the currently logged in company.
	 * 
	 * @param couponType
	 *            the type of coupons to be returned.
	 * @return a set of all coupons that match the couponType provided and were
	 *         issued by the company that logged into the system.
	 * @exception CouponSystemException
	 *                if there is a connection issue
	 */

	public Set<Coupon> getCouponsByType(CouponType couponType) throws CouponSystemException {
		return companyDBDAO.getCouponsByType(companyId, couponType);

	}

	/**
	 * getCouponsUpToPrice returns all of the coupons with a price lower than or
	 * equal to the provided price and issued by the currently logged in
	 * company.
	 * 
	 * @param price
	 *            the maximum price of all returned coupons .
	 * @return a set of all coupons with a price lower than or equal to the
	 *         provided price and issued by the currently logged in company
	 * @exception CouponSystemException
	 *                if there is a connection issue
	 */

	public Set<Coupon> getCouponsUpToPrice(double price) throws CouponSystemException {
		return companyDBDAO.getCouponsUpToPrice(companyId, price);

	}

	/**
	 * getCouponsUntilEndDate returns all of the coupons of the currently logged
	 * in company that will expire by the specified date.
	 * 
	 * @param endDate
	 *            specifies the maximum expiration date of all returned coupons.
	 * 
	 * @return a set of coupons with an end date which is before the specified
	 *         end date and were issue by the currently logged in company.
	 * @exception CouponSystemException
	 *                if there is a connection issue
	 */

	public Set<Coupon> getCouponsUntilEndDate(Date endDate) throws CouponSystemException {
		return companyDBDAO.getCouponsUntilEndDate(companyId, endDate);

	}

	/**
	 * getCompany returns the details of the current company.
	 * 
	 * @return company object containing the company that logged into the
	 *         system.
	 * @exception CouponSystemException
	 *                if there is a connection issue
	 */

	public Company getCompany() throws CouponSystemException {
		return companyDBDAO.getCompany(companyId);
	}

}
