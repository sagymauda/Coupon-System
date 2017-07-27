package Facades;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.Set;

import DAO.DB.CouponDBDAO;
import DAO.DB.CustomerDBDAO;
import DAO.DB.Customer_CouponDBDAO;
import Exceptions.CouponSystemException;
import Exceptions.connectionException;
import javaBeans.Coupon;
import javaBeans.CouponType;

public class CustomerFacade implements ClientCouponFacade {

	private CouponDBDAO couponDBDAO;
	private CustomerDBDAO customerDBDAO;
	private Customer_CouponDBDAO customer_couponDAODB;

	private long customerId;

	/**
	 * Constructs an CustomerFacade instance with access to relevant Customer
	 * methods
	 * 
	 * @exception connectionException
	 *                if the database is down or a connection couldn't be
	 *                established.
	 */
	public CustomerFacade(long customerId) throws connectionException {
		this.couponDBDAO = new CouponDBDAO();
		this.customerDBDAO = new CustomerDBDAO();
		this.customer_couponDAODB = new Customer_CouponDBDAO();
		this.customerId = customerId;
	}

	/**
	 * purchaseCoupon allows the customer to purchase a coupon by adding the
	 * specified coupon to the customer coupon table. this method also updates
	 * the coupon amount to reflect this purchase. a coupon can only be
	 * purchased if the coupon has not expired or been sold out. additionally, a
	 * customer can only purchase a coupon if no such coupon has been previously
	 * purchased by this customer.
	 * 
	 * Total number of tables affected-2: coupon, customer-coupon.
	 * 
	 * @param coupon
	 *            is a coupon object representing a coupon to be added to the
	 *            coupon system
	 * @exception CouponSystemException
	 *                if a coupon matching the provided coupon if has already
	 *                been purchased. if the coupon has expired if the coupon is
	 *                sold out- amount is zero.
	 */
	public void purchaseCoupon(Coupon coupon) throws CouponSystemException {
		Coupon dbCoupon = couponDBDAO.getCoupon(coupon.getCoupon_id());
		if (dbCoupon.getAmount() == 0) {
			throw new CouponSystemException("coupon sold out. purchase failed");

		} else if (dbCoupon.getEnd_date().before(new Date())) {
			throw new CouponSystemException("coupon already expired. purchase failed");

		}
		try {
			customer_couponDAODB.createCustomer_Coupon(customerId, coupon.getCoupon_id());
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new CouponSystemException("coupon already purchased in the past. purchase failed");
		}

		dbCoupon.setAmount(dbCoupon.getAmount() - 1);
		couponDBDAO.updateCoupon(dbCoupon);

	}

	/**
	 * getAllPurchasedCoupons retrieves all of the coupons purchased by the
	 * currently logged in customer.
	 * 
	 * @return a set of Coupon objects purchased by the currently logged in
	 *         customer.
	 * @exception CouponSystemException
	 * 
	 */

	public Set<Coupon> getAllPurchasedCoupons() throws CouponSystemException {
		return customerDBDAO.getCoupons(customerId);
	}

	/**
	 * getAllPurchasedCouponsByType retrieves all of the coupons purchased by
	 * the currently logged in customer that match the provided type category.
	 * 
	 * @param couponType
	 *            the type category(Enum value) of the coupons to be returned.
	 *
	 * @return a set of Coupon objects purchased by the currently logged in
	 *         customer that match the provided type category.
	 * @throws CouponSystemException
	 *             if there is a connection issue
	 */

	public Set<Coupon> getAllPurchasedCouponsByType(CouponType couponType) throws CouponSystemException {

		return customerDBDAO.getCouponsByType(customerId, couponType);
	}

	/**
	 * getAllPurchasedCouponsByPrice retrieves all of the coupons purchased by
	 * the currently logged in customer with a price lower than or equal to the
	 * provided price.
	 * 
	 * @param price
	 *            the maximum price of all returned coupons .
	 * @return a set of all coupons with a price lower than or equal to the
	 *         provided price, purchased by the currently logged in customer
	 * @throws CouponSystemException
	 *             if there is a connection issue
	 */

	public Set<Coupon> getAllPurchasedCouponsByPrice(double price) throws CouponSystemException {
		return customerDBDAO.getCouponsByPrice(customerId, price);
	}
}
