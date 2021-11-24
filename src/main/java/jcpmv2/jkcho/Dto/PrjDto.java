package jcpmv2.jkcho.Dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PrjDto {
    private Long pid;
    private String pname;
    private String pcontent;
    private Boolean pview;
    private Boolean cview;
    private Boolean eview;
    private String item;
    private String condition;
    private int pageNo;
    private Long cid;
    private Long[] eid;
    private String duplicateCheck;

}
