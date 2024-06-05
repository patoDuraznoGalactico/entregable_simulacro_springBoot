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
import riwi.riwi.riwi_education.api.dto.request.AssignmentsRequest;
import riwi.riwi.riwi_education.api.dto.request.AssignmentsRequestUpdate;
import riwi.riwi.riwi_education.api.dto.response.AssignmentsResponse;
import riwi.riwi.riwi_education.infraestructure.abstract_service.IAssignmentService;
import riwi.riwi.riwi_education.utils.enums.SortType;

@RestController
@RequestMapping(path = "/assignments")
@AllArgsConstructor
public class AssignmentController {

    @Autowired
    private final IAssignmentService assignmentService;

    @GetMapping
    public ResponseEntity<Page<AssignmentsResponse>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader(required = false) SortType sortType
    ){
        if(Objects.isNull(sortType))
            sortType = SortType.NONE;
        return ResponseEntity.ok(this.assignmentService.getAll(page -1, size, sortType));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<AssignmentsResponse> get(
            @PathVariable int id
    ) {
        return ResponseEntity.ok(this.assignmentService.get(id));
    }

    @PostMapping
    public ResponseEntity<AssignmentsResponse> insert(
            @Validated @RequestBody AssignmentsRequest request
            ){
        return ResponseEntity.ok(this.assignmentService.create(request));
    }

        @PutMapping(path = "/{id}")
    public ResponseEntity<AssignmentsResponse> update(
            @Validated @RequestBody AssignmentsRequestUpdate request,
            @PathVariable int id
    ) {
        return ResponseEntity.ok(this.assignmentService.update(request, id));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable int id
    ){
        this.assignmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
