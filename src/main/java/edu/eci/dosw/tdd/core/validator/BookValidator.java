package edu.eci.dosw.tdd.core.validator;

import edu.eci.dosw.tdd.core.util.ValidationUtil;

public class BookValidator {

	private BookValidator() {
	}
	
	public static void validateCreateBook(String title, String author, int copies) {
		ValidationUtil.requireLengthBetween(title, 1, 120, "titulo");
		ValidationUtil.requireLengthBetween(author, 1, 120, "autor");
		BookValidator.validateInventory(copies, copies);
	}

	public static void validateAvailableCopies(int copies) {
		ValidationUtil.requirePositive(copies, "copias disponibles");
	}

	public static void validateInventory(int totalStock, int availableStock) {
		ValidationUtil.requirePositive(totalStock, "stock total");
		ValidationUtil.requireTrue(availableStock >= 0, "copias disponibles no puede ser negativo");
		ValidationUtil.requireTrue(availableStock <= totalStock,
				"copias disponibles no puede ser mayor al stock total");
	}

}
