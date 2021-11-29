package jcpmv2.jkcho.Dto.Comp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CompConditionSearchDataDto {
    private String item; // 조건 검색 시, 검색할 문자를 담을 item
    private String condition; // 조건 검색 시, 조건을 담을 item
    private int pageNo; // 조건 검색 완료 후 테이블 페이징을 위한 item
}
