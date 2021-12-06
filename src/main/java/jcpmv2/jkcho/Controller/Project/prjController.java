package jcpmv2.jkcho.Controller.Project;

import jcpmv2.jkcho.Dto.*;
import jcpmv2.jkcho.Dto.Comp.CompPrjParticiDataDto;
import jcpmv2.jkcho.Dto.Emp.EmpTableDataDto;
import jcpmv2.jkcho.Dto.Project.*;
import jcpmv2.jkcho.Error.Model.*;
import jcpmv2.jkcho.Service.Project.PrjService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ListDto<PrjTableDataDto>> findAll(PagingDto pagingDto) { // 프로젝트 전체 검색
        // pagingDto에 페이징을 위한 pageNo값
        return ResponseEntity.ok(prjService.findAll(pagingDto));
    }

    @PostMapping("/listConditionSearch") // 프로젝트 조건 검색
    public ResponseEntity<ListDto<PrjTableDataDto>> listConditionSearch(@RequestBody @Valid PrjConditionSearchDataDto prjConditionSearchDataDto) {
        // prjConditionSearchDataDto에 조건, 검색어, pageNo
        return ResponseEntity.ok(prjService.listConditionSearch(prjConditionSearchDataDto));
    }

    @PostMapping("/{pid}")// 프로젝트 상세 모달창 오픈 전, pname+pcontent select
    public ResponseEntity<ListDto<PrjTableDataDto>> findPnameAndPcontent(@Valid PrjTableDataDto prjTableDataDto) {
        /*
        오류 해결을 위하여 @RequestBody 삭제
        Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported
        */
        return ResponseEntity.ok(prjService.findPnameAndPcontent(prjTableDataDto));
    }

    @PostMapping("/participationCompSearch") // 프로젝트에 참여중인 회사 목록 검색
    public ResponseEntity<ListDto<CompPrjParticiDataDto>> findParticipationComp(@RequestBody @Valid PrjIdDataDto prjIdDataDto) {
        /*
        오류 해결을 위하여 @RequestBody 삭제
        Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported
        */
        return ResponseEntity.ok(prjService.findParticipationComp(prjIdDataDto));
    }

    @PostMapping("/compParticiEmpSearch") // 회사에서 해당 프로젝트에 참여하고 있는 직원 목록을 검색
    public ResponseEntity<ListDto<EmpTableDataDto>> compParticiEmpSearch(@RequestBody @Valid PrjIdDataDto prjIdDataDto) {
        return ResponseEntity.ok(prjService.compParticiEmpSearch(prjIdDataDto));
    }

    @PostMapping("/prjAddCompDeplicateCheck") // 회사 참여 시도 시, 중복 체크
    public ResponseEntity<HttpStatus> prjAddCompDeplicateCheck(@RequestBody @Valid PrjDuplicateCheckDto prjDuplicateCheckDto) {
        prjService.prjAddCompDeplicateCheck(prjDuplicateCheckDto);
        return ResponseEntity.ok().build();
    }
    /*--------------------------------------CREATE--------------------------------------*/
    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid PrjTableDataDto prjTableDataDto) { // 프로젝트 신규 생성
        // 생성을 위해 입력받은 정보의 앞뒤 공백을 제거(Trim)
        prjTableDataDto.setPname(prjTableDataDto.getPname().trim());
        prjTableDataDto.setPcontent(prjTableDataDto.getPcontent().trim());
        // 필수 입력 사항의 유효성을 체크. 미입력시 프로젝트이름 Exception 발생
        if (prjTableDataDto.getPname().equals("")) {
            throw new PnameNullException();
        }
        prjService.create(prjTableDataDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/prjAddCompEmpLastStep") // 신규 회사 및 직원 & 직원 추가 등록
    public ResponseEntity<HttpStatus> prjAddCompEmpLastStep(@RequestBody @Valid PrjAddCompEmpDataDto prjAddCompEmpDataDto) {
        prjService.prjAddCompEmpLastStep(prjAddCompEmpDataDto);
        return ResponseEntity.ok().build();
    }

    /*--------------------------------------UPDATE--------------------------------------*/
    @GetMapping("/update/{pid}") // 업데이트 진행 전, 해당 프로젝트의 정보를 불러옴
    public ResponseEntity<ListDto<PrjTableDataDto>> updatePrjStep(PrjIdDataDto prjIdDataDto) {
        return ResponseEntity.ok(prjService.updatePrjStep(prjIdDataDto));
    }

    @PutMapping() // 프로젝트 정보 업데이트 실시
    public ResponseEntity<HttpStatus> updatePrjTry(@RequestBody @Valid PrjTableDataDto prjTableDataDto) {
        prjTableDataDto.setPname(prjTableDataDto.getPname().trim());
        prjTableDataDto.setPcontent(prjTableDataDto.getPcontent().trim());
        if(prjTableDataDto.getPname().equals("")) {
            throw new PnameNullException();
        }
        prjService.updatePrjTry(prjTableDataDto);
        return ResponseEntity.ok().build();
    }

    /*--------------------------------------DELETE--------------------------------------*/
    @PutMapping("/{pid}") // 프로젝트 가삭제
    public ResponseEntity<HttpStatus> unrealDelete(@PathVariable @Valid Long pid) {
        prjService.unrealDelete(pid);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{pid}") // 프로젝트 실삭제
    public ResponseEntity<HttpStatus> realDelete(@PathVariable @Valid Long pid) {
        prjService.realDelete(pid);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/prjCompUnrealDelete") // 프로젝트에서 해당 참여 회사를 가삭제
    public ResponseEntity<HttpStatus> prjCompUnrealDelete(@RequestBody @Valid PrjTableDataDto prjTableDataDto) {
        prjService.prjCompUnrealDelete(prjTableDataDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/prjCompRealDelete") // 프로젝트에서 해당 참여 회사를 실삭제
    public ResponseEntity<HttpStatus> prjCompRealDelete(@RequestBody @Valid PrjTableDataDto prjTableDataDto) {
        prjService.prjCompRealDelete(prjTableDataDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/prjEmpUnrealDelete") // 프로젝트에 참여하고 있는 회사의 해당 직원을 가삭제
    public ResponseEntity<HttpStatus> prjEmpUnrealDelete(@RequestBody @Valid PrjEidToDeleteDataDto prjEidToDeleteDataDto) {
        prjService.prjEmpUnrealDelete(prjEidToDeleteDataDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/prjEmpRealDelete") // 프로젝트에 참여하고 있는 회사의 해당 직원을 실삭제
    public ResponseEntity<HttpStatus> prjEmpRealDelete(@RequestBody @Valid PrjEidToDeleteDataDto prjEidToDeleteDataDto) {
        prjService.prjEmpRealDelete(prjEidToDeleteDataDto);
        return ResponseEntity.ok().build();
    }
}
