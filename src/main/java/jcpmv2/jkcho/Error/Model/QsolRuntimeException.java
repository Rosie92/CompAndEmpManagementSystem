package jcpmv2.jkcho.Error.Model;

public class QsolRuntimeException extends RuntimeException { 
    /*
        지정된 오류를 발생시키고, 지정한 메시지를 보내기 위해 생성된 메소드, RuntimeException을 상속
        발생된 오류 클래스로부터 이곳으로 이동 후 GlobalExceptionHandler로 이동하여 처리됨
     */
    public QsolRuntimeException() {super();}
    public QsolRuntimeException(String message) {super(message);}
}
