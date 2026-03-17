package edu.eci.dosw.tdd.core.validator;

import edu.eci.dosw.tdd.core.util.ValidationUtil;

public class BookValidator {

	public static void validateCreateBook(String title, String author, int copies) {
		ValidationUtil.requireLengthBetween(title, 1, 120, "titulo");
		ValidationUtil.requireLengthBetween(author, 1, 120, "autor");
		BookValidator.validateAvailableCopies(copies);
	}

	public static void validateBookId(String bookId) {
		ValidationUtil.requireNotBlank(bookId, "bookId");
	}

	public static void validateAvailableCopies(int copies) {
		ValidationUtil.requirePositive(copies, "copias disponibles");
	}

}
