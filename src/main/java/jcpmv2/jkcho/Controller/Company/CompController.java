package jcpmv2.jkcho.Controller.Company;

import jcpmv2.jkcho.Dto.*;
import jcpmv2.jkcho.Dto.Comp.*;
import jcpmv2.jkcho.Service.Company.CompService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/company/compController")
public class CompController {
    @Autowired
    private CompService compService;

    /*--------------------------------------SELECT--------------------------------------*/
    @GetMapping("/{pageNo}")
    public ResponseEntity<ListDto<CompTableDataDto>> findAll(PagingDto pagingDto) {
        return ResponseEntity.ok(compService.findAll(pagingDto));
    }

    @PostMapping("/listConditionSearch")
    public ResponseEntity<ListDto<CompTableDataDto>> listConditionSearch(@RequestBody @Valid CompConditionSearchDataDto compConditionSearchDataDto) {
        return ResponseEntity.ok(compService.listConditionSearch(compConditionSearchDataDto));
    }

    /*--------------------------------------CREATE--------------------------------------*/
    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid CompTableDataDto compTableDataDto) {
        if (compTableDataDto.getCname().equals("")) {
            throw new NullPointerException();
        } else if (compTableDataDto.getCboss().equals("")) {
            throw new NullPointerException();
        } else if (compTableDataDto.getCcall().equals("")) {
            throw new NullPointerException();
        } else if (compTableDataDto.getCnumber().equals("")) {
            throw new NullPointerException();
        }
        compService.create(compTableDataDto);
        return ResponseEntity.ok().build();
    }

    /*--------------------------------------UPDATE--------------------------------------*/
    @GetMapping("/updateReady/{cid}")
    public ResponseEntity<ListDto<CompTableDataDto>> compUpdateReady(CompTableDataDto compTableDataDto){
        return ResponseEntity.ok(compService.compUpdateReady(compTableDataDto));
    }

    @PutMapping()
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid CompTableDataDto compTableDataDto) {
        compService.update(compTableDataDto);
        return ResponseEntity.ok().build();
    }

    /*--------------------------------------DELETE--------------------------------------*/
    @PutMapping("/{cid}")
    public ResponseEntity<HttpStatus> unrealDelete(@PathVariable @Valid Long cid) {
        compService.unrealDelete(cid);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{cid}")
    public ResponseEntity<HttpStatus> realDelete(@PathVariable @Valid Long cid) {
        compService.realDelete(cid);
        return ResponseEntity.ok().build();
    }
}