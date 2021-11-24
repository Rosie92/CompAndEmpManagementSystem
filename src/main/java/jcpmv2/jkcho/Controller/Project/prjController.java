package jcpmv2.jkcho.Controller.Project;

import jcpmv2.jkcho.Dto.*;
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
    public ResponseEntity<ListDto<PrjDto>> findAll(PagingDto pagingDto) {
        return ResponseEntity.ok(prjService.findAll(pagingDto));
    }

    @PostMapping("/listConditionSearch")
    public ResponseEntity<ListDto<PrjDto>> listConditionSearch(@RequestBody @Valid PrjDto prjDto) {
        return ResponseEntity.ok(prjService.listConditionSearch(prjDto));
    }

    @PostMapping("/{pid}")
    public ResponseEntity<ListDto<PrjDto>> findPnameAndPcontent(@Valid PrjDto prjDto) {
        /*
        오류 해결을 위하여 @RequestBody 삭제
        Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported
        */
        return ResponseEntity.ok(prjService.findPnameAndPcontent(prjDto));
    }

    @PostMapping("/participationCompSearch/{pid}")
    public ResponseEntity<ListDto<CompDto>> findParticipationComp(@Valid PrjDto prjDto) {
        /*
        오류 해결을 위하여 @RequestBody 삭제
        Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported
        */
        return ResponseEntity.ok(prjService.findParticipationComp(prjDto));
    }

    @PostMapping("/compPartiEmpSearch")
    public ResponseEntity<ListDto<EmpDto>> compPartiEmpSearch(@RequestBody @Valid PrjDto prjDto) {
        return ResponseEntity.ok(prjService.compPartiEmpSearch(prjDto));
    }

    @PostMapping("/prjAddCompDeplicateCheck")
    public ResponseEntity<HttpStatus> prjAddCompDeplicateCheck(@RequestBody @Valid PrjDto prjDto) {
        prjService.prjAddCompDeplicateCheck(prjDto);
        if(prjDto.getDuplicateCheck().equals("이미 참여한 회사입니다")) {
            return new ResponseEntity("이미 참여한 회사입니다", HttpStatus.OK);
        }
        return ResponseEntity.ok().build();
    }
    /*--------------------------------------CREATE--------------------------------------*/
    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid PrjDto prjDto) {
        if (prjDto.getPname().equals("")) {
            throw new NullPointerException();
        } else if (prjDto.getPcontent().equals("")) {
            throw new NullPointerException();
        }
        prjService.create(prjDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/prjAddCompEmpLastStep")
    public ResponseEntity<HttpStatus> prjAddCompEmpLastStep(@RequestBody @Valid PrjDto prjDto) {
        prjService.prjAddCompEmpLastStep(prjDto);
        return ResponseEntity.ok().build();
    }

    /*--------------------------------------UPDATE--------------------------------------*/
    @GetMapping("/update/{pid}")
    public ResponseEntity<ListDto<PrjDto>> updatePrjStep(PrjDto prjDto) {
        return ResponseEntity.ok(prjService.updatePrjStep(prjDto));
    }

    @PutMapping()
    public ResponseEntity<HttpStatus> updatePrjTry(@RequestBody @Valid PrjDto prjDto) {
        prjService.updatePrjTry(prjDto);
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
    public ResponseEntity<HttpStatus> prjCompUnrealDelete(@RequestBody @Valid PrjDto prjDto) {
        prjService.prjCompUnrealDelete(prjDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/prjCompRealDelete")
    public ResponseEntity<HttpStatus> prjCompRealDelete(@RequestBody @Valid PrjDto prjDto) {
        prjService.prjCompRealDelete(prjDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/prjEmpUnrealDelete")
    public ResponseEntity<HttpStatus> prjEmpUnrealDelete(@RequestBody @Valid EmpDto empDto) {
        prjService.prjEmpUnrealDelete(empDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/prjEmpRealDelete")
    public ResponseEntity<HttpStatus> prjEmpRealDelete(@RequestBody @Valid EmpDto empDto) {
        prjService.prjEmpRealDelete(empDto);
        return ResponseEntity.ok().build();
    }
}
