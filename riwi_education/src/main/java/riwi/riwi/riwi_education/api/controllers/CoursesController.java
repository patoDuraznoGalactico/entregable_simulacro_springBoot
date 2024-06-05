package riwi.riwi.riwi_education.api.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import riwi.riwi.riwi_education.api.dto.request.CoursesRequest;
import riwi.riwi.riwi_education.api.dto.request.CoursesRequestUpdate;
import riwi.riwi.riwi_education.api.dto.response.CoursesResponse;
import riwi.riwi.riwi_education.api.dto.response.MessagesBasicResponse;
import riwi.riwi.riwi_education.api.dto.response.MessagesResponse;
import riwi.riwi.riwi_education.infraestructure.abstract_service.ICoursesService;
import riwi.riwi.riwi_education.utils.enums.SortType;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "/courses")
@AllArgsConstructor
public class CoursesController {

    @Autowired
    private final ICoursesService coursesServices;

    @GetMapping
    public ResponseEntity<Page<CoursesResponse>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader(required = false) SortType sortType
    ){
        System.out.println("eeeee");
        if(Objects.isNull(sortType))
            sortType = SortType.NONE;
        return ResponseEntity.ok(this.coursesServices.getAll(page -1, size, sortType));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CoursesResponse> get(
            @PathVariable int id
    ) {
        return ResponseEntity.ok(this.coursesServices.get(id));
    }

    @PostMapping
    public ResponseEntity<CoursesResponse> insert(
            @Validated @RequestBody CoursesRequest request
            ){
        return ResponseEntity.ok(this.coursesServices.create(request));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<CoursesResponse> update(
            @Validated @RequestBody CoursesRequestUpdate request,
            @PathVariable int id
    ) {
        return ResponseEntity.ok(this.coursesServices.update(request, id));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable int id
    ){
        this.coursesServices.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/{id}/messages")
    public ResponseEntity<List<MessagesResponse>> getMessagesById(
            @PathVariable int id
    ) {
        return ResponseEntity.ok(this.coursesServices.getMessagesById(id));
    }
}
