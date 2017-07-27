package DAO.Interfaces;

import java.util.Set;

import Exceptions.CouponSystemException;
import javaBeans.Coupon;
import javaBeans.CouponType;

public interface CouponDAO {

	void createCoupon(Coupon coupon) throws CouponSystemException;

	void removeCoupon(Coupon coupon) throws CouponSystemException;

	void updateCoupon(Coupon coupon) throws CouponSystemException;

	Coupon getCoupon(long id) throws CouponSystemException;

	Set<Coupon> getAllCoupons() throws CouponSystemException;

	Set<Coupon> getCouponByType(CouponType type) throws CouponSystemException;

}
