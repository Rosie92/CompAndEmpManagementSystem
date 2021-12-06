package jcpmv2.jkcho.Controller.Employee;

import jcpmv2.jkcho.Dto.Emp.*;
import jcpmv2.jkcho.Dto.ListDto;
import jcpmv2.jkcho.Error.Model.*;
import jcpmv2.jkcho.Service.Employee.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/employee/empController")
public class EmpController {
    @Autowired
    private EmpService empService;

    /*--------------------------------------SELECT--------------------------------------*/
    @PostMapping() // 직원 전체 리스트 검색
    public ResponseEntity<ListDto<EmpTableDataDto>> findAllByEcompidOrderByEnamePaging(@RequestBody @Valid EmpListSearchDataDto empListSearchDataDto) {/*SearchingDto searchingDto*/
        return ResponseEntity.ok(empService.findAllByEcompidOrderByEnamePaging(empListSearchDataDto));/*searchingDto*/
    }

    @PostMapping("/emplistConditionSearch") // 직원 조건 리스트 검색
    public ResponseEntity<ListDto<EmpTableDataDto>> emplistConditionSearch(@RequestBody @Valid EmpConditionSearchDataDto empConditionSearchDataDto) {
        return ResponseEntity.ok(empService.emplistConditionSearch(empConditionSearchDataDto));
    }

    /*--------------------------------------CREATE--------------------------------------*/
    @PostMapping("/create") // 직원 생성
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid EmpCidGotViewDataDto empCidGotViewDataDto) {
        // 입력받은 직원 정보 앞뒤 공백을 제거(trim)
        empCidGotViewDataDto.setEname(empCidGotViewDataDto.getEname().trim());
        empCidGotViewDataDto.setEemail(empCidGotViewDataDto.getEemail().trim());
        empCidGotViewDataDto.setEphone(empCidGotViewDataDto.getEphone().trim());
        empCidGotViewDataDto.setEposition(empCidGotViewDataDto.getEposition().trim());
        empCidGotViewDataDto.setEaffiliation(empCidGotViewDataDto.getEaffiliation().trim());
        // 입력받은 정보 유효성 체크 (이름과 휴대폰 필수 항목)
        if(empCidGotViewDataDto.getEname().equals("")) {
            throw new EnameNullException();
        } else if(empCidGotViewDataDto.getEphone().equals("")) {
            throw new EphoneNullException();
        }
        empService.create(empCidGotViewDataDto);
        return ResponseEntity.ok().build();
    }

    /*--------------------------------------UPDATE--------------------------------------*/
    @GetMapping("/updateStep/{eid}")
    // 업데이트 실행 전 해당 직원의 정보를 가져옴
    public ResponseEntity<ListDto<EmpTableDataDto>> findAll(EmpTableDataDto empTableDataDto){
        return ResponseEntity.ok(empService.empUpdateReady(empTableDataDto));
    }

    @PutMapping() // 업데이트 실행
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid EmpTableDataDto empTableDataDto) {
        // 입력받은 직원 정보 앞뒤 공백을 제거(trim)
        empTableDataDto.setEname(empTableDataDto.getEname().trim());
        empTableDataDto.setEemail(empTableDataDto.getEemail().trim());
        empTableDataDto.setEphone(empTableDataDto.getEphone().trim());
        empTableDataDto.setEposition(empTableDataDto.getEposition().trim());
        empTableDataDto.setEaffiliation(empTableDataDto.getEaffiliation().trim());
        // 입력받은 정보 유효성 체크 (이름과 휴대폰 필수 항목)
        if(empTableDataDto.getEname().equals("")) {
            throw new EnameNullException();
        } else if(empTableDataDto.getEphone().equals("")) {
            throw new EphoneNullException();
        }
        empService.update(empTableDataDto);
        return ResponseEntity.ok().build();
    }
    /*--------------------------------------DELETE--------------------------------------*/
    @PutMapping("/{eid}") // 업데이트 가삭제 실행
    public ResponseEntity<HttpStatus> unrealDelete(@PathVariable @Valid Long eid) {
        empService.unrealDelete(eid);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{eid}") // 업데이트 실삭제 진행
    public ResponseEntity<HttpStatus> realDelete(@PathVariable @Valid Long eid) {
        empService.realDelete(eid);
        return ResponseEntity.ok().build();
    }
}

