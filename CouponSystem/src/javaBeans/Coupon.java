package javaBeans;

import java.util.Date;

public class Coupon {
	private long coupon_id;
	private String title;
	private Date start_date;
	private Date end_date;
	private int amount;
	private CouponType type;
	private String message;
	private double price;
	private String image;

	public Coupon(long coupon_id) {
		this(coupon_id, "title", new Date(0), new Date(0), 0, CouponType.NONE, "msg", 0, "none");
		;

	}

	public Coupon(long coupon_id, String title, Date end_date, Date start_date, int amount, CouponType type,
			String message, double price, String image) {
		super();
		this.coupon_id = coupon_id;
		this.title = title;
		this.start_date = start_date;
		this.end_date = end_date;
		this.amount = amount;
		this.type = type;
		this.message = message;
		this.price = price;
		this.image = image;
	}

	public Coupon(String title, Date end_date, Date start_date, int amount, CouponType type, String message,
			double price, String image) {
		this(0, title, end_date, start_date, amount, type, message, price, image);

	}

	public Coupon() {
		this(0, "title", new Date(0), new Date(0), 0, CouponType.NONE, "msg", 0, "none");

	}

	public Coupon(String title, int amount) {
		this(0, title, new Date(0), new Date(0), amount, CouponType.NONE, "msg", 0, "none");

	}

	/**
	 * @return the coupon_id
	 */
	public long getCoupon_id() {
		return coupon_id;
	}

	/**
	 * @param coupon_id
	 *            the coupon_id to set
	 */
	public void setCoupon_id(long coupon_id) {
		this.coupon_id = coupon_id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the start_date
	 */
	public Date getStart_date() {
		return start_date;
	}

	/**
	 * @param start_date
	 *            the start_date to set
	 */
	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	/**
	 * @return the end_date
	 */
	public Date getEnd_date() {
		return end_date;
	}

	/**
	 * @param end_date
	 *            the end_date to set
	 */
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}

	/**
	 * @return the type
	 */
	public CouponType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(CouponType type) {
		this.type = type;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * @return the image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @param image
	 *            the image to set
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Coupon [coupon_id=" + coupon_id + ", title=" + title + ", start_date=" + start_date + ", end_date="
				+ end_date + ", amount=" + amount + ", type=" + type + ", message=" + message + ", price=" + price
				+ ", image=" + image + "]";
	}

}
