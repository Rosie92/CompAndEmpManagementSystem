package jcpmv2.jkcho.Error.Model;

public class EphoneNullException extends QsolRuntimeException {
    // 직원 휴대폰 미입력시 발생하는 오류. QsolRuntimeException으로 이동 & 최종적으로 GlobalExceptionHandler에서 처리됨
    public EphoneNullException() {
        super("error.ephone.null");
    }
}
