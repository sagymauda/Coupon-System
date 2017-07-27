package tests;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import Exceptions.CouponSystemException;
import Facades.AdminFacade;
import Facades.CompanyFacade;
import Facades.CustomerFacade;
import couponSystem.ClientType;
import couponSystem.CouponSystem;
import javaBeans.Company;
import javaBeans.Coupon;
import javaBeans.CouponType;
import javaBeans.Customer;

public class CouponSystemTest {
	public static void main(String[] args) {
		Calendar endDate = Calendar.getInstance();
		endDate.set(2017, 9, 1);
		try {

			// start system
			CouponSystem couponSystem = CouponSystem.getInstance();

			// login as admin and perform actions
			AdminFacade adminFacade = (AdminFacade) couponSystem.login(0, "admin", "1234", ClientType.ADMIN);

			// admin: company related actions
			adminFacade.CreateCompany(new Company("coca cola", "wewin2298", "coke@gmail.com"));
			Company company = adminFacade.getCompany(103);
			adminFacade.removeCompany(company);
			Set<Company> allCompanies = adminFacade.getAllCompanies();
			adminFacade.updateCompany(allCompanies.iterator().next());

			// admin: customer related actions

			adminFacade.CreateCustomer(new Customer("Chaim cohen", "45tr7728"));
			Customer customer = adminFacade.getCustomer(2000);
			customer.setPassword("lilo6588");
			adminFacade.updateCustomer(customer);
			adminFacade.removeCustomer(customer);
			Set<Customer> allCustomers = adminFacade.getAllCustomers();

			// login as company and perform actions
			CompanyFacade companyFacade = (CompanyFacade) couponSystem.login(112, "Name26", "password2626",
					ClientType.COMPANY);
			companyFacade.createCoupon(new Coupon("spa say", endDate.getTime(), new Date(), 100, CouponType.HEALTH,
					"spa day including massage", 1700.0, "pic.jpg"));
			Company currentCompany = companyFacade.getCompany();
			Set<Coupon> companyCoupons = companyFacade.getAllCoupons();
			Set<Coupon> companyCouponsByDate = companyFacade.getCouponsUntilEndDate(endDate.getTime());
			companyFacade.removeCoupon(companyCoupons.iterator().next());

			// login as customer and perform actions
			CustomerFacade customerFacade = (CustomerFacade) couponSystem.login(2005, "CustName7", "password114",
					ClientType.CUSTOMER);
			customerFacade.purchaseCoupon(companyCoupons.iterator().next());
			Set<Coupon> customerCoupons = customerFacade.getAllPurchasedCoupons();
			Set<Coupon> customerCouponsByPrice = customerFacade.getAllPurchasedCouponsByPrice(1000);
			Set<Coupon> customerCouponsByType = customerFacade.getAllPurchasedCouponsByType(CouponType.CAMPING);

			// shut system down

			couponSystem.shutDown();

		} catch (CouponSystemException e) {

			e.printStackTrace();

		}
	}
}
