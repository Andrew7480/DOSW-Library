package edu.eci.dosw.tdd.core.validator;

import edu.eci.dosw.tdd.core.util.ValidationUtil;

public class UserValidator {

	private UserValidator() {
	}
	
	public static void validateUserName(String name) {
		ValidationUtil.requireNotBlank(name, "name");
	}

	public static void validateCreateUser(String name, String username, String passwordHash) {
		validateUserName(name);
		ValidationUtil.requireLengthBetween(name, 1, 100, "nombre");
		ValidationUtil.requireNotBlank(username, "username");
		ValidationUtil.requireLengthBetween(username, 1, 100, "username");
		ValidationUtil.requireNotBlank(passwordHash, "passwordHash");
	}

	public static void validateUserId(String userId) {
		ValidationUtil.requireNotBlank(userId, "userId");
	}

	
}
