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
    @PostMapping()
    public ResponseEntity<ListDto<EmpTableDataDto>> findAllByEcompidOrderByEnamePaging(@RequestBody @Valid EmpListSearchDataDto empListSearchDataDto) {/*SearchingDto searchingDto*/
        return ResponseEntity.ok(empService.findAllByEcompidOrderByEnamePaging(empListSearchDataDto));/*searchingDto*/
    }

    @PostMapping("/emplistConditionSearch")
    public ResponseEntity<ListDto<EmpTableDataDto>> emplistConditionSearch(@RequestBody @Valid EmpConditionSearchDataDto empConditionSearchDataDto) {
        return ResponseEntity.ok(empService.emplistConditionSearch(empConditionSearchDataDto));
    }

    /*--------------------------------------CREATE--------------------------------------*/
    @PostMapping("/create")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid EmpCidGotViewDataDto empCidGotViewDataDto) {
        empCidGotViewDataDto.setEname(empCidGotViewDataDto.getEname().trim());
        empCidGotViewDataDto.setEemail(empCidGotViewDataDto.getEemail().trim());
        empCidGotViewDataDto.setEphone(empCidGotViewDataDto.getEphone().trim());
        empCidGotViewDataDto.setEposition(empCidGotViewDataDto.getEposition().trim());
        empCidGotViewDataDto.setEaffiliation(empCidGotViewDataDto.getEaffiliation().trim());
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
    public ResponseEntity<ListDto<EmpTableDataDto>> findAll(EmpTableDataDto empTableDataDto){
        return ResponseEntity.ok(empService.empUpdateReady(empTableDataDto));
    }

    @PutMapping()
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid EmpTableDataDto empTableDataDto) {
        empTableDataDto.setEname(empTableDataDto.getEname().trim());
        empTableDataDto.setEemail(empTableDataDto.getEemail().trim());
        empTableDataDto.setEphone(empTableDataDto.getEphone().trim());
        empTableDataDto.setEposition(empTableDataDto.getEposition().trim());
        empTableDataDto.setEaffiliation(empTableDataDto.getEaffiliation().trim());
        if(empTableDataDto.getEname().equals("")) {
            throw new EnameNullException();
        } else if(empTableDataDto.getEphone().equals("")) {
            throw new EphoneNullException();
        }
        empService.update(empTableDataDto);
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
