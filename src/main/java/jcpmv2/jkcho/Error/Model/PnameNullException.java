package jcpmv2.jkcho.Error.Model;

public class PnameNullException extends QsolRuntimeException {
    // 프로젝트 명 미입력시 발생하는 오류. QsolRuntimeException으로 이동 & 최종적으로 GlobalExceptionHandler에서 처리됨
    public PnameNullException() {
        super("error.pname.null");
    }
}
