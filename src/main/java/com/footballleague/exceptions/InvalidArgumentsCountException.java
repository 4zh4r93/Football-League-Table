package exceptions;

public class InvalidArgumentsCountException extends Exception {

  public InvalidArgumentsCountException(String errorMessage) {
      super(errorMessage);
  }

}