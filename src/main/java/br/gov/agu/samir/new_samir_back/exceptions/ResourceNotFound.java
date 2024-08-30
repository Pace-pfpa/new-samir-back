package br.gov.agu.samir.new_samir_back.exceptions;

public class ResourceNotFound extends RuntimeException{
    public ResourceNotFound() {
    }

    public ResourceNotFound(String message) {
        super(message);
    }
}
