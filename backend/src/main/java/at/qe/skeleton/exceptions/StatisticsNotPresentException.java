package at.qe.skeleton.exceptions;

public class StatisticsNotPresentException extends RuntimeException {
    public StatisticsNotPresentException(Long id) {
        super("No statistics available for user with ID: " + id);
    }
}
