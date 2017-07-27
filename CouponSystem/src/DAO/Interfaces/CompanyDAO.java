package DAO.Interfaces;

import java.util.Set;

import Exceptions.CouponSystemException;
import javaBeans.Company;
import javaBeans.Coupon;
import javaBeans.CouponType;

public interface CompanyDAO {
	void createCompany(Company company) throws CouponSystemException;

	void removeCompany(Company company) throws CouponSystemException;

	void updateCompany(Company company) throws CouponSystemException;

	Company getCompany(long id) throws CouponSystemException;

	Set<Company> getAllCompanies() throws CouponSystemException;

	Set<Coupon> getCoupons(long id) throws CouponSystemException;

	boolean login(long id, String compName, String password) throws CouponSystemException;

	Set<Coupon> getCouponsByType(long id, CouponType type) throws CouponSystemException;

}
