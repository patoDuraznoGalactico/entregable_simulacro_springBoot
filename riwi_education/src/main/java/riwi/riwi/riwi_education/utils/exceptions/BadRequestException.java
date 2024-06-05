package riwi.riwi.riwi_education.utils.exceptions;

public class BadRequestException extends RuntimeException {

    private static final String ERROR_MESSAGE = "No hay registros en la entidad %s con el id suministrado";

    public BadRequestException(String message) {
        super(String.format(ERROR_MESSAGE,message));
    }
}
