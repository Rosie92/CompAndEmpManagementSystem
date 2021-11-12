package jcpmv2.jkcho.Dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EmpDto {
    private String ename;
    private String eemail;
    private String ephone;
    private String eposition;
    private String eaffiliation;
    private Long searchCompid; // 화면에서 받아온 회사id 값을 나르기 위한 공간
    private Long ecompid; // DB에서 가져온 직원테이블의 id 값을 나르기 위한 공간
    private String cname; // 회사 테이블에서 join으로 가져온 회사명 정보
    private Long eid; // 직원 업데이트에서 구분을 위한 개인 id
    private Boolean eview;
    private String item;
    private String condition;
    private int pageNo;
}
