package riwi.riwi.riwi_education.infraestructure.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import riwi.riwi.riwi_education.api.dto.request.SubmissionsRequest;
import riwi.riwi.riwi_education.api.dto.response.*;
import riwi.riwi.riwi_education.domain.entities.Assignments;
import riwi.riwi.riwi_education.domain.entities.Courses;
import riwi.riwi.riwi_education.domain.entities.Submissions;
import riwi.riwi.riwi_education.domain.entities.Users;
import riwi.riwi.riwi_education.domain.repositories.AssignmentsRepository;
import riwi.riwi.riwi_education.domain.repositories.SubmissionsRepository;
import riwi.riwi.riwi_education.domain.repositories.UsersRepository;
import riwi.riwi.riwi_education.infraestructure.abstract_service.ISubmissionsService;
import riwi.riwi.riwi_education.utils.enums.SortType;
import riwi.riwi.riwi_education.utils.exceptions.BadRequestException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SubmissionService implements ISubmissionsService{

    @Autowired
    private final SubmissionsRepository submissionsRepository;

    @Autowired
    private final UsersRepository usersRepository;

    @Autowired
    private final AssignmentsRepository assignmentsRepository;

    @Override
    public SubmissionResponse create(SubmissionsRequest request) {
        Submissions submissions = this.requestToEntity(request);
        Assignments assignments = this.assignmentsRepository.findById(request.getAssignmentId()).orElseThrow(()-> new BadRequestException("tarea"));
        Users user = this.usersRepository.findById(request.getUserId()).orElseThrow(()-> new BadRequestException("usuario"));

        submissions.setAssignment(assignments);
        submissions.setUser(user);

        SubmissionResponse submissionResponse = new SubmissionResponse();
        submissionResponse = this.entityToResponse(this.submissionsRepository.save(submissions));
        return submissionResponse;

    }

    @Override
    public SubmissionResponse get(Integer id) {
        return this.entityToResponse(this.find(id));
    }

    @Override
    public SubmissionResponse update(SubmissionsRequest request, Integer id) {
        Submissions submissions = this.find(id);
        Assignments assignments = this.assignmentsRepository.findById(request.getAssignmentId()).orElseThrow(()-> new BadRequestException("tarea"));
        Users users = this.usersRepository.findById(request.getUserId()).orElseThrow(()-> new BadRequestException("usuario"));

        Submissions submissionUpdate = this.requestToEntity(request);
        submissionUpdate.setSubmissionId(id);
        submissionUpdate.setAssignment(assignments);
        submissionUpdate.setUser(users);

        SubmissionResponse submissionResponse = new SubmissionResponse();
        submissionResponse = this.entityToResponse(this.submissionsRepository.save(submissionUpdate));
        return submissionResponse;
    }

    @Override
    public void delete(Integer id) {
        Submissions submissions = this.find(id);
        this.submissionsRepository.delete(submissions);
    }

    @Override
    public Page<SubmissionResponse> getAll(int page, int size, SortType sort) {
        if(page<0) page = 0;
        PageRequest pagination = null;

        switch (sort){
            case NONE -> pagination = PageRequest.of(page, size);
            case ASC -> pagination = PageRequest.of(page,size, Sort.by(FIELD_BY_SORT).ascending());
            case DESC -> pagination = PageRequest.of(page,size, Sort.by(FIELD_BY_SORT).descending());
        }

        return this.submissionsRepository.findAll(pagination).map(this::entityToResponse);
    }

    private SubmissionResponse entityToResponse(Submissions entity){
        SubmissionResponse submissionResponse = new SubmissionResponse();
        BeanUtils.copyProperties(entity, submissionResponse);

        UsersBasicResponse usersResponse = new UsersBasicResponse();
        BeanUtils.copyProperties(entity.getUser(), usersResponse);

        AssignmentsBasicResponse assignmentsResponse = new AssignmentsBasicResponse();
        BeanUtils.copyProperties(entity.getAssignment(), assignmentsResponse);

        submissionResponse.setUser(usersResponse);
        submissionResponse.setAssignment(assignmentsResponse);
        return submissionResponse;
    }
    
    private Submissions requestToEntity(SubmissionsRequest request){
        Submissions submissions = new Submissions();
        BeanUtils.copyProperties(request,submissions);
        return submissions;
    }

    private Submissions find(Integer id){
        return this.submissionsRepository.findById(id)
                .orElseThrow(()-> new BadRequestException("envios"));
    }
    public List<SubmissionBasicResponse> listToBasic(List<Submissions> list) {
        List<SubmissionBasicResponse> submissionList = new ArrayList<>();
        submissionList = list.stream()
                .map(submissions -> {
                    SubmissionBasicResponse submissionBasicResponse = new SubmissionBasicResponse();
                    BeanUtils.copyProperties(submissions, submissionBasicResponse);
                    return submissionBasicResponse;
                })
                .collect(Collectors.toList());
        return submissionList;
    }
}
