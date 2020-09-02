package telran.ashkelon2020.accounting.service.security;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import telran.ashkelon2020.accounting.dao.UserAccountRepository;
import telran.ashkelon2020.accounting.dto.UserLoginDto;
import telran.ashkelon2020.accounting.dto.exceptions.ForbiddenException;
import telran.ashkelon2020.accounting.dto.exceptions.TokenValidateException;
import telran.ashkelon2020.accounting.dto.exceptions.UnauthorizedException;
import telran.ashkelon2020.accounting.dto.exceptions.UserNotFoundException;
import telran.ashkelon2020.accounting.model.UserAccount;

@Service
@Order(10)
public class AccountSecurityImpl implements AccountSecurity {

	Map<String, String> users = new ConcurrentHashMap<>();
	
	@Autowired
	UserAccountRepository repository;

	@Override
	public String getLogin(String token) {
		UserLoginDto userLoginDto = tokenDecode(token); //получение дто с логином и паролем из токена
		UserAccount userAccount = repository.findById(userLoginDto.getLogin()) //проверка, что такой юзер есть в базе
				.orElseThrow(() -> new UserNotFoundException(userLoginDto.getLogin()));
		if (!BCrypt.checkpw(userLoginDto.getPassword(), userAccount.getPassword())) { //проверка, что пароль юзера и переданный в дто пароль совпадают
			throw new UnauthorizedException();
		}	
		// FIXME check roles (что вообще есть роли)
//		if(userAccount.getRoles().isEmpty()) {
//			throw new UserNotActiveException(userAccount.getLogin());
//		}
		return userAccount.getLogin();
	}

	@Override
	public boolean checkExpDate(String login) {
		UserAccount userAccount = repository.findById(login)
				.orElseThrow(() -> new UserNotFoundException(login));
		if (userAccount.getExpDate().isBefore(LocalDateTime.now())) {
			throw new ForbiddenException();
		}
		return true;
	}

	private UserLoginDto tokenDecode(String token) {
		try {
			String[] credentials = token.split(" ");
			String credential = new String(Base64.getDecoder().decode(credentials[1]));
			credentials = credential.split(":");
			return new UserLoginDto(credentials[0], credentials[1]);
		} catch (Exception e) {
			throw new TokenValidateException();
		}
	}

	@Override
	public boolean isBanned(String login) {
		UserAccount userAccount = repository.findById(login)
				.orElseThrow(()-> new UserNotFoundException(login));
		return userAccount.getRoles().isEmpty();
	}
	
	@Override
	public boolean hasRole(String login, String role) {
		UserAccount userAccount = repository.findById(login)
				.orElseThrow(()-> new UserNotFoundException(login));
		return userAccount.getRoles().contains(role.toUpperCase());
	}
	

	@Override
	public String addUser(String sessionId, String login) {
		return users.put(sessionId, login);
	}

	@Override
	public String getUser(String sessionId) {
		return users.get(sessionId);
	}

	@Override
	public String removeUser(String sessionId) {
		return users.remove(sessionId);
	}






}
