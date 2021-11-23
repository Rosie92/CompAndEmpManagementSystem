package jcpmv2.jkcho.Controller.Project;

import jcpmv2.jkcho.Dto.*;
import jcpmv2.jkcho.Service.Project.PrjService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
            prjDto.setDuplicateCheck("중복체크확인");
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
}
