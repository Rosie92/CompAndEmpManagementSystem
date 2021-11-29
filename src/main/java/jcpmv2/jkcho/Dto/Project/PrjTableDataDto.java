package jcpmv2.jkcho.Dto.Project;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrjTableDataDto extends PrjIdDataDto{
    private Long pid; // 프로젝트id
    private String pname; // 프로젝트명
    private String pcontent; // 프로젝트내용
    private Boolean pview; // 프로젝트 가삭제 여부 확인
}
