package CouponSystem.step2;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import Exceptions.CouponSystemException;
import Facades.CompanyFacade;
import javaBeans.Company;
import javaBeans.Coupon;
import javaBeans.CouponType;

@Path("CompanyService")

public class CompanyService {

	/* webAPI for a Company role in couponSystem. */

	@Context
	HttpServletResponse response;
	@Context
	HttpServletRequest request;
	@Context
	UriInfo uriInfo;

	private Logger logger;

	public CompanyService() {
		try {
			logger = CSlogger.getInstance().getLogger();

		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	private CompanyFacade getFacadeFromSession() throws WebApplicationException, CouponSystemException {
		HttpSession session = request.getSession(false);
		if (session != null) {
			CompanyFacade companyFacade = (CompanyFacade) session.getAttribute("facade");
			if (companyFacade != null) {
				return companyFacade;
			}
		}

		throw new WebApplicationException(" please check your credentials", Response.Status.UNAUTHORIZED);

	}

	/*
	 * createCoupon adds a coupon to the couponsystem
	 * 
	 * @param coupon- the coupon to be added to the system
	 * 
	 * @returns response containing one of the following response codes: 200 if
	 * ok , 401 if the user is not properly logged in, and 400 if the action
	 * could not be performed.
	 * 
	 * if the request was successful, the response will include the resource URI
	 * while failure will include the specific error message from the system.
	 * 
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("createCoupon")
	public Response createCoupon(Coupon coupon) {

		try {
			logger.entering(getClass().getName(), "createCoupon");
			CompanyFacade companyFacade = getFacadeFromSession();
			String id = String.valueOf(companyFacade.getCouponIdByTitle(coupon));
			URI uri = uriInfo.getAbsolutePathBuilder().path(id).build();
			companyFacade.createCoupon(coupon);
			logger.log(Level.FINER, "coupon Created successfully " + id, coupon);
			return Response.created(uri).build();
		} catch (CouponSystemException e) {
			logger.log(Level.SEVERE, "Action failed: " + e.getMessage());
			return Response.status(400).entity(e.getMessage()).build();
		} catch (WebApplicationException e) {
			logger.log(Level.INFO, "UNAUTHORIZED");
			return Response.status(401).entity(e.getMessage()).build();
		}
	}

	/*
	 * removeCoupon removes a coupon from the couponsystem
	 * 
	 * @pathParam- couponId the identifier of the coupon to be removed.
	 *
	 * @returns response containing one of the following response codes: 200 if
	 * ok , 401 if the user is not properly logged in, and 400 if the action
	 * could not be performed. failure will include the specific error message
	 * from the system.
	 */

	@DELETE
	@Consumes(MediaType.TEXT_PLAIN)
	@Path("removeCoupon/{couponId}")
	public Response removeCoupon(@PathParam("couponId") long couponId) {
		logger.entering(getClass().getName(), "removeCoupon");
		Coupon coupon = new Coupon();
		coupon.setCoupon_id(couponId);
		try {
			getFacadeFromSession().removeCoupon(coupon);
			logger.log(Level.FINER, "coupon removed successfully ", coupon);
			return Response.ok(coupon.getTitle() + " was removed").status(200).build();
		} catch (WebApplicationException e) {
			logger.log(Level.INFO, "UNAUTHORIZED");
			return Response.status(401).entity(e.getMessage()).build();
		} catch (CouponSystemException e) {
			logger.log(Level.SEVERE, "Action failed: " + e.getMessage());
			return Response.status(400).entity(e.getMessage()).build();

		}

	}

	/*
	 * updateCoupon edits the information of a coupon in the system
	 * 
	 * @pathParam- couponId the identifier of the coupon to be updated.
	 *
	 * @returns response containing one of the following response codes: 200 if
	 * ok , 401 if the user is not properly logged in, and 400 if the action
	 * could not be performed.
	 * 
	 * failure will include the specific error message from the system.
	 * 
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("updateCoupon/{couponId}")
	public Response updateCoupon(Coupon coupon) {

		try {
			logger.entering(getClass().getName(), "updateCoupon");
			getFacadeFromSession().updateCoupon(coupon);
			logger.log(Level.FINER, coupon.getCoupon_id() + "coupon updated successfully ", coupon);
			return Response.ok(coupon.getTitle() + " was updated").status(200).build();
		} catch (WebApplicationException e) {
			logger.log(Level.INFO, "UNAUTHORIZED");
			return Response.status(401).entity(e.getMessage()).build();
		} catch (CouponSystemException e) {
			logger.log(Level.SEVERE, "Action failed: " + e.getMessage());
			return Response.status(400).entity(e.getMessage()).build();
		}
	}

	/*
	 * getAllCoupons retrieves all of the coupons issued by the currently logged
	 * in company.
	 * 
	 *
	 * @returns response containing one of the following response codes: 200 if
	 * ok , 401 if the user is not properly logged in, and 404 if the action
	 * could not be performed.
	 * 
	 * a successful response will include all of the company coupons while
	 * failure will include the specific error message from the system.
	 */

	@GET
	// @Produces(MediaType.APPLICATION_JSON)
	@Path("getAllCoupons")
	public Response getAllCoupons() {
		logger.entering(getClass().getName(), "getAllCoupons");
		Set<Coupon> set;
		try {
			set = getFacadeFromSession().getAllCoupons();
			GenericEntity<Set<Coupon>> genericSet = new GenericEntity<Set<Coupon>>(set) {
			};
			logger.exiting(getClass().getName(), "getAllCoupons");
			return Response.ok(genericSet).build();
		} catch (WebApplicationException e) {
			logger.log(Level.INFO, "UNAUTHORIZED");
			return Response.status(401).entity(e.getMessage()).build();
		} catch (CouponSystemException e) {
			logger.log(Level.SEVERE, "Action failed: " + e.getMessage());
			return Response.status(404).entity(e.getMessage()).build();
		}

	}

	/*
	 * getCouponsByType retrieves all of the coupons issued by the currently
	 * logged in company, matching the provided coupon type category.
	 * 
	 * @pathParam type- the desired coupon type.
	 * 
	 * @returns response containing one of the following response codes: 200 if
	 * ok , 401 if the user is not properly logged in, and 404 if the action
	 * could not be performed.
	 * 
	 * a successful response will include all of the company coupons matching
	 * the provided coupon type while failure will include the specific error
	 * message from the system.
	 */

	@GET
	// @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	@Path("getCouponsByType/{type}")

	public Response getCouponsByType(@PathParam("type") String type) {
		logger.entering(getClass().getName(), "getCouponsByType");
		try {
			CouponType couponType = CouponType.valueOf(type);
			Set<Coupon> set = getFacadeFromSession().getCouponsByType(couponType);
			GenericEntity<Set<Coupon>> genericSet = new GenericEntity<Set<Coupon>>(set) {
			};
			logger.exiting(getClass().getName(), "getCouponsByType: " + type);
			return Response.ok(genericSet).build();
		} catch (WebApplicationException e) {
			logger.log(Level.INFO, "UNAUTHORIZED");
			return Response.status(401).entity(e.getMessage()).build();
		} catch (CouponSystemException e) {
			logger.log(Level.SEVERE, "Action failed: " + e.getMessage());
			return Response.status(404).entity(e.getMessage()).build();
		}

	}

	/*
	 * getCouponsUpToPrice retrieves all of the coupons issued by the currently
	 * logged in company,with a price that is lower or equal to the provided
	 * price.
	 * 
	 * @pathParam price- the desired maximal coupon price.
	 * 
	 * @returns response containing one of the following response codes: 200 if
	 * ok , 401 if the user is not properly logged in, and 404 if the action
	 * could not be performed.
	 * 
	 * a successful response will include all of the company coupons matching
	 * the price criteria while failure will include the specific error message
	 * from the system.
	 */

	@GET
	// @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	@Path("getCouponsUpToPrice/{price}")

	public Response getCouponsUpToPrice(@PathParam("price") double price) {
		logger.entering(getClass().getName(), "getCouponsUpToPrice");
		try {
			Set<Coupon> set = getFacadeFromSession().getCouponsUpToPrice(price);
			GenericEntity<Set<Coupon>> genericSet = new GenericEntity<Set<Coupon>>(set) {
			};
			logger.exiting(getClass().getName(), "getCouponsUpToPrice: " + price, price);
			return Response.ok(genericSet).build();
		} catch (WebApplicationException e) {
			logger.log(Level.INFO, "UNAUTHORIZED");
			return Response.status(401).entity(e.getMessage()).build();
		} catch (CouponSystemException e) {
			logger.log(Level.SEVERE, "Action failed: " + e.getMessage());
			return Response.status(404).entity(e.getMessage()).build();
		}

	}

	/*
	 * getCouponsUntilEndDate retrieves all of the coupons issued by the
	 * currently logged in company,with an end date that is prior or equal to
	 * the provided date.
	 * 
	 * @pathParam endDate- the desired maximal coupon end date.
	 * 
	 * @returns response containing one of the following response codes: 200 if
	 * ok , 401 if the user is not properly logged in, 404 if the action could
	 * not be performed or 500 if there was a general error.
	 * 
	 * a successful response will include all of the company coupons matching
	 * the end date criteria while failure will include the specific error
	 * message from the system.
	 */

	@GET
	// @Produces(MediaType.APPLICATION_JSON)

	@Consumes(MediaType.TEXT_PLAIN)
	@Path("getCouponsUntilEndDate/{endDate}")

	public Response getCouponsUntilEndDate(@PathParam("endDate") String endDateStr) {
		logger.entering(getClass().getName(), "getCouponsUntilEndDate");
		DateFormat format = new SimpleDateFormat("dd.mm.yyyy", Locale.ENGLISH);
		try {
			Date endDate = format.parse(endDateStr);
			Set<Coupon> set;
			set = getFacadeFromSession().getCouponsUntilEndDate(endDate);
			GenericEntity<Set<Coupon>> genericSet = new GenericEntity<Set<Coupon>>(set) {
			};
			logger.exiting(getClass().getName(), "getCouponsUntilEndDate: " + endDate, endDate);
			return Response.ok(genericSet).build();
		} catch (WebApplicationException e) {
			logger.log(Level.INFO, "UNAUTHORIZED");
			return Response.status(401).entity(e.getMessage()).build();
		} catch (CouponSystemException e) {
			logger.log(Level.SEVERE, "Action failed: " + e.getMessage());
			return Response.status(404).entity(e.getMessage()).build();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Action failed: " + e.getMessage());
			return Response.status(500).entity(e.getMessage()).build();

		}

	}

	/*
	 * getCompany retrieves the details of the currently logged in company.
	 * 
	 * 
	 * @returns response containing one of the following response codes: 200 if
	 * ok , 401 if the user is not properly logged in ,and 404 if the action
	 * could not be performed.
	 * 
	 * a successful response will include the company accounbt details while
	 * failure will include the specific error message from the system.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getCompany")
	public Response getCompany() {
		logger.entering(getClass().getName(), "getCompany");
		try {
			Company company = getFacadeFromSession().getCompany();
			logger.exiting(getClass().getName(), "getCompany: " + company.getCompName(), company);
			return Response.ok(company).build();
		} catch (WebApplicationException e) {
			logger.log(Level.INFO, "UNAUTHORIZED");
			return Response.status(401).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
		} catch (CouponSystemException e) {
			logger.log(Level.SEVERE, "Action failed: " + e.getMessage());
			return Response.status(404).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
		}

	}

	/*
	 * method for testing- this message should show up when the resource url is
	 * used in a browser.
	 */

	@GET
	@Produces(MediaType.TEXT_PLAIN)

	public String test() {
		return "success! you got here(Company service)!!!";
	}

	/*
	 * getCoupon retrieves a specified company coupon from the couponsystem
	 * 
	 * @pathParam- id the identifier of the coupon to be returned.
	 *
	 * @returns response containing one of the following response codes: 200 if
	 * ok , 401 if the user is not properly logged in, and 404 if the action
	 * could not be performed.
	 * 
	 * a successful response will include the requested coupon while failure
	 * will include the specific error message from the system.
	 */

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	@Path("getCoupon/{id}")
	public Response getCouponById(@PathParam("id") long id) {
		logger.entering(getClass().getName(), "getCoupon");
		Coupon coupon;
		try {
			coupon = getFacadeFromSession().getCouponById(id);
			logger.log(Level.FINER, "got coupon :" + id, id);
			return Response.ok(coupon).build();
		} catch (WebApplicationException e) {
			logger.log(Level.INFO, "UNAUTHORIZED");
			return Response.status(401).entity(e.getMessage()).build();
		} catch (CouponSystemException e) {
			logger.log(Level.SEVERE, "Action failed: " + e.getMessage());
			return Response.status(404).entity(e.getMessage()).build();

		}

	}

}
