package at.qe.skeleton.exceptions;

public class TopicNotFoundException extends RuntimeException {
    public TopicNotFoundException(Long id) {
        super("Could not find Topic with ID: " + id);
    }
    public TopicNotFoundException(String name) {
        super("Could not find Topic " + name);
    }
}