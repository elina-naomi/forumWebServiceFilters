package telran.ashkelon2020.accounting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserChangePasswordDto {
	String oldPassword;
	String newPassword;
	String newPasswordConfirm;
}
