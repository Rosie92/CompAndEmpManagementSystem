package jcpmv2.jkcho.Error.Model;

public class DuplicateException extends QsolRuntimeException {
    public DuplicateException() {
        super("error.duplicate");
    }
}
