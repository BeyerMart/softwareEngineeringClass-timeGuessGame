package at.qe.skeleton.exceptions;

public class TermNotFoundException extends RuntimeException {
    public TermNotFoundException(Long id) {
        super("Could not find Term with ID: " + id);
    }
    public TermNotFoundException(String name) {
        super("Could not find Term " + name);
    }
}