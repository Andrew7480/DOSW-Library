package edu.eci.dosw.tdd.core.validator;

import edu.eci.dosw.tdd.core.util.ValidationUtil;

public class UserValidator {


	public static void validateCreateUser(String name) {
		ValidationUtil.requireLengthBetween(name, 1, 100, "nombre");
	}

	public static void validateUserId(String userId) {
		ValidationUtil.requireNotBlank(userId, "userId");
	}

}
