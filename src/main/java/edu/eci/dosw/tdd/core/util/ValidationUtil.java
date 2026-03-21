package edu.eci.dosw.tdd.core.util;

import edu.eci.dosw.tdd.core.exception.InvalidInputException;

public class ValidationUtil {


	public static void requireNotNull(Object value, String fieldName) {
		if (value == null) {
			throw new InvalidInputException(fieldName + " no puede ser nulo");
		}
	}

	public static void requireNotBlank(String value, String fieldName) {
		requireNotNull(value, fieldName);
		if (value.isBlank()) {
			throw new InvalidInputException(fieldName + " no puede estar vacio");
		}
	}

	public static void requireLengthBetween(String value, int min, int max, String fieldName) {
		requireNotBlank(value, fieldName);
		int length = value.trim().length();
		if (length < min || length > max) {
			throw new InvalidInputException(
					String.format("%s debe tener entre %d y %d caracteres", fieldName, min, max));
		}
	}

	public static void requirePositive(int value, String fieldName) {
		if (value <= 0) {
			throw new InvalidInputException(fieldName + " debe ser mayor a cero");
		}
	}

	public static void requireTrue(boolean condition, String message) {
		if (!condition) {
			throw new InvalidInputException(message);
		}
	}
}
