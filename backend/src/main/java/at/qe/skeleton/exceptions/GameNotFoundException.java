package at.qe.skeleton.exceptions;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(Long id) {
        super("Could not find Game with ID: " + id);
    }
    public GameNotFoundException(String name) {
        super("Could not find Game " + name);
    }
}