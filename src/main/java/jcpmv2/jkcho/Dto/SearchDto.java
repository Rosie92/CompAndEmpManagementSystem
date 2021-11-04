package jcpmv2.jkcho.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchDto {
    private Integer pageNo = 1;
    private Integer countPerPage = 5;
}
