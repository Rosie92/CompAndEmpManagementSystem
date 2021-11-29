package jcpmv2.jkcho.Dto.Emp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmpConditionSearchDataDto extends EmpCidGotViewDataDto{
    private String item; // 조건 검색 시, 검색할 문자를 담을 item
    private String condition; // 조건 검색 시, 조건을 담을 item
    private int pageNo; // 조건 검색 완료 후, 목록을 페이징하기 위한 item
}
