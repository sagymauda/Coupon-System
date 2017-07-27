package javaBeans;

public class Customer_Coupon {

	private long cust_id;
	private long Coupon_id;

	/**
	 * @return the cust_id
	 */
	public long getCust_id() {
		return cust_id;
	}

	/**
	 * @param cust_id
	 *            the cust_id to set
	 */
	public void setCust_id(long cust_id) {
		this.cust_id = cust_id;
	}

	/**
	 * @return the coupon_id
	 */
	public long getCoupon_id() {
		return Coupon_id;
	}

	/**
	 * @param coupon_id
	 *            the coupon_id to set
	 */
	public void setCoupon_id(long coupon_id) {
		Coupon_id = coupon_id;
	}

	public Customer_Coupon() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Customer_Coupon [cust_id=" + cust_id + ", Coupon_id=" + Coupon_id + "]";
	}

	public Customer_Coupon(long cust_id, long coupon_id) {
		super();
		this.cust_id = cust_id;
		Coupon_id = coupon_id;
	}

}
