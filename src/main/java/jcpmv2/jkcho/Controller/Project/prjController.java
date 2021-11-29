package jcpmv2.jkcho.Controller.Project;

import jcpmv2.jkcho.Dto.*;
import jcpmv2.jkcho.Dto.Comp.CompPrjParticiDataDto;
import jcpmv2.jkcho.Dto.Emp.EmpTableDataDto;
import jcpmv2.jkcho.Dto.Project.*;
import jcpmv2.jkcho.Service.Project.PrjService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@ControllerAdvice
@RestController
@RequestMapping("/api/project/prjController")
public class prjController {
    @Autowired
    private PrjService prjService;

    /*--------------------------------------SELECT--------------------------------------*/
    @GetMapping("/{pageNo}")
    public ResponseEntity<ListDto<PrjTableDataDto>> findAll(PagingDto pagingDto) {
        return ResponseEntity.ok(prjService.findAll(pagingDto));
    }

    @PostMapping("/listConditionSearch")
    public ResponseEntity<ListDto<PrjTableDataDto>> listConditionSearch(@RequestBody @Valid PrjConditionSearchDataDto prjConditionSearchDataDto) {
        return ResponseEntity.ok(prjService.listConditionSearch(prjConditionSearchDataDto));
    }

    @PostMapping("/{pid}")
    public ResponseEntity<ListDto<PrjTableDataDto>> findPnameAndPcontent(@Valid PrjTableDataDto prjTableDataDto) {
        /*
        오류 해결을 위하여 @RequestBody 삭제
        Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported
        */
        return ResponseEntity.ok(prjService.findPnameAndPcontent(prjTableDataDto));
    }

    @PostMapping("/participationCompSearch")
    public ResponseEntity<ListDto<CompPrjParticiDataDto>> findParticipationComp(@RequestBody @Valid PrjIdDataDto prjIdDataDto) {
        /*
        오류 해결을 위하여 @RequestBody 삭제
        Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported
        */
        return ResponseEntity.ok(prjService.findParticipationComp(prjIdDataDto));
    }

    @PostMapping("/compParticiEmpSearch")
    public ResponseEntity<ListDto<EmpTableDataDto>> compParticiEmpSearch(@RequestBody @Valid PrjIdDataDto prjIdDataDto) {
        return ResponseEntity.ok(prjService.compParticiEmpSearch(prjIdDataDto));
    }

    @PostMapping("/prjAddCompDeplicateCheck")
    public ResponseEntity<HttpStatus> prjAddCompDeplicateCheck(@RequestBody @Valid PrjDuplicateCheckDto prjDuplicateCheckDto) {
        prjService.prjAddCompDeplicateCheck(prjDuplicateCheckDto);
        if(prjDuplicateCheckDto.getDuplicateCheck().equals("이미 참여한 회사입니다")) {
            return new ResponseEntity("이미 참여한 회사입니다", HttpStatus.OK);
        }
        return ResponseEntity.ok().build();
    }
    /*--------------------------------------CREATE--------------------------------------*/
    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid PrjTableDataDto prjTableDataDto) {
        if (prjTableDataDto.getPname().equals("")) {
            throw new NullPointerException();
        } else if (prjTableDataDto.getPcontent().equals("")) {
            throw new NullPointerException();
        }
        prjService.create(prjTableDataDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/prjAddCompEmpLastStep")
    public ResponseEntity<HttpStatus> prjAddCompEmpLastStep(@RequestBody @Valid PrjAddCompEmpDataDto prjAddCompEmpDataDto) {
        prjService.prjAddCompEmpLastStep(prjAddCompEmpDataDto);
        return ResponseEntity.ok().build();
    }

    /*--------------------------------------UPDATE--------------------------------------*/
    @GetMapping("/update/{pid}")
    public ResponseEntity<ListDto<PrjTableDataDto>> updatePrjStep(PrjIdDataDto prjIdDataDto) {
        return ResponseEntity.ok(prjService.updatePrjStep(prjIdDataDto));
    }

    @PutMapping()
    public ResponseEntity<HttpStatus> updatePrjTry(@RequestBody @Valid PrjTableDataDto prjTableDataDto) {
        prjService.updatePrjTry(prjTableDataDto);
        return ResponseEntity.ok().build();
    }

    /*--------------------------------------DELETE--------------------------------------*/
    @PutMapping("/{pid}")
    public ResponseEntity<HttpStatus> unrealDelete(@PathVariable @Valid Long pid) {
        prjService.unrealDelete(pid);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{pid}")
    public ResponseEntity<HttpStatus> realDelete(@PathVariable @Valid Long pid) {
        prjService.realDelete(pid);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/prjCompUnrealDelete")
    public ResponseEntity<HttpStatus> prjCompUnrealDelete(@RequestBody @Valid PrjTableDataDto prjTableDataDto) {
        prjService.prjCompUnrealDelete(prjTableDataDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/prjCompRealDelete")
    public ResponseEntity<HttpStatus> prjCompRealDelete(@RequestBody @Valid PrjTableDataDto prjTableDataDto) {
        prjService.prjCompRealDelete(prjTableDataDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/prjEmpUnrealDelete")
    public ResponseEntity<HttpStatus> prjEmpUnrealDelete(@RequestBody @Valid PrjEidToDeleteDataDto prjEidToDeleteDataDto) {
        prjService.prjEmpUnrealDelete(prjEidToDeleteDataDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/prjEmpRealDelete")
    public ResponseEntity<HttpStatus> prjEmpRealDelete(@RequestBody @Valid PrjEidToDeleteDataDto prjEidToDeleteDataDto) {
        prjService.prjEmpRealDelete(prjEidToDeleteDataDto);
        return ResponseEntity.ok().build();
    }
}
