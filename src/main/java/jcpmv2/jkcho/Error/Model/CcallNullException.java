package jcpmv2.jkcho.Error.Model;

public class CcallNullException extends QsolRuntimeException {
    // 회사 대표전화 미입력시 발생하는 오류. QsolRuntimeException으로 이동 & 최종적으로 GlobalExceptionHandler에서 처리됨
    public CcallNullException() {
        super("error.ccall.null");
    }
}
