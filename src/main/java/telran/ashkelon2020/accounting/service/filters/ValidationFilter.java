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

import telran.ashkelon2020.accounting.service.security.AccountSecurity;

@Service
@Order(40)
public class ValidationFilter implements Filter {
	
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
			String login = request.getUserPrincipal().getName();
			
			String user = path.split("/")[3];
			if(path.contains("comment")) {
				user = path.split("/")[5];
			}
			if(!login.equals(user)) {
				response.sendError(403);
				return;
			}
		}
		chain.doFilter(request, response);
	}

	private boolean checkPathAndMethod(String path, String method) {
		boolean res = path.matches("^/account/user/([^/]+)[/]?$") && "Put".equalsIgnoreCase(method);
		res = res || (path.matches("^/account/user/([^/]+)[/]?$") && "Delete".equalsIgnoreCase(method)) ||
				path.matches("^/forum/post/([^/]+)[/]?$") && "Post".equalsIgnoreCase(method) ||
				path.matches("/forum/post/([^/]+)/comment/([^/]+)[/]?$") && "Put".equalsIgnoreCase(method);
		return res;
	}

}
