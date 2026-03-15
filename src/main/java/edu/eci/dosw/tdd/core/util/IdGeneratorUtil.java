package edu.eci.dosw.tdd.core.util;

import java.util.UUID;

public class IdGeneratorUtil {


	private IdGeneratorUtil() {
		throw new IllegalStateException("Utility class");
	}

	public static String generateId() {
		return UUID.randomUUID().toString();
	}

	public static String generateId(String prefix) {
		String sanitizedPrefix = ValidationUtil.requireNonBlank(prefix, "El prefijo del identificador");
		return sanitizedPrefix + "-" + generateId();
	}
}
