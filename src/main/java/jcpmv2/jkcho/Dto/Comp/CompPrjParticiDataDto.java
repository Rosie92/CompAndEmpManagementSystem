package jcpmv2.jkcho.Dto.Comp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompPrjParticiDataDto extends CompTableDataDto{
    private String count; // 회사의 해당 프로젝트에 참여하고 있는 직원 수
}
