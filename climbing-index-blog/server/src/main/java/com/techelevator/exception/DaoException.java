package com.techelevator.exception;

/*
Problem: While you use ResponseStatusException, the error messages sometimes lack specificity or are inconsistent ("DAO Error - " + e.getMessage()).
         Relying solely on DaoException can make it hard to distinguish between different types of database issues.

Solution:
    -Consider creating more specific custom exceptions for common business rule violations
     * (e.g., UserAlreadyExistsException, ResourceNotFoundException, PasswordsMismatchException).
    -Map these custom exceptions to appropriate HTTP status codes using @ExceptionHandler in a @ControllerAdvice or directly in the controller if it's a simple case.
    -This provides clearer, more structured error responses to the client.

Example: Instead of throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists.");,
         you could throw a UserAlreadyExistsException and have Spring handle the mapping to BAD_REQUEST.
*
* */


public class DaoException extends RuntimeException {
    public DaoException() {
        super();
    }
    public DaoException(String message) {
        super(message);
    }
    public DaoException(String message, Exception cause) {
        super(message, cause);
    }
}
