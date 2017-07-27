package javaBeans;

public class Company_Coupon {

	private long Comp_id;
	private long Coupon_id;

	public long getComp_id() {
		return Comp_id;
	}

	public void setComp_id(long comp_id) {
		Comp_id = comp_id;
	}

	public long getCoupon_id() {
		return Coupon_id;
	}

	public void setCoupon_id(long coupon_id) {
		Coupon_id = coupon_id;
	}

	public Company_Coupon(long comp_id, long coupon_id) {
		super();
		Comp_id = comp_id;
		Coupon_id = coupon_id;
	}

	public Company_Coupon() {
		super();
	}

	@Override
	public String toString() {
		return "Company_Coupon [Comp_id=" + Comp_id + ", Coupon_id=" + Coupon_id + "]";
	}

}
