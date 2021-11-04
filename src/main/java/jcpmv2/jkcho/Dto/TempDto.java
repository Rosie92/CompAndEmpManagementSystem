package jcpmv2.jkcho.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TempDto {
    private Long cid; // 선택한 회사 id 값 가져오기
    private String searchCompId;
    private String searchCname;
}
