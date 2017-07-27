package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Exceptions.CouponSystemException;
import Facades.AdminFacade;
import Facades.CompanyFacade;
import Facades.CustomerFacade;
import couponSystem.ClientType;
import couponSystem.CouponSystem;

public class loginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * receives user credentials from login.html form. Performs login Performs
	 * login, sets Facade on session and redirects to corresponding user page.
	 * If client check the “remember me” box, cookies containing login details
	 * will be planted in the response
	 */
	public loginServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
		response.getWriter().println(" you got here!(doGet servlet)");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			long id = Long.parseLong(request.getParameter("id"));
			String password = request.getParameter("password");
			String type = request.getParameter("clientType");
			Boolean remember = Boolean.parseBoolean(request.getParameter("rememberMe"));
			HttpSession session = request.getSession(true);
			String url = "";

			switch (type) {
			case "Admin":
				AdminFacade adminFacade = (AdminFacade) CouponSystem.getInstance().login(0L, password,
						ClientType.ADMIN);
				session.setAttribute("facade", adminFacade);
				url = "http://localhost:8080/CouponSystemPart2/Admin/html/adminMain.html";
				break;
			case "Customer":
				CustomerFacade customerFacade = (CustomerFacade) CouponSystem.getInstance().login(id, password,
						ClientType.CUSTOMER);
				session.setAttribute("facade", customerFacade);
				url = "http://localhost:8080/CouponSystemPart2/Customer/Html/CustomerMain.html";
				break;
			case "Company":
				CompanyFacade companyFacade = (CompanyFacade) CouponSystem.getInstance().login(id, password,
						ClientType.COMPANY);
				session.setAttribute("facade", companyFacade);
				url = "http://localhost:8080/CouponSystemPart2/Company/Html/CompanyMain.html";
				break;
			}
			if (remember) {
				response.addCookie(new Cookie("userID", request.getParameter("id")));
				response.addCookie(new Cookie("password", password));
				response.addCookie(new Cookie("clientType", type));
			}
			response.sendRedirect(response.encodeRedirectURL(url));

		} catch (

		CouponSystemException e) {
			response.sendRedirect(
					response.encodeRedirectURL("http://localhost:8080/CouponSystemPart2/General/Html/login.html"));
		}

	}
}
