package jcpmv2.jkcho.Dto.Emp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmpListSearchDataDto extends EmpCidGotViewDataDto{
    private int pageNo; // 페이징을 위한 item
    private String participationEmpRemove; // 프로젝트 > 회사 직원 목록 > 새로운 직원을 참가시키기 위해 직원 목록을 불러올 때, 이미 참가중인 직원을 제거하기 위한 구분 item
    private Long cid; // 회사 id를 확인하기 위한 item
    private Long pid; // 프로젝트 id를 확인하기 위한 item
}
