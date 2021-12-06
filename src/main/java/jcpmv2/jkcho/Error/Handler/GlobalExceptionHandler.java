package jcpmv2.jkcho.Error.Handler;

import jcpmv2.jkcho.Dto.ErrorInfoDto;
import jcpmv2.jkcho.Error.Model.QsolRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@ControllerAdvice
/*
    컨트롤러를 보조하는 클래스임을 명시
    @컨트롤러 어노테이션을 갖거나 xml 설정 파일에서 컨트롤러로 명시된 클래스에서 발생한 Exception을 감지
    유사 어노테이션 = @RestControllerAdvice
    @ControllerAdvice(com.freeboard01.api.boardApi) 와 같이 특정 클래스만 명시하는 것도 가능
*/
@RestController // @Controller와 @ResponseBody 를 합친 어노테이션
@Slf4j
public class GlobalExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ResponseBody
    /*
        ExceptionHandler는 컨트롤러 혹은 레스트 컨트롤러가 아님, 응답값은 컨트롤러처럼 String 또는 ModelAndView만 가능
        따라서 응답 객체를 반환하고자 하면 @ResponseBody 어노테이션을 메소드 위에 명시하여야함
        String이나 ModelAndView를 이용해 에러 코드 혹은 메시지만 반환하거나 Map으로 반환하는 것 client 측에서 Error를 처리함에
        있어 일관성이 부족한 응답으로 인해 불편함이 많을 것이기에 객체를 활용해 통일된 형식을 지키도록 함
    */
    @ExceptionHandler(value = Exception.class)
    /*
        @ControllerAdvice가 명시된 클래스 내부 메소드에 사용, Attribute로 Exception 클래스를 받음
        즉, RuntimeException.class나 더 상위 클래스인 Exception.class 등을 넘기면 됨
     */
    public ResponseEntity<ErrorInfoDto> handleException(Exception e) {
        log.error("--- global exception handler --- {}", e.getLocalizedMessage());
        /*JOptionPane.showMessageDialog(null, e, "ERROR", JOptionPane.ERROR_MESSAGE);*/
        List<String> errorMessages = new ArrayList<>();
        errorMessages.add(e.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorInfoDto(999, "Error Occurs", errorMessages));
    }

    @ResponseBody
    @ExceptionHandler(NullPointerException.class) // NullPointerException 발생 시 캐치하여 실행되는 메소드
    public ResponseEntity<ErrorInfoDto> NullPointerException(final NullPointerException e) {
        log.error("--- NullPointerException handler --- {}", e.getLocalizedMessage());
        List<String> errorMessages = new ArrayList<>();
        errorMessages.add(e.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                body(new ErrorInfoDto(999, "Error Occurs", errorMessages));
    }

    /*@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)*/
    @ResponseBody
    @ExceptionHandler(QsolRuntimeException.class) // 만들어진 오류 클래스로부터 QsolRuntimeException을 거쳐 이곳으로 오게됨
    public ResponseEntity<ErrorInfoDto> ItemNullException (final QsolRuntimeException e) {
        System.out.println("compCreateItemNull");
        /*final ErrorInfoDto errorInfo = new ErrorInfoDto();
        errorInfo.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorInfo.setErrorMessage(messageSource.getMessage(e.getMessage(), null, locale));
        return errorInfo;*/
        log.error("--- CnameNullExeption handler --- {}", e.getLocalizedMessage());
        List<String> errorMessage = new ArrayList<>();
        errorMessage.add(e.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                body(new ErrorInfoDto(998, "compCreateItemNull", errorMessage));
    }

}
