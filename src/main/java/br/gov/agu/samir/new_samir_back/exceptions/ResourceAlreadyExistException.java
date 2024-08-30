package br.gov.agu.samir.new_samir_back.exceptions;

public class ResourceAlreadyExistException extends RuntimeException{
    public ResourceAlreadyExistException() {}

    public ResourceAlreadyExistException(String message) {
        super(message);
    }
}
