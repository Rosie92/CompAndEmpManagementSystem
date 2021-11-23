package jcpmv2.jkcho.Dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CompDto {
    private String cname;
    private String cboss;
    private String ccall;
    private String cnumber;
    private Long cid;
    private Boolean cview;
    private String item;
    private String condition;
    private int pageNo;
    private String count; // 프로젝트 참여 회사 테이블에서 참여 직원 수 표시를 위해 정보를 담아올 그릇
}
