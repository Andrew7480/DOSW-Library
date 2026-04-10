package edu.eci.dosw.tdd.core.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.persistence.nonrelational.document.BookDocument;
import edu.eci.dosw.tdd.persistence.nonrelational.document.LoanDocument;
import edu.eci.dosw.tdd.persistence.nonrelational.document.UserDocument;
import edu.eci.dosw.tdd.persistence.nonrelational.mapper.LoanDocumentMapper;
import edu.eci.dosw.tdd.persistence.nonrelational.repository.MongoBookRepository;
import edu.eci.dosw.tdd.persistence.nonrelational.repository.MongoUserRepository;

@Repository
@Profile("mongo")
public class LoanRepositoryMongoImpl implements LoanRepository {

	private final MongoUserRepository userRepository;
	private final MongoBookRepository bookRepository;

	public LoanRepositoryMongoImpl(MongoUserRepository userRepository, MongoBookRepository bookRepository) {
		this.userRepository = userRepository;
		this.bookRepository = bookRepository;
	}

	@Override
	public Loan save(Loan loan) {
		UserDocument user = findUserForLoanOrThrow(loan);
		List<LoanDocument> currentLoans = safeLoans(user);
		LoanDocument document = LoanDocumentMapper.toDocument(loan);
		List<LoanDocument> updatedLoans = Stream.concat(
				currentLoans.stream().filter(existing -> !loan.getId().equals(existing.getId())),
				Stream.of(document)
		).toList();

		user.setLoans(new ArrayList<>(updatedLoans));

		userRepository.save(user);
		return loan;
	}

	@Override
	public Optional<Loan> findById(String id) {
		return userRepository.findByLoanId(id)
				.flatMap(user -> safeLoans(user).stream()
						.filter(loan -> id.equals(loan.getId()))
						.findFirst()
						.map(loan -> LoanDocumentMapper.toDomain(loan, user, resolveBookTitle(loan.getBookId()))));
	}

	@Override
	public List<Loan> findAll() {
		return userRepository.findAll().stream()
				.flatMap(user -> safeLoans(user).stream()
						.map(loan -> LoanDocumentMapper.toDomain(loan, user, resolveBookTitle(loan.getBookId()))))
				.toList();
	}

	@Override
	public void delete(String id) {
		userRepository.findByLoanId(id).ifPresent(user -> {
			List<LoanDocument> updatedLoans = safeLoans(user).stream()
					.filter(loan -> !id.equals(loan.getId()))
					.toList();
			user.setLoans(new ArrayList<>(updatedLoans));
			userRepository.save(user);
		});
	}

	@Override
	public List<Loan> findByUsername(String username) {
		return userRepository.findByUsername(username)
				.map(user -> safeLoans(user).stream()
						.map(loan -> LoanDocumentMapper.toDomain(loan, user, resolveBookTitle(loan.getBookId())))
						.toList())
				.orElseGet(List::of);
	}

	@Override
	public Optional<Loan> findByUsernameAndBookTitle(String username, String bookTitle) {
		return userRepository.findByUsername(username)
				.flatMap(user -> safeLoans(user).stream()
						.filter(loan -> isLoanForBookTitle(loan, bookTitle))
						.findFirst()
						.map(loan -> LoanDocumentMapper.toDomain(loan, user, resolveBookTitle(loan.getBookId()))));
	}

	private UserDocument findUserForLoanOrThrow(Loan loan) {
		return userRepository.findByUsername(loan.getUser().getUsername())
				.orElseGet(() -> userRepository.findById(loan.getUser().getId())
						.orElseThrow(() -> new IllegalStateException("User not found for loan")));
	}

	private List<LoanDocument> safeLoans(UserDocument user) {
		return user.getLoans() == null ? List.of() : user.getLoans();
	}

	private String resolveBookTitle(String bookId) {
		if (bookId == null) {
			return null;
		}
		return bookRepository.findById(bookId)
				.map(BookDocument::getTitle)
				.orElse(bookId);
	}

	private boolean isLoanForBookTitle(LoanDocument loan, String bookTitle) {
		if (bookTitle == null || loan.getBookId() == null) {
			return false;
		}
		String resolvedTitle = resolveBookTitle(loan.getBookId());
		return resolvedTitle != null && resolvedTitle.equalsIgnoreCase(bookTitle);
	}
}
