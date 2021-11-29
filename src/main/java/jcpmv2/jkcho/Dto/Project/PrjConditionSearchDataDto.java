package jcpmv2.jkcho.Dto.Project;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrjConditionSearchDataDto extends PrjIdDataDto{
    private String item; // 조건 검색 시, 검색할 문자를 담을 item
    private String condition; // 조건 검색 시, 조건을 담을 item
}
