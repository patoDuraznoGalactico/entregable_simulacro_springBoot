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
import riwi.riwi.riwi_education.api.dto.request.MessagesRequest;
import riwi.riwi.riwi_education.api.dto.response.MessagesResponse;
import riwi.riwi.riwi_education.infraestructure.abstract_service.IMessageService;
import riwi.riwi.riwi_education.utils.enums.SortType;

@RestController
@RequestMapping(path = "/messages")
@AllArgsConstructor
public class MessagesController {

    @Autowired
    private final IMessageService messageService;

    @GetMapping
    public ResponseEntity<Page<MessagesResponse>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader(required = false) SortType sortType
    ){
        if(Objects.isNull(sortType))
            sortType = SortType.NONE;
        return ResponseEntity.ok(this.messageService.getAll(page -1, size, sortType));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<MessagesResponse> get(
            @PathVariable int id
    ) {
        return ResponseEntity.ok(this.messageService.get(id));
    }

    @PostMapping
    public ResponseEntity<MessagesResponse> insert(
            @Validated @RequestBody MessagesRequest request
    ){
        return ResponseEntity.ok(this.messageService.create(request));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<MessagesResponse> update(
            @Validated @RequestBody MessagesRequest request,
            @PathVariable int id
    ) {
        return ResponseEntity.ok(this.messageService.update(request, id));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable int id
    ){
        this.messageService.delete(id);
        return ResponseEntity.noContent().build();
    }
}