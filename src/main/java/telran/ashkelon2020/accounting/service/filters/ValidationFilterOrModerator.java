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
import telran.ashkelon2020.forum.dao.PostRepository;
@Service
@Order(60)
public class ValidationFilterOrModerator implements Filter {
	
	@Autowired
	AccountSecurity securityService;
	
	@Autowired
	PostRepository forumRepository;

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
			
			String user = forumRepository.findById(path.split("/")[3]).get().getAuthor();


			if(!login.equals(user) && !securityService.hasRole(request.getUserPrincipal().getName(), "MODERATOR")) {
				response.sendError(403);
				return;
			}
		}
		chain.doFilter(request, response);
	}

	private boolean checkPathAndMethod(String path, String method) {
		boolean res = 
				path.matches("/forum/post/([^/]+)[/]?$") && ("Delete".equalsIgnoreCase(method)||"Put".equalsIgnoreCase(method));
				return res;
	}

}
