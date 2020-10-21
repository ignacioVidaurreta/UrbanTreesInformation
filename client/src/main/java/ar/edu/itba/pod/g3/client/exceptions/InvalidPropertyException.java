package ar.edu.itba.pod.g3.client.exceptions;

public class InvalidPropertyException extends Exception {
    public InvalidPropertyException(String invalidValue, String property){
        super(String.format("%s is not a valid value for %s", invalidValue, property));
    }

}
