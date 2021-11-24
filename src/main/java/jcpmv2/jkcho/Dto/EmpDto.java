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

    private String pagingOff;
    private String participationEmpRemove; // 프로젝트 > 회사 직원 목록 > 새로운 직원을 참가시키기 위해 직원 목록을 불러올 때, 이미 참가중인 직원을 제거하기 위한 구분 item
    private Long cid;
    private Long pid;
}
