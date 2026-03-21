package edu.eci.dosw.tdd.core.util;

import java.time.LocalDate;

public class DateUtil {
	
	private DateUtil() {
	}

	public static LocalDate today() {
		return LocalDate.now();
	}

	public static boolean isPast(LocalDate date) {
		ValidationUtil.requireNotNull(date, "fecha");
		return date.isBefore(today());
	}

	public static boolean isTodayOrFuture(LocalDate date) {
		ValidationUtil.requireNotNull(date, "fecha");
		return !date.isBefore(today());
	}
}
