package at.qe.skeleton.exceptions;

public class TeamNotFoundException extends RuntimeException {
    public TeamNotFoundException(Long id) {
        super("Could not find Team with ID: " + id);
    }
    public TeamNotFoundException(String name) {
        super("Could not find Team " + name);
    }
}