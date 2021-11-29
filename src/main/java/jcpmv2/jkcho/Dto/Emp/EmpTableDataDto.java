package jcpmv2.jkcho.Dto.Emp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EmpTableDataDto {
    private Long eid; // 직원 아이디, 직원 업데이트에서 구분을 위한 개인 id
    private Long ecompid; // 직원이 소속된 회사의 아이디, DB에서 가져온 직원테이블의 id 값을 나르기 위한 공간
    private String ename; // 이름
    private String eemail; // 이메일
    private String ephone; // 휴대폰
    private String eposition; // 소속
    private String eaffiliation; // 직급
    private Boolean eview; // 직원 가삭제 여부 확인
    private String cname; // Domain:CompInfo 에서 join으로 가져온 회사명 정보

}
