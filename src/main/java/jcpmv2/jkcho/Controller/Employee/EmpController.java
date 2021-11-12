package jcpmv2.jkcho.Controller.Employee;

import jcpmv2.jkcho.Dto.CompDto;
import jcpmv2.jkcho.Dto.EmpDto;
import jcpmv2.jkcho.Dto.ListDto;
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
    @PostMapping()
    public ResponseEntity<ListDto<EmpDto>> findAllByEcompidOrderByEnamePaging(@RequestBody @Valid EmpDto empDto) {/*SearchingDto searchingDto*/
        return ResponseEntity.ok(empService.findAllByEcompidOrderByEnamePaging(empDto));/*searchingDto*/
    }

    @PostMapping("/emplistConditionSearch")
    public ResponseEntity<ListDto<EmpDto>> emplistConditionSearch(@RequestBody @Valid EmpDto empDto) {
        return ResponseEntity.ok(empService.emplistConditionSearch(empDto));
    }

    /*--------------------------------------CREATE--------------------------------------*/
    @PostMapping("/create")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid EmpDto empDto) {
        empService.create(empDto);
        if(empDto.getEname().equals("중복된 사원입니다(이름과 이메일이 중복)")) {
            return new ResponseEntity("중복된 사원입니다(이름과 이메일이 중복)", HttpStatus.OK);
        } else {
            return ResponseEntity.ok().build();
        }
    }

    /*--------------------------------------UPDATE--------------------------------------*/
    @GetMapping("/updateStep/{eid}")
    public ResponseEntity<ListDto<EmpDto>> findAll(EmpDto empDto){
        return ResponseEntity.ok(empService.empUpdateReady(empDto));
    }

    @PutMapping()
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid EmpDto empDto) {
        empService.empUpdateDirtyChecking(empDto);
        return ResponseEntity.ok().build();
    }
    /*--------------------------------------DELETE--------------------------------------*/
    @PutMapping("/{eid}")
    public ResponseEntity<HttpStatus> unrealDelete(@PathVariable @Valid Long eid) {
        empService.unrealDelete(eid);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{eid}")
    public ResponseEntity<HttpStatus> realDelete(@PathVariable @Valid Long eid) {
        empService.realDelete(eid);
        return ResponseEntity.ok().build();
    }
}
