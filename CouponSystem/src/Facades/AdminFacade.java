package Facades;

import java.util.Iterator;
import java.util.Set;

import DAO.DB.CompanyDBDAO;
import DAO.DB.Company_CouponDBDAO;
import DAO.DB.CouponDBDAO;
import DAO.DB.CustomerDBDAO;
import DAO.DB.Customer_CouponDBDAO;
import Exceptions.CouponSystemException;
import Exceptions.connectionException;
import javaBeans.Company;
import javaBeans.Coupon;
import javaBeans.Customer;

public class AdminFacade implements ClientCouponFacade {

	private CompanyDBDAO companyDBDAO;
	private CouponDBDAO couponDBDAO;
	private CustomerDBDAO customerDBDAO;
	private Customer_CouponDBDAO customer_couponDAODB;
	private Company_CouponDBDAO company_couponDBDAO;

	/**
	 * Constructs an AdminFacade instance with full access to all database
	 * tables.
	 * 
	 * 
	 * @exception connectionException
	 *                if the database is down or a connection couldn't be
	 *                established.
	 */
	public AdminFacade() throws connectionException {
		this.companyDBDAO = new CompanyDBDAO();
		this.couponDBDAO = new CouponDBDAO();
		this.customerDBDAO = new CustomerDBDAO();
		this.customer_couponDAODB = new Customer_CouponDBDAO();
		this.company_couponDBDAO = new Company_CouponDBDAO();

	}

	/**
	 * CreateCompany adds a specified company to the coupon system only if a
	 * company by that name doesn't already exist in the system.
	 * 
	 * @param company
	 *            is a company object representing a company to be added to the
	 *            coupon system
	 * @exception CouponSystemException
	 *                if a company by that name already exists or there is a
	 *                connection issue
	 */
	public void CreateCompany(Company company) throws CouponSystemException {

		try {
			Company companyFromDB = companyDBDAO.getCompanyByName(company.getCompName());
			if (companyFromDB == null) {
				companyDBDAO.createCompany(company);

			} else {
				throw new CouponSystemException(
						" cannot add company because a company by that name already exists in the system");
			}

		} catch (CouponSystemException e) {
			throw e;
		}
	}

	/**
	 * removeCompany removes a specified company with all of its associated
	 * coupons -both available for sale and already purchased- from the coupon
	 * system. Total number of tables affected-4: company, coupon,
	 * company-coupon, customer-coupon
	 * 
	 * @param company
	 *            is a company object representing a company to be deleted from
	 *            the entire coupon system
	 * @exception CouponSystemException
	 *                if the company could not be deleted from all tables.
	 */
	public void removeCompany(Company company) throws CouponSystemException {
		companyDBDAO.removeCompany(company);
		Set<Coupon> set = companyDBDAO.getCoupons(company.getId());
		Iterator<Coupon> it = set.iterator();
		while (it.hasNext()) {
			Coupon tempCoupon = it.next();
			couponDBDAO.removeCoupon(tempCoupon);
			customer_couponDAODB.removeCustomer_CouponByCouponId(tempCoupon.getCoupon_id());

		}
		company_couponDBDAO.removeCompany_CouponByCompId(company.getId());

	}

	/**
	 * updateCompany updates the company matching the provided company id in the
	 * following fields: password and email. The company id and company name are
	 * not updated.
	 * 
	 * @param company
	 *            is a company object representing a company with an updated
	 *            field:email or password. these fields will be saved as the new
	 *            company details.
	 * @exception CouponSystemException
	 *                if the company could not be updated.
	 */
	public void updateCompany(Company company) throws CouponSystemException {
		Company tempComp;
		tempComp = companyDBDAO.getCompany(company.getId());
		tempComp.setPassword(company.getPassword());
		tempComp.setEmail(company.getEmail());
		companyDBDAO.updateCompany(tempComp);
	}

