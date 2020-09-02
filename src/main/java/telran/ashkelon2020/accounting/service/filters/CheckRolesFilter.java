package telran.ashkelon2020.accounting.service.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import telran.ashkelon2020.accounting.dto.exceptions.UserNotFoundException;
import telran.ashkelon2020.accounting.service.security.AccountSecurity;

@Service
@Order(30)
public class CheckRolesFilter implements Filter {
	
	@Autowired
	AccountSecurity securityService;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		String path = request.getServletPath();
		String method = request.getMethod();
		if(checkPathAndMethod(path, method)) {
			System.out.println(path + this.getClass());

			try {
				if(securityService.isBanned(request.getUserPrincipal().getName())) {
					response.sendError(403);
					return;
				}
			} catch (UserNotFoundException e) {
				response.sendError(404);
				return;
			}
		}
		chain.doFilter(request, response);
	}

	private boolean checkPathAndMethod(String path, String method) {
		boolean res = "/account/login".equalsIgnoreCase(path) && "Post".equalsIgnoreCase(method);
		res = res || ("Put".equalsIgnoreCase(method) 
				&& path.matches("^/account/user/([^/]+)[/]?$")) ||
				path.contains("/forum/post/") && ("Put".equalsIgnoreCase(method)||"Delete".equalsIgnoreCase(method)||"Post".equalsIgnoreCase(method));
				return res;
	}

}
