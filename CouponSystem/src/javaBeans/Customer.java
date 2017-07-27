package javaBeans;

public class Customer {

	private long cust_id;
	private String cust_name;
	private String password;

	public Customer(long cust_id, String cust_name, String password) {
		super();
		this.cust_id = cust_id;
		this.cust_name = cust_name;
		this.password = password;
	}

	public Customer(String cust_name, String password) {
		this(0, cust_name, password);
	}

	public Customer() {
		this(0, "noname", "none");

	}

	public Customer(String name) {
		this(0, name, "none");

	}

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
	 * @return the cust_name
	 */
	public String getCust_name() {
		return cust_name;
	}

	/**
	 * @param cust_name
	 *            the cust_name to set
	 */
	public void setCust_name(String cust_name) {
		this.cust_name = cust_name;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Customer [cust_id=" + cust_id + ", cust_name=" + cust_name + ", password=" + password + "]";
	}

}
