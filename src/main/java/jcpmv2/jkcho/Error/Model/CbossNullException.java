package jcpmv2.jkcho.Error.Model;

public class CbossNullException extends QsolRuntimeException {
    // 회사 대표자명 미입력시 발생하는 오류. QsolRuntimeException으로 이동 & 최종적으로 GlobalExceptionHandler에서 처리됨
    public CbossNullException() {
        super("error.cboss.null");
    }
}
