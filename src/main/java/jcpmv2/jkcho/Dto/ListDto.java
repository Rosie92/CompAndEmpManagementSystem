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
    // <T> 사용하는 메소드에서 지정한 제네릭 타입
    private Long listCount;
    private int listCount2;
    private Long empListCount;
    private List<T> list;
    private Long compid;

    public ListDto() {
        this.listCount = 0L;
        this.listCount2 = 0;
        this.empListCount = 0L;
        this.list = new ArrayList<>();
    }

}
