package exceptions;

public class FaithfulKittenException extends Exception {
    public FaithfulKittenException() {
        super("This kitten won't betray his current mistress!!! First you need him to be alone");
    }
}
