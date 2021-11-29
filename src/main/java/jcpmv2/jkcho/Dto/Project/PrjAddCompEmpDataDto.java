package jcpmv2.jkcho.Dto.Project;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrjAddCompEmpDataDto extends PrjIdDataDto{
    private Long[] eid;
    private Boolean cview;
    private Boolean eview;
}
