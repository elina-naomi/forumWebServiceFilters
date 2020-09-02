package telran.ashkelon2020.accounting.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import telran.ashkelon2020.accounting.dto.RolesResponseDto;
import telran.ashkelon2020.accounting.dto.UserAccountResponseDto;
import telran.ashkelon2020.accounting.dto.UserRegisterDto;
import telran.ashkelon2020.accounting.dto.UserUpdateDto;
import telran.ashkelon2020.accounting.service.UserAccountService;
import telran.ashkelon2020.accounting.service.security.AccountSecurity;

@RestController
@RequestMapping("/account")
public class UserAccountController {

	@Autowired
	UserAccountService accountService;
	
	@Autowired
	AccountSecurity securityService;

	@PostMapping("/register")
	public UserAccountResponseDto register(@RequestBody UserRegisterDto userRegisterDto) {
		return accountService.addUser(userRegisterDto);
	}

	@PostMapping("/login")
	public UserAccountResponseDto login(Principal principal) {

		return accountService.getUser(principal.getName());
	}

	@PutMapping("/user/{login}")
	public UserAccountResponseDto updateUser(@PathVariable String login, @RequestBody UserUpdateDto userUpdateDto) {
		return accountService.editUser(login, userUpdateDto);
	}

	@DeleteMapping("/user/{login}")
	public UserAccountResponseDto removeUser(@PathVariable String login) {
		return accountService.removeUser(login);
	}

	//FIXME сделать авторизацию, проверить что это админ
	@PutMapping("user/{login}/role/{role}")
	public RolesResponseDto addRole(@PathVariable String login, @PathVariable String role) {

		return accountService.changeRolesList(login, role, true);
	}

	//FIXME сделать авторизацию, проверить что это админ
	@DeleteMapping("user/{login}/role/{role}")
	public RolesResponseDto removeRole(@PathVariable String login, @PathVariable String role) {

		return accountService.changeRolesList(login, role, false);
	}

	
	@PutMapping("/password")
	public void changePassword(@RequestHeader("X-Password") String newPassword, Principal principal) {
		accountService.changePassword(principal.getName(), newPassword);
	}


}
