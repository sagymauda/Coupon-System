package CouponSystem.step2;

import java.net.URI;
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
import Facades.AdminFacade;
import javaBeans.Company;
import javaBeans.Customer;

@Path("adminService")
public class AdminService {

	/* webAPI for Administrator role in couponSystem. */

	@Context
	private HttpServletResponse response;
	@Context
	HttpServletRequest request;
	@Context
	UriInfo uriInfo;

	private Logger logger;

	public AdminService() {
		try {
			logger = CSlogger.getInstance().getLogger();

		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	private AdminFacade getFacadeFromSession() throws WebApplicationException, CouponSystemException {
		HttpSession session = request.getSession(false);
		if (session != null) {
			AdminFacade adminFacade = (AdminFacade) session.getAttribute("facade");
			if (adminFacade != null) {
				return adminFacade;

			}
		}

		throw new WebApplicationException(" please check your credentials", Response.Status.UNAUTHORIZED);

	}

	/*
	 * CreateCompany adds a company to the couponsystem
	 * 
	 * @param company- the company to be added to the system
	 * 
	 * @returns response containing one of the following response codes: 200 if
	 * ok , 401 if the user is not properly logged in, and 400 if the action
	 * could not be performed. if the request was successful, the response will
	 * include the resource URI.
	 */

	@POST
	@Path("addCompany")
	@Consumes(MediaType.APPLICATION_JSON)

	public Response CreateCompany(Company company) {
		try {
			logger.entering(getClass().getName(), "addCompany");
			AdminFacade adminFacade = getFacadeFromSession();
			adminFacade.CreateCompany(company);
			String id = String.valueOf(adminFacade.getCompanyIDByName(company));
			URI uri = uriInfo.getAbsolutePathBuilder().path(id).build();
			logger.log(Level.FINER, "company Created successfully ", company);
			return Response.created(uri).build();
		} catch (WebApplicationException e) {
			logger.log(Level.INFO, "UNAUTHORIZED");
			return Response.status(401).entity(e.getMessage()).build();
		} catch (CouponSystemException e) {
			logger.log(Level.SEVERE, "Action failed: " + e.getMessage());
			return Response.status(400).entity(e.getMessage()).build();

		}

	}

	/*
	 * CreateCustomer adds a customer to the couponsystem
	 * 
	 * @param customer- the customer to be added to the system
	 * 
	 * @returns response containing one of the following response codes: 200 if
	 * ok , 401 if the user is not properly logged in, and 400 if the action
	 * could not be performed. if the request was successful, the response will
	 * include the resource URI.
	 */

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("addCustomer")

	public Response CreateCustomer(Customer customer) {
		try {
			logger.entering(getClass().getName(), "addCustomer");
			AdminFacade facade = getFacadeFromSession();
			facade.CreateCustomer(customer);
			String id = String.valueOf(facade.getCustomerIDByName(customer));
			URI uri = uriInfo.getAbsolutePathBuilder().path(id).build();
			logger.log(Level.FINER, "customer Created successfully ", customer);
			return Response.created(uri).build();
		} catch (WebApplicationException e) {
			logger.log(Level.INFO, "UNAUTHORIZED");
			return Response.status(401).entity(e.getMessage()).build();
		} catch (CouponSystemException e) {
			logger.log(Level.SEVERE, "Action failed: " + e.getMessage());
			return Response.status(400).entity(e.getMessage()).build();
		}

	}

	/*
	 * removeCompany removes a company from the couponsystem
	 * 
	 * @pathParam- companyId the identifier of the company to be removed.
	 *
	 * @returns response containing one of the following response codes: 200 if
	 * ok , 401 if the user is not properly logged in, and 400 if the action
	 * could not be performed.
	 */

	@DELETE
	@Path("deleteCompany/{companyId}")
	@Consumes(MediaType.TEXT_PLAIN)
	public Response removeCompany(@PathParam("companyId") int companyId) {
		logger.entering(getClass().getName(), "deleteCompany");
		Company company = new Company();
		company.setId(companyId);
		try {
			getFacadeFromSession().removeCompany(company);
			logger.log(Level.FINER, "company removed successfully ", company);
			return Response.ok(company.getCompName() + " was removed").status(200).build();
		} catch (WebApplicationException e) {
			logger.log(Level.INFO, "UNAUTHORIZED");
			return Response.status(401).entity(e.getMessage()).build();
		} catch (CouponSystemException e) {
			logger.log(Level.SEVERE, "Action failed: " + e.getMessage());
			return Response.status(400).entity(e.getMessage()).build();

		}

	}

	/*
	 * deleteCustomer removes a customer from the couponsystem
	 * 
	 * @pathParam- customerId the identifier of the customer to be removed.
	 *
	 * @returns response containing one of the following response codes: 200 if
	 * ok , 401 if the user is not properly logged in, and 400 if the action
	 * could not be performed.
	 */

	@DELETE
	@Consumes(MediaType.TEXT_PLAIN)
	@Path("deleteCustomer/{customerId}")

	public Response removeCustomer(@PathParam("customerId") int customerId) {
		logger.entering(getClass().getName(), "deleteCustomer");
		Customer customer = new Customer();
		customer.setCust_id(customerId);
		try {
			getFacadeFromSession().removeCustomer(customer);
			logger.log(Level.FINER, "customer removed successfully ", customer);
			return Response.ok(customer.getCust_name() + " was removed").status(200).build();
		} catch (WebApplicationException e) {
			logger.log(Level.INFO, "UNAUTHORIZED");
			return Response.status(401).entity(e.getMessage()).build();
		} catch (CouponSystemException e) {
			logger.log(Level.SEVERE, "Action failed: " + e.getMessage());
			return Response.status(400).entity(e.getMessage()).build();
		}

	}

	/*
	 * updateCompany edits the information of a company in the system
	 * 
	 * @pathParam- companyId the identifier of the company to be updated.
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
	@Path("updateCompany/{companyId}")
	public Response updateCompany(Company company) {
		logger.entering(getClass().getName(), "updateCompany");
		try {
			getFacadeFromSession().updateCompany(company);
			logger.log(Level.FINER, "company updated successfully ", company);
			return Response.ok(company.getCompName() + " was updated").build();
		} catch (WebApplicationException e) {
			logger.log(Level.INFO, "UNAUTHORIZED");
			return Response.status(401).entity(e.getMessage()).build();
		} catch (CouponSystemException e) {
			logger.log(Level.SEVERE, "Action failed: " + e.getMessage());
			return Response.status(400).entity(e.getMessage()).build();

		}

	}

	/*
	 * updateCustomer edits the information of a customer in the system
	 * 
	 * @pathParam- customerId the identifier of the customer to be updated.
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
	@Path("updateCustomer/{customerId}")

	public Response updateCustomer(Customer customer) {
		logger.entering(getClass().getName(), "updateCustomer");
		try {
			getFacadeFromSession().updateCustomer(customer);
			logger.log(Level.FINER, "customer updated successfully ", customer);
			return Response.ok(customer.getCust_name() + " was updated").build();
		} catch (WebApplicationException e) {
			logger.log(Level.INFO, "UNAUTHORIZED");
			return Response.status(401).entity(e.getMessage()).build();
		} catch (CouponSystemException e) {
			logger.log(Level.SEVERE, "Action failed: " + e.getMessage());
			return Response.status(400).entity(e.getMessage()).build();
		}

	}

	/*
	 * getCompany retrieves a specified company from the couponsystem
	 * 
	 * @pathParam- companyId the identifier of the company to be returned.
	 *
	 * @returns response containing one of the following response codes: 200 if
	 * ok , 401 if the user is not properly logged in, and 404 if the action
	 * could not be performed.
	 * 
	 * a successful response will include the requested company while failure
	 * will include the specific error message from the system.
	 */

	@GET
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getCompany/{companyId}")
	public Response getCompany(@PathParam("companyId") long companyId) {
		logger.entering(getClass().getName(), "getCompany");
		try {
			Company company = getFacadeFromSession().getCompany(companyId);
			logger.exiting(getClass().getName(), "getCompany " + companyId, companyId);
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
	 * getCustomer retrieves a specified customer from the couponsystem
	 * 
	 * @pathParam- customerId the identifier of the customer to be returned.
	 *
	 * @returns response containing one of the following response codes: 200 if
	 * ok , 401 if the user is not properly logged in, and 404 if the action
	 * could not be performed.
	 * 
	 * a successful response will include the requested customer while failure
	 * will include the specific error message from the system.
	 */

	@GET
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getCustomer/{customerId}")

	public Response getCustomer(@PathParam("customerId") long customerId) {
		logger.entering(getClass().getName(), "getCustomer");
		try {
			Customer customer = getFacadeFromSession().getCustomer(customerId);
			logger.exiting(getClass().getName(), "getCustomer " + customerId, customerId);
			return Response.ok(customer).build();
		} catch (WebApplicationException e) {
			logger.log(Level.INFO, "UNAUTHORIZED");
			return Response.status(401).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();

		} catch (CouponSystemException e) {
			logger.log(Level.SEVERE, "Action failed: " + e.getMessage());
			return Response.status(404).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();

		}
	}

	/*
	 * getCustomer retrieves all of the customers in the couponsystem
	 * 
	 *
	 * @returns response containing one of the following response codes: 200 if
	 * ok , 401 if the user is not properly logged in, and 404 if the action
	 * could not be performed.
	 * 
	 * a successful response will include all of the customers while failure
	 * will include the specific error message from the system.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getCustomer")
	public Response getAllCustomers() {
		try {
			logger.entering(getClass().getName(), "getAllCustomers");
			Set<Customer> set = getFacadeFromSession().getAllCustomers();
			GenericEntity<Set<Customer>> genericSet = new GenericEntity<Set<Customer>>(set) {
			};
			logger.exiting(getClass().getName(), "getAllCustomers");
			return Response.ok(genericSet).build();
		} catch (WebApplicationException e) {
			logger.log(Level.INFO, "UNAUTHORIZED");
			return Response.status(401).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
		} catch (CouponSystemException e) {
			logger.log(Level.SEVERE, "Action failed: " + e.getMessage());
			return Response.status(404).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();

		}
	}

	/*
	 * getCompany retrieves all of the companies in the couponsystem
	 * 
	 *
	 * @returns response containing one of the following response codes: 200 if
	 * ok , 401 if the user is not properly logged in, and 404 if the action
	 * could not be performed.
	 * 
	 * a successful response will include all of the companies while failure
	 * will include the specific error message from the system.
	 */
	@GET
	// @Produces(MediaType.APPLICATION_JSON)
	@Path("getCompany")
	public Response getAllCompanies() {
		logger.entering(getClass().getName(), "getAllCompanies");
		try {
			Set<Company> companies = getFacadeFromSession().getAllCompanies();
			GenericEntity<Set<Company>> genericCompanies = new GenericEntity<Set<Company>>(companies) {
			};
			logger.exiting(getClass().getName(), "getAllCompanies");
			return Response.ok(genericCompanies).build();
		} catch (WebApplicationException e) {
			logger.log(Level.INFO, "UNAUTHORIZED");
			return Response.status(401).entity(e.getMessage()).build();
		} catch (CouponSystemException e) {
			logger.log(Level.SEVERE, "Action failed: " + e.getMessage());
			return Response.status(404).entity(e.getMessage()).build();

		}
	}

	/*
	 * method for testing- this message should show up when the resource url is
	 * used in a browser.
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)

	public String test() {
		return "success! you got here(Admin service)!!!";

	}

}
