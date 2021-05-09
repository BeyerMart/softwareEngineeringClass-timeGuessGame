package at.qe.skeleton.exceptions;

public class RoomNotFoundException extends RuntimeException {
    public RoomNotFoundException(Long id) {
        super("Could not find room " + id);
    }
}
