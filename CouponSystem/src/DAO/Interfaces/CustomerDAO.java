package DAO.Interfaces;

import java.util.Set;

import Exceptions.CouponSystemException;
import javaBeans.Coupon;
import javaBeans.Customer;

public interface CustomerDAO {
	void createCustomer(Customer cst) throws CouponSystemException;

	void removeCustomer(Customer cst) throws CouponSystemException;

	void updateCustomer(Customer cst) throws CouponSystemException;

	Customer getCustomer(long id) throws CouponSystemException;

	Set<Customer> getAllCustomers() throws CouponSystemException;

	Set<Coupon> getCoupons(long id) throws CouponSystemException;

	boolean login(long id, String cust_name, String password) throws CouponSystemException;

}
