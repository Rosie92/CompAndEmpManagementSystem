package jcpmv2.jkcho.Error.Model;

public class EnameNullException extends QsolRuntimeException {
    // 직원 이름 미입력시 발생하는 오류. QsolRuntimeException으로 이동 & 최종적으로 GlobalExceptionHandler에서 처리됨
    public EnameNullException() {
        super("error.ename.null");
    }
}
