package edu.eci.dosw.tdd.core.util;

public class ValidationUtil {

	private ValidationUtil() {
		throw new IllegalStateException("Utility class");
	}

	public static <T> T requireNonNull(T value, String fieldName) {
		if (value == null) {
			throw new IllegalArgumentException(fieldName + " no puede ser nulo");
		}
		return value;
	}

	public static String requireNonBlank(String value, String fieldName) {
		requireNonNull(value, fieldName);
		if (value.trim().isEmpty()) {
			throw new IllegalArgumentException(fieldName + " no puede estar vacío");
		}
		return value.trim();
	}

	public static String requireLengthBetween(String value, String fieldName, int minLength, int maxLength) {
		String sanitizedValue = requireNonBlank(value, fieldName);
		int length = sanitizedValue.length();
		if (length < minLength || length > maxLength) {
			throw new IllegalArgumentException(
				fieldName + " debe tener entre " + minLength + " y " + maxLength + " caracteres"
			);
		}
		return sanitizedValue;
	}

	public static void requireTrue(boolean condition, String message) {
		if (!condition) {
			throw new IllegalArgumentException(message);
		}
	}
}
