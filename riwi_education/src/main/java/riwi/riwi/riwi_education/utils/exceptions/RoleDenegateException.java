package riwi.riwi.riwi_education.utils.exceptions;

public class RoleDenegateException  extends RuntimeException{
    private static final String ERROR_MESSAGE = "El Usuario debe ser de tipo %s";

    public RoleDenegateException(String rol) {
        super(String.format(ERROR_MESSAGE, rol));
    }
}
