package riwi.riwi.riwi_education.api.controllers;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import riwi.riwi.riwi_education.api.dto.request.SubmissionsRequest;
import riwi.riwi.riwi_education.api.dto.response.SubmissionResponse;
import riwi.riwi.riwi_education.infraestructure.abstract_service.ISubmissionsService;
import riwi.riwi.riwi_education.utils.enums.SortType;

@RestController
@RequestMapping(path = "/submissions")
@AllArgsConstructor
public class SubmissionController {
    @Autowired
    private final ISubmissionsService submissionsService;

    @GetMapping
    public ResponseEntity<Page<SubmissionResponse>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader(required = false) SortType sortType
    ){
        if(Objects.isNull(sortType))
            sortType = SortType.NONE;
        return ResponseEntity.ok(this.submissionsService.getAll(page -1, size, sortType));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<SubmissionResponse> get(
            @PathVariable int id
    ) {
        return ResponseEntity.ok(this.submissionsService.get(id));
    }

    @PostMapping
    public ResponseEntity<SubmissionResponse> insert(
            @Validated @RequestBody SubmissionsRequest request
            ){
        return ResponseEntity.ok(this.submissionsService.create(request));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<SubmissionResponse> update(
            @Validated @RequestBody SubmissionsRequest request,
            @PathVariable int id
    ) {
        return ResponseEntity.ok(this.submissionsService.update(request, id));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable int id
    ){
        this.submissionsService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
