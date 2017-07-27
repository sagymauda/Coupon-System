package DAO.Interfaces;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Set;

import Exceptions.CouponSystemException;
import javaBeans.Customer_Coupon;

public interface Customer_CouponDAO {
	void createCustomer_Coupon(long custID, long CouponID)
			throws CouponSystemException, SQLIntegrityConstraintViolationException;

	Set<Customer_Coupon> getAll() throws CouponSystemException;

	void removeCustomer_Coupon(long customerId, long couponId) throws CouponSystemException;

	void removeCustomer_CouponByCustomerId(Long customerId) throws CouponSystemException;

	void removeCustomer_CouponByCouponId(Long couponId) throws CouponSystemException;

}
