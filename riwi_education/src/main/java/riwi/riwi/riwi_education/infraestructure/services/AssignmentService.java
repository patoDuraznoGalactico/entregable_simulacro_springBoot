package riwi.riwi.riwi_education.infraestructure.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import riwi.riwi.riwi_education.api.dto.request.AssignmentsRequest;
import riwi.riwi.riwi_education.api.dto.request.AssignmentsRequestUpdate;
import riwi.riwi.riwi_education.api.dto.request.LessonsRequest;
import riwi.riwi.riwi_education.api.dto.response.*;
import riwi.riwi.riwi_education.domain.entities.Assignments;
import riwi.riwi.riwi_education.domain.entities.Courses;
import riwi.riwi.riwi_education.domain.entities.Lessons;
import riwi.riwi.riwi_education.domain.entities.Submissions;
import riwi.riwi.riwi_education.domain.repositories.AssignmentsRepository;
import riwi.riwi.riwi_education.domain.repositories.CoursesRepository;
import riwi.riwi.riwi_education.domain.repositories.LessonsRepository;
import riwi.riwi.riwi_education.domain.repositories.SubmissionsRepository;
import riwi.riwi.riwi_education.infraestructure.abstract_service.IAssignmentService;
import riwi.riwi.riwi_education.utils.enums.SortType;
import riwi.riwi.riwi_education.utils.exceptions.BadRequestException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AssignmentService implements IAssignmentService {

    @Autowired
    private final AssignmentsRepository assignmentsRepository;
    @Autowired
    private final LessonsRepository lessonsRepository;
    @Autowired
    private final SubmissionService submissionService;
    
    @Override
    public AssignmentsResponse create(AssignmentsRequest request) {
        Assignments assignment =  this.requestToEntity(request);
        Lessons lesson = this.lessonsRepository.findById(request.getLessonId()).orElseThrow(()-> new BadRequestException("leccion"));

        assignment.setLesson(lesson);
        AssignmentsResponse assignmentsResponse = new AssignmentsResponse();

        assignmentsResponse = this.entityToResponse(this.assignmentsRepository.save(assignment));
        return assignmentsResponse;

    }

    @Override
    public AssignmentsResponse get(Integer id) {
        return this.entityToResponse(this.find(id));
    }

    @Override
    public AssignmentsResponse update(AssignmentsRequest request, Integer id) {
        Assignments assignments = this.find(id);
        Lessons lessons = this.lessonsRepository.findById(request.getLessonId()).orElseThrow(()-> new BadRequestException("leccion"));
        
        Assignments assignmentUpdate = this.requestToEntity(request);
        assignmentUpdate.setAssignmentId(id);
        assignmentUpdate.setLesson(lessons);

        AssignmentsResponse assignmentsResponse = new AssignmentsResponse();
        assignmentsResponse = this.entityToResponse(this.assignmentsRepository.save(assignmentUpdate));
        return assignmentsResponse;
    }

    @Override
    public void delete(Integer id) {
        Assignments assignments = this.find(id);
        this.assignmentsRepository.delete(assignments);
    }

    @Override
    public Page<AssignmentsResponse> getAll(int page, int size, SortType sort) {
        if(page<0) page = 0;
        PageRequest pagination = null;

        switch (sort){
            case NONE -> pagination = PageRequest.of(page, size);
            case ASC -> pagination = PageRequest.of(page,size, Sort.by(FIELD_BY_SORT).ascending());
            case DESC -> pagination = PageRequest.of(page,size, Sort.by(FIELD_BY_SORT).descending());
        }

        return this.assignmentsRepository.findAll(pagination).map(this::entityToResponse);
    }
    
    private AssignmentsResponse entityToResponse(Assignments entity){
        AssignmentsResponse assignmentsResponse = new AssignmentsResponse();
        BeanUtils.copyProperties(entity, assignmentsResponse);

        LessonsBasicResponse lessonsResponse = new LessonsBasicResponse();
        BeanUtils.copyProperties(entity.getLesson(), lessonsResponse);
        List<SubmissionBasicResponse> submissionBasicList = new ArrayList<>();
        assignmentsResponse.setSubmissions(submissionBasicList);
        assignmentsResponse.setLesson(lessonsResponse);

        if (entity.getSubmissions() != null) {
            assignmentsResponse.setSubmissions(this.submissionService.listToBasic(entity.getSubmissions()));
        }
        return assignmentsResponse;
    }

    private Assignments requestToEntity(AssignmentsRequest request){
        Assignments assignments = new Assignments();
        BeanUtils.copyProperties(request,assignments);
        return assignments;
    }

    
    private Assignments find(Integer id){
        return this.assignmentsRepository.findById(id)
                .orElseThrow(()-> new BadRequestException("tarea"));
    }

    @Override
    public AssignmentsResponse update(AssignmentsRequestUpdate request, Integer id) {
        Assignments assignments = this.find(id);

        BeanUtils.copyProperties(request,assignments);
        return this.entityToResponse(this.assignmentsRepository.save(assignments));
    }

    public List<AssignmentsBasicResponse> listToBasic(List<Assignments> list) {
        List<AssignmentsBasicResponse> assignmentsList = new ArrayList<>();
        assignmentsList = list.stream()
                .map(assignments-> {
                    AssignmentsBasicResponse assignmentsBasicResponse = new AssignmentsBasicResponse();
                    BeanUtils.copyProperties(assignments, assignmentsBasicResponse);
                    return assignmentsBasicResponse;
                })
                .collect(Collectors.toList());
        return assignmentsList;
    }


}
