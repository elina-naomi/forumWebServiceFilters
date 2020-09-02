package telran.ashkelon2020.accounting.service.security;

public interface AccountSecurity {
	
	String getLogin(String token);
	
	boolean checkExpDate(String login);
	
	boolean isBanned(String login);
	
	boolean hasRole(String login, String role);
		
	String addUser(String sessionId, String login);
	
	String getUser(String sessionId);
	
	String removeUser(String sessionId);

}
