package jcpmv2.jkcho.Error.Model;

public class DuplicateException extends QsolRuntimeException {
    // 직원 정보 중복 입력 시 발생하는 오류. QsolRuntimeException으로 이동 & 최종적으로 GlobalExceptionHandler에서 처리됨
    public DuplicateException() {
        super("error.duplicate");
    }
}
