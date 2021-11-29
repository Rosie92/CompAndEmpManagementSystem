package jcpmv2.jkcho.Dto.Comp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CompTableDataDto {
    private Long cid; // 회사 아이디
    private String cname; // 회사명
    private String cboss; // 대표자명
    private String ccall; // 대표번호
    private String cnumber; //법인번호
    private Boolean cview; // 회사 가삭제 여부 확인
}