	/**
	 * getCompany retrieves the company specified by the company Id.
	 * 
	 * 
	 * @param companyId
	 *            is the id number of the desired company
	 * 
	 * @exception CouponSystemException
	 *                if the company could not be retrieved.
	 */
	public Company getCompany(long companyId) throws CouponSystemException {

		return companyDBDAO.getCompany(companyId);

	}

	/**
	 * getAllCompanies retrieves all details about all of the companies that
	 * exist in the coupon system.
	 * 
	 * 
	 * @return a set of all companies in the system.
	 * 
	 * @exception CouponSystemException
	 *                if there are no companies in the system or if there was a
	 *                connection problem.
	 */
	public Set<Company> getAllCompanies() throws CouponSystemException {
		Set<Company> set = companyDBDAO.getAllCompanies();
		if (set == null) {
			throw new CouponSystemException("no companies have signed up yet");
		} else {
			return set;
		}

	}

	/**
	 * CreateCustomer adds a specified customer to the coupon system only if a
	 * customer by that name doesn't already exist in the system.
	 * 
	 * @param customer
	 *            is a customer object representing a customer to be added to
	 *            the coupon system
	 * @exception CouponSystemException
	 *                if a customer by that name already exists or there is a
	 *                connection issue
	 */

	public void CreateCustomer(Customer customer) throws CouponSystemException {

		try {
			Customer DBCustomer = customerDBDAO.getCustomerByName(customer.getCust_name());
			if (DBCustomer == null) {
				customerDBDAO.createCustomer(customer);
			} else {
				throw new CouponSystemException(
						" cannot add customer because a customer by that name already exists in the system");

			}
		} catch (CouponSystemException e) {
			throw e;
		}

	}

	/**
	 * removeCustomer removes a specified customer with all of its purchased
	 * coupons from the coupon system. Total number of tables affected-2:
	 * Customer-coupon,customer.
	 * 
	 * @param customer
	 *            the customer to be deleted from the system.
	 * 
	 * @exception CouponSystemException
	 *                if there is a connection issue.
	 */
	public void removeCustomer(Customer customer) throws CouponSystemException {
		customerDBDAO.removeCustomer(customer);
		Set<Coupon> set = customerDBDAO.getCoupons(customer.getCust_id());
		Iterator<Coupon> it = set.iterator();
		while (it.hasNext()) {
			customer_couponDAODB.removeCustomer_Coupon(customer.getCust_id(), it.next().getCoupon_id());
		}
	}

	/**
	 * updateCustomer updates the customer password. The customer id and name
	 * are not updated.
	 * 
	 * @param customer
	 *            is a customer object representing a customer with an updated
	 *            password field.
	 * @exception CouponSystemException
	 *                if the customer could not be updated.
	 */
	public void updateCustomer(Customer customer) throws CouponSystemException {
		Customer tempCust = customerDBDAO.getCustomer(customer.getCust_id());
		tempCust.setPassword(customer.getPassword());
		customerDBDAO.updateCustomer(tempCust);

	}

	/**
	 * getCompany retrieves the customer specified by the customer Id.
	 * 
	 * 
	 * @param customerId
	 *            is the id number of the desired customer
	 * 
	 * @exception CouponSystemException
	 *                if the customer could not be retrieved.
	 */
	public Customer getCustomer(long customerId) throws CouponSystemException {

		return customerDBDAO.getCustomer(customerId);

	}

	/**
	 * getAllCustomers retrieves all details about all of the customers that
	 * exist in the coupon system.
	 * 
	 * 
	 * @return a set of all customers in the system.
	 * 
	 * @exception CouponSystemException
	 *                if there are no customers in the system or if there was a
	 *                connection problem.
	 */
	public Set<Customer> getAllCustomers() throws CouponSystemException {
		Set<Customer> set = customerDBDAO.getAllCustomers();
		if (set == null) {
			throw new CouponSystemException("no customers have signed up yet");
		} else {
			return set;
		}
	}
}
