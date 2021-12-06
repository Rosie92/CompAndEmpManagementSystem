package jcpmv2.jkcho.Error.Model;

public class CnumberNullException extends QsolRuntimeException {
    // 회사 법인번호 미입력시 발생하는 오류. QsolRuntimeException으로 이동 & 최종적으로 GlobalExceptionHandler에서 처리됨
    public CnumberNullException() {
        super("error.cnumber.null");
    }
}
