package CouponSystem.step2;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import Exceptions.CouponSystemException;
import Facades.CustomerFacade;
import javaBeans.Coupon;
import javaBeans.CouponType;

@Path("CustomerService")

public class CustomerService {
	@Context
	private HttpServletResponse response;
	@Context
	HttpServletRequest request;

	private Logger logger;

	public CustomerService() {
		logger = CSlogger.getInstance().getLogger();
	}

	private CustomerFacade getFacadeFromSession() throws WebApplicationException {
		HttpSession session = request.getSession(false);
		if (session != null) {
			CustomerFacade customerFacade = (CustomerFacade) session.getAttribute("facade");
			if (customerFacade != null) {
				return customerFacade;
			}
		}
		throw new WebApplicationException(" please check your credentials", Response.Status.UNAUTHORIZED);
	}

	/*
	 * purchaseCoupon performs purchase of the provided coupon
	 * 
	 * @param coupon- the coupon customer wishes to buy
	 * 
	 * @returns response containing one of the following response codes: 200 if
	 * ok , 401 if the user is not properly logged in, and 400 if the action
	 * could not be performed.
	 * 
	 * 
	 * failure will include the specific error message from the system.
	 */

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("purchaseCoupon")
	public Response purchaseCoupon(Coupon coupon) {
		try {
			logger.entering(getClass().getName(), "purchaseCoupon");
			getFacadeFromSession().purchaseCoupon(coupon);
			logger.log(Level.INFO, "coupon purchased successfully ", coupon);
			return Response.ok(coupon.getTitle() + " was purchased").status(200).build();
		} catch (WebApplicationException e) {
			logger.log(Level.INFO, "UNAUTHORIZED");
			return Response.status(401).entity(e.getMessage()).build();
		} catch (CouponSystemException e) {
			logger.log(Level.SEVERE, "Action failed: " + e.getMessage());
			return Response.status(400).entity(e.getMessage()).build();

		}

	}

	/*
	 * getAllPurchasedCoupons retrieves all of the coupons purchased by the
	 * currently logged in customer.
	 * 
	 * 
	 * @returns response containing one of the following response codes: 200 if
	 * ok , 401 if the user is not properly logged in, and 404 if the action
	 * could not be performed.
	 * 
	 * a successful response will include all of the customer's coupons while
	 * failure will include the specific error message from the system.
	 */

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getAllPurchasedCoupons")
	public Response getAllPurchasedCoupons() {
		try {
			logger.entering(getClass().getName(), "getAllPurchasedCoupons");
			Set<Coupon> set = getFacadeFromSession().getAllPurchasedCoupons();
			GenericEntity<Set<Coupon>> genericSet = new GenericEntity<Set<Coupon>>(set) {
			};
			logger.exiting(getClass().getName(), "getAllPurchasedCoupons");
			return Response.ok(genericSet).build();
		} catch (WebApplicationException e) {
			logger.log(Level.INFO, "UNAUTHORIZED");
			return Response.status(401).entity(e.getMessage()).type(MediaType.TEXT_PLAIN).build();
		} catch (CouponSystemException e) {
			logger.log(Level.SEVERE, "Action failed: " + e.getMessage());
			return Response.status(404).entity(e.getMessage()).type(MediaType.TEXT_PLAIN).build();
		}

	}

	/*
	 * getAllPurchasedCouponsByType retrieves all of the coupons purchased by
	 * the currently logged in customer, matching the provided coupon type
	 * category.
	 * 
	 * @pathParam type- the desired coupon type.
	 * 
	 * @returns response containing one of the following response codes: 200 if
	 * ok , 401 if the user is not properly logged in, and 404 if the action
	 * could not be performed.
	 * 
	 * a successful response will include all of the customer's purchased
	 * coupons matching the provided coupon type while failure will include the
	 * specific error message from the system.
	 */

	@GET
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getAllPurchasedCouponsByType/{type}")

