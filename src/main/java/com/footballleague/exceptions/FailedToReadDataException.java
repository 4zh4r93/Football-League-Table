package exceptions;

public class FailedToReadDataException extends Exception {

  public FailedToReadDataException(String errorMessage) {
      super(errorMessage);
  }

}