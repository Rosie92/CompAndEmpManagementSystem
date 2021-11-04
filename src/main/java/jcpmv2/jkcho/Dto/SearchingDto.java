package jcpmv2.jkcho.Dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class SearchingDto extends PagingDto {
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate fromDate;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate toDate;

    public LocalDateTime from() {
        return fromDate == null ? null : LocalDateTime.of(fromDate, LocalTime.of(0, 0, 0));
    }

    public LocalDateTime to() {
        return toDate == null ? null : LocalDateTime.of(toDate.plusDays(1), LocalTime.of(0, 0, 0));
    }

}