	public Response getAllPurchasedCouponsByType(@PathParam("type") String type) {
		try {
			logger.entering(getClass().getName(), "getAllPurchasedCouponsByType");
			CouponType couponType = CouponType.valueOf(type);
			Set<Coupon> set = getFacadeFromSession().getAllPurchasedCouponsByType(couponType);
			GenericEntity<Set<Coupon>> genericSet = new GenericEntity<Set<Coupon>>(set) {
			};
			logger.log(Level.INFO, " got all coupons by type:  " + type, type);
			return Response.ok(genericSet).build();
		} catch (WebApplicationException e) {
			logger.log(Level.INFO, "UNAUTHORIZED");
			return Response.status(401).entity(e.getMessage()).type(MediaType.TEXT_PLAIN).build();
		} catch (CouponSystemException e) {
			logger.log(Level.SEVERE, "Action failed: " + e.getMessage());
			return Response.status(404).entity(e.getMessage()).type(MediaType.TEXT_PLAIN).build();
		}

	}

	/*
	 * getAllPurchasedCouponsByPrice retrieves all of the coupons purchased by
	 * the currently logged in customer, with a price that is lower or equal to
	 * the provided price.
	 * 
	 * @pathParam price- the desired maximal coupon price.
	 * 
	 * @returns response containing one of the following response codes: 200 if
	 * ok , 401 if the user is not properly logged in, and 404 if the action
	 * could not be performed.
	 * 
	 * a successful response will include all of the customer's purchased
	 * coupons matching the provided coupon price while failure will include the
	 * specific error message from the system.
	 */

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	@Path("getAllPurchasedCouponsByPrice/{price}")
	public Response getAllPurchasedCouponsByPrice(@PathParam("price") double price) {
		try {
			logger.entering(getClass().getName(), "getAllPurchasedCouponsByPrice");
			Set<Coupon> set = getFacadeFromSession().getAllPurchasedCouponsByPrice(price);
			GenericEntity<Set<Coupon>> genericSet = new GenericEntity<Set<Coupon>>(set) {
			};
			logger.log(Level.INFO, " got all coupons by price:  " + price, price);
			return Response.ok(genericSet).build();
		} catch (WebApplicationException e) {
			logger.log(Level.INFO, "UNAUTHORIZED");
			return Response.status(401).entity(e.getMessage()).type(MediaType.TEXT_PLAIN).build();
		} catch (CouponSystemException e) {
			logger.log(Level.SEVERE, "Action failed: " + e.getMessage());
			return Response.status(404).entity(e.getMessage()).type(MediaType.TEXT_PLAIN).build();
		}

	}

	/*
	 * method for testing- this message should show up when the resource url is
	 * used in a browser.
	 */

	@GET
	@Produces(MediaType.TEXT_PLAIN)

	public String test() {
		return "success! you got here(customer service)!!!";

	}

	/*
	 * getAllCoupons retrieves all of the coupons in the system.
	 * 
	 * 
	 * @returns response containing one of the following response codes: 200 if
	 * ok , 401 if the user is not properly logged in, and 404 if the action
	 * could not be performed.
	 * 
	 * a successful response will include all of the coupons while failure will
	 * include the specific error message from the system.
	 */

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getAllCoupons")
	public Response getAllCoupons() {
		try {
			logger.entering(getClass().getName(), "getAllCoupons");
			Set<Coupon> set = getFacadeFromSession().getAllCoupons();
			GenericEntity<Set<Coupon>> genericSet = new GenericEntity<Set<Coupon>>(set) {
			};
			logger.exiting(getClass().getName(), "getAllCoupons");
			return Response.ok(genericSet).build();
		} catch (WebApplicationException e) {
			logger.log(Level.INFO, "UNAUTHORIZED");
			return Response.status(401).entity(e.getMessage()).type(MediaType.TEXT_PLAIN).build();
		} catch (CouponSystemException e) {
			logger.log(Level.SEVERE, "Action failed: " + e.getMessage());
			return Response.status(404).entity(e.getMessage()).type(MediaType.TEXT_PLAIN).build();
		}
	}
}
