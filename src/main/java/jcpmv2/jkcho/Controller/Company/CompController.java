package jcpmv2.jkcho.Controller.Company;

import jcpmv2.jkcho.Dto.*;
import jcpmv2.jkcho.Service.Company.CompService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/company/compController")
public class CompController {
    @Autowired
    private CompService compService;

    /*--------------------------------------SELECT--------------------------------------*/
    @GetMapping("/{pageNo}")
    public ResponseEntity<ListDto<CompDto>> findAll(PagingDto pagingDto) {
        return ResponseEntity.ok(compService.findAll(pagingDto));
    }

    @PostMapping("/listConditionSearch")
    public ResponseEntity<ListDto<CompDto>> listConditionSearch(@RequestBody @Valid CompDto compDto) {
        return ResponseEntity.ok(compService.listConditionSearch(compDto));
    }

    /*--------------------------------------CREATE--------------------------------------*/
    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid CompDto compDto) {
        if (compDto.getCname().equals("")) {
            throw new NullPointerException();
        } else if (compDto.getCboss().equals("")) {
            throw new NullPointerException();
        } else if (compDto.getCcall().equals("")) {
            throw new NullPointerException();
        } else if (compDto.getCnumber().equals("")) {
            throw new NullPointerException();
        }
        compService.create(compDto);
        return ResponseEntity.ok().build();
    }

    /*--------------------------------------UPDATE--------------------------------------*/
    @GetMapping("/updateReady/{cid}")
    public ResponseEntity<ListDto<CompDto>> compUpdateReady(CompDto compDto){
        return ResponseEntity.ok(compService.compUpdateReady(compDto));
    }

    @PutMapping()
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid CompDto compDto) {
        compService.compUpdateDirtyChecking(compDto);
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
