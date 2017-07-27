package tests;

import java.util.Iterator;
import java.util.Set;

import DAO.DB.CompanyDBDAO;
import DAO.DB.Company_CouponDBDAO;
import DAO.DB.CouponDBDAO;
import DAO.DB.CustomerDBDAO;
import DAO.DB.Customer_CouponDBDAO;
import Exceptions.CouponSystemException;
import javaBeans.Company;
import javaBeans.Company_Coupon;
import javaBeans.Coupon;
import javaBeans.Customer;
import javaBeans.Customer_Coupon;

public class PrintTables {

	/*
	 * prints out the DB tables in order to test the facades
	 * 
	 */
	public static void main(String[] args) {
		try {

			printCompanyCouponTable();
			printCompanyTable();
			printCouponTable();
			printCustomerCouponTable();
			printCustomerTable();

		} catch (CouponSystemException e) {

			e.printStackTrace();
		}
	}

	public static void printset(Set<?> set) {
		Iterator<?> it = set.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}

	}

	public static void printCompanyCouponTable() throws CouponSystemException {
		Company_CouponDBDAO comp_coup;
		System.out.println(" Company Coupon Table:");
		comp_coup = new Company_CouponDBDAO();
		Set<Company_Coupon> set = comp_coup.getAll();
		printset(set);
		System.out.println("=======================");
	}

	public static void printCouponTable() throws CouponSystemException {
		System.out.println("Coupon Table:");
		CouponDBDAO couponDBDAO = new CouponDBDAO();
		Set<Coupon> set2 = couponDBDAO.getAllCoupons();
		printset(set2);
		System.out.println("=======================");
	}

	public static void printCustomerCouponTable() throws CouponSystemException {
		Customer_CouponDBDAO customer_couponDAODB = new Customer_CouponDBDAO();
		System.out.println("Customer- Coupon Table:");
		Set<Customer_Coupon> set3 = customer_couponDAODB.getAll();
		printset(set3);
		System.out.println("=======================");
	}

	public static void printCustomerTable() throws CouponSystemException {
		CustomerDBDAO customerDBDAO = new CustomerDBDAO();
		System.out.println("Customer Table:");
		Set<Customer> set4 = customerDBDAO.getAllCustomers();
		printset(set4);
		System.out.println("=======================");
	}

	public static void printCompanyTable() throws CouponSystemException {
		System.out.println(" Company Table:");
		CompanyDBDAO company = new CompanyDBDAO();
		Set<Company> set5 = company.getAllCompanies();
		printset(set5);
		System.out.println("=======================");

	}

}
