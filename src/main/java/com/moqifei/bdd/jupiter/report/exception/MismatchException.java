package com.moqifei.bdd.jupiter.report.exception;

public class MismatchException extends RuntimeException {
  public MismatchException() {}

  public MismatchException(String message) {
    super(message);
  }

  public MismatchException(String message, Throwable cause) {
    super(message, cause);
  }

  public MismatchException(Throwable cause) {
    super(cause);
  }

  public MismatchException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
