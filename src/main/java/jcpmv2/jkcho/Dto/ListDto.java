package jcpmv2.jkcho.Dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class ListDto<T> {
    private Long count;
    private List<T> list;
    private Long compid;

    public ListDto() {
        this.count = 0L;
        this.list = new ArrayList<>();
    }

}
