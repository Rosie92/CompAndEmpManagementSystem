package jcpmv2.jkcho.Dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorInfoDto {
    /*
        int는 자료형 선언, 값이 안들어왔을 때 0 값을 반환
        integer는 객체로 만들어 래퍼 클래스로 선언, 값이 안들어왔을 때 null 값을 반환
        마찬가지로 Boolean은 null, boolean은 false
     */
    private Integer errorCode;
    private String errorMessage;
    private List<String> errorMessages;
}
