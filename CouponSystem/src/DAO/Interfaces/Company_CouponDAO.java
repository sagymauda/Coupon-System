package DAO.Interfaces;

import java.util.Set;

import Exceptions.CouponSystemException;
import javaBeans.Company_Coupon;

public interface Company_CouponDAO {

	void createCompany_Coupon(long compID, long CouponID) throws CouponSystemException;

	Set<Company_Coupon> getAll() throws CouponSystemException;

	void removeCompany_CouponByCompId(Long companyId) throws CouponSystemException;

	void removeCompany_CouponByCouponId(Long couponId) throws CouponSystemException;
}
