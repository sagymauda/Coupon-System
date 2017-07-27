package servlets;

import java.io.IOException;
import java.util.logging.Handler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import CouponSystem.step2.CSlogger;

/**
 * Logout Servlet- invalidates the user session, removes the login cookies ,
 * closes logger file handler and redirects to login page
 */
public class logoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		Handler[] handlers = CSlogger.getInstance().getLogger().getHandlers();
		for (Handler handler : handlers) {
			handler.close();
		}
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equalsIgnoreCase("userID") || cookies[i].getName().equalsIgnoreCase("password")
						|| cookies[i].getName().equalsIgnoreCase("clientType")) {
					cookies[i].setMaxAge(0);
					response.addCookie(cookies[i]);
				}
			}
		}

		response.sendRedirect(
				response.encodeRedirectURL("http://localhost:8080/CouponSystemPart2/General/Html/login.html"));
	}

}
