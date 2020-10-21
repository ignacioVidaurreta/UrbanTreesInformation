package ar.edu.itba.pod.g3.client.exceptions;


/**
 * Exception thrown when a required property isn't inserted.
 */
public class RequiredPropertyException extends Exception {
    public RequiredPropertyException(String property){
        super(String.format("Missing required property: %s", property));
    }
}
