package riwi.riwi.riwi_education.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;
import riwi.riwi.riwi_education.api.dto.request.UsersRequest;
import riwi.riwi.riwi_education.api.dto.response.UsersResponse;
import riwi.riwi.riwi_education.infraestructure.abstract_service.IUsersServices;
import riwi.riwi.riwi_education.utils.enums.SortType;

import java.util.Objects;


@RestController
@RequestMapping(path = "/user")
@AllArgsConstructor
public class UsersController {

    @Autowired
    private final IUsersServices userService;

    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    @GetMapping
    public ResponseEntity<Page<UsersResponse>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader(required = false) SortType sortType
    ) {
        if (Objects.isNull(sortType))
            sortType = SortType.NONE;
        return ResponseEntity.ok(this.userService.getAll(page - 1, size, sortType));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<UsersResponse> get(
            @PathVariable int id
    ) {
        return ResponseEntity.ok(this.userService.get(id));
    }

    @PostMapping
    public ResponseEntity<UsersResponse> insert(
            @Validated @RequestBody UsersRequest request) {
        System.out.println(request);
        return ResponseEntity.ok(this.userService.create(request));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<UsersResponse> update(
            @Validated @RequestBody UsersRequest request,
            @PathVariable int id
    ) {
        return ResponseEntity.ok(this.userService.update(request, id));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable int id
    ){
        this.userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}