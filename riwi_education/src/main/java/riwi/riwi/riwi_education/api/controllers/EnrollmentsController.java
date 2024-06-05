package riwi.riwi.riwi_education.api.controllers;
import lombok.AllArgsConstructor;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import riwi.riwi.riwi_education.api.dto.request.EnrollmentRequest;
import riwi.riwi.riwi_education.api.dto.response.EnrollmentResponse;
import riwi.riwi.riwi_education.infraestructure.abstract_service.IEnrollmentService;
import riwi.riwi.riwi_education.utils.enums.SortType;

@RestController
@RequestMapping(path = "/enrollments")
@AllArgsConstructor
public class EnrollmentsController {

    @Autowired
    protected final IEnrollmentService enrollmentService;

    @GetMapping
    public ResponseEntity<Page<EnrollmentResponse>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader(required = false) SortType sortType
    ){
        if(Objects.isNull(sortType))
            sortType = SortType.NONE;
        return ResponseEntity.ok(this.enrollmentService.getAll(page -1, size, sortType));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<EnrollmentResponse> get(
            @PathVariable int id
    ) {
        return ResponseEntity.ok(this.enrollmentService.get(id));
    }

    @PostMapping
    public ResponseEntity<EnrollmentResponse> insert(
            @Validated @RequestBody EnrollmentRequest request
    ){
        return ResponseEntity.ok(this.enrollmentService.create(request));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<EnrollmentResponse> update(
            @Validated @RequestBody EnrollmentRequest request,
            @PathVariable int id
    ) {
        return ResponseEntity.ok(this.enrollmentService.update(request, id));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable int id
    ){
        this.enrollmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

}