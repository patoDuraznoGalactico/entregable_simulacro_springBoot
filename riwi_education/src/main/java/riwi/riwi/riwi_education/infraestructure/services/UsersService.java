package riwi.riwi.riwi_education.infraestructure.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import riwi.riwi.riwi_education.api.dto.request.UsersRequest;
import riwi.riwi.riwi_education.api.dto.response.*;
import riwi.riwi.riwi_education.domain.entities.*;
import riwi.riwi.riwi_education.domain.repositories.UsersRepository;
import riwi.riwi.riwi_education.infraestructure.abstract_service.IMessageService;
import riwi.riwi.riwi_education.infraestructure.abstract_service.IUsersServices;
import riwi.riwi.riwi_education.utils.enums.SortType;
import riwi.riwi.riwi_education.utils.exceptions.BadRequestException;

@Service
@AllArgsConstructor
public class UsersService implements IUsersServices{

    @Autowired
    private final UsersRepository usersRepository;

    @Autowired
    private final MessageService messageService;
    @Autowired
    private final CoursesService coursesService;
    @Autowired
    private final EnrollmentService enrollmentService;
    @Autowired
    private final SubmissionService submissionService;
    
    @Override
    public UsersResponse create(UsersRequest request) {
        Users user = this.requestToEntity(request);
        return this.entityToResp(this.usersRepository.save(user));
    }

    @Override
    public UsersResponse get(Integer id) {
        return this.entityToResp(this.find(id));
    }

    @Override
    public UsersResponse update(UsersRequest request, Integer id) {
        Users user = this.find(id);
        Users userUpdate = this.requestToEntity(request);
        userUpdate.setUserId(id);
        return this.entityToResp(this.usersRepository.save(userUpdate));
    }

    @Override
    public void delete(Integer id) {
        Users user = this.find(id);
        this.usersRepository.delete(user);
    }

    @Override
    public Page<UsersResponse> getAll(int page, int size, SortType sort) {

        if(page<0) page = 0;
        PageRequest pagination= null;

        switch (sort){
            case NONE -> pagination = PageRequest.of(page, size);
            case ASC -> pagination = PageRequest.of(page,size, Sort.by(FIELD_BY_SORT).ascending());
            case DESC -> pagination = PageRequest.of(page,size, Sort.by(FIELD_BY_SORT).descending());
        }
        return this.usersRepository.findAll(pagination).map(this::entityToResp);
    }


    private UsersResponse entityToResp(Users entity) {
        UsersResponse usersResponse = new UsersResponse();
        BeanUtils.copyProperties(entity, usersResponse);

        List<CoursesBasicResponse> coursesBasicList = new ArrayList<>();
        List<EnrollmentBasicResponse> enrollmentsBasicList = new ArrayList<>();
        List<SubmissionBasicResponse> submissionBasicList = new ArrayList<>();
        List<MessagesBasicResponse> messagesBasicList = new ArrayList<>();
        usersResponse.setSentMessages(messagesBasicList);
        usersResponse.setRecieverMessages(messagesBasicList);
        usersResponse.setSubmissions(submissionBasicList);
        usersResponse.setCourses(coursesBasicList);
        usersResponse.setEnrollments(enrollmentsBasicList);

        if (entity.getCourses() != null) {
            usersResponse.setCourses(this.coursesService.listToBasic(entity.getCourses()));
        }
        if (entity.getEnrollments() != null) {
            usersResponse.setEnrollments(this.enrollmentService.listToBasic(entity.getEnrollments()));
        }
        if (entity.getSubmissions() != null) {
            usersResponse.setSubmissions(this.submissionService.listToBasic(entity.getSubmissions()));
        }
        if (entity.getSentMessages() != null) {
            usersResponse.setSentMessages(this.messageService.listToBasic(entity.getSentMessages()));
        }
        if (entity.getReceivedMessages() != null) {
            usersResponse.setRecieverMessages(this.messageService.listToBasic(entity.getReceivedMessages()));
        }
        return usersResponse;
    }

     private Users requestToEntity(UsersRequest user) {
         Users users = new Users();
         BeanUtils.copyProperties(user,users);
         return users;
    }

    private Users find(Integer id) {
        return this.usersRepository.findById(id)
                    .orElseThrow(()-> new BadRequestException("Users"));
    }
    
}
