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
import riwi.riwi.riwi_education.api.dto.request.LessonsRequest;
import riwi.riwi.riwi_education.api.dto.request.LessonsRequestUpdate;
import riwi.riwi.riwi_education.api.dto.response.*;
import riwi.riwi.riwi_education.domain.entities.Courses;
import riwi.riwi.riwi_education.domain.entities.Lessons;
import riwi.riwi.riwi_education.domain.repositories.CoursesRepository;
import riwi.riwi.riwi_education.domain.repositories.LessonsRepository;
import riwi.riwi.riwi_education.infraestructure.abstract_service.ILessonsService;
import riwi.riwi.riwi_education.utils.enums.SortType;
import riwi.riwi.riwi_education.utils.exceptions.BadRequestException;

@Service
@AllArgsConstructor
public class LessonsService implements ILessonsService{
    
    @Autowired
    private final LessonsRepository lessonsRepository;

    @Autowired
    private final CoursesRepository coursesRepository;

    @Autowired
    private final AssignmentService assignmentService;

    
    @Override
    public LessonsResponse create(LessonsRequest request) {
        Lessons lessons = this.requestToEntity(request);
        Courses courses = this.coursesRepository.findById(request.getCourseId()).orElseThrow(()-> new BadRequestException("curso"));

        lessons.setCourse(courses);
        LessonsResponse lessonsResponse = new LessonsResponse();

        lessonsResponse = this.entityToResponse(this.lessonsRepository.save(lessons));
         
        return lessonsResponse;
    }

    @Override
    public LessonsResponse get(Integer id) {
        return this.entityToResponse(this.find(id));
    }

    @Override
    public LessonsResponse update(LessonsRequest request, Integer id) {
        Lessons lessons = this.find(id);
        Courses courses = this.coursesRepository.findById(request.getCourseId()).orElseThrow(()-> new BadRequestException("curso"));

        Lessons lessonUpdate = this.requestToEntity(request);
        lessonUpdate.setLessonId(id);
        lessonUpdate.setCourse(courses);

        LessonsResponse lessonsResponse = new LessonsResponse();
        lessonsResponse = this.entityToResponse(this.lessonsRepository.save(lessonUpdate));
        return lessonsResponse;

    }

    @Override
    public void delete(Integer id) {
        Lessons lesson = this.find(id);
        this.lessonsRepository.delete(lesson);
    }

    @Override
    public Page<LessonsResponse> getAll(int page, int size, SortType sort) {
        if(page<0) page = 0;
        PageRequest pagination = null;

        switch (sort){
            case NONE -> pagination = PageRequest.of(page, size);
            case ASC -> pagination = PageRequest.of(page,size, Sort.by(FIELD_BY_SORT).ascending());
            case DESC -> pagination = PageRequest.of(page,size, Sort.by(FIELD_BY_SORT).descending());
        }

        return this.lessonsRepository.findAll(pagination).map(this::entityToResponse);
    }

    private LessonsResponse entityToResponse(Lessons entity){
        LessonsResponse lessonsResponse = new LessonsResponse();
        BeanUtils.copyProperties(entity, lessonsResponse);

        CoursesBasicResponse coursesBasic = new CoursesBasicResponse();
        BeanUtils.copyProperties(entity.getCourse(),coursesBasic);
        
        UsersBasicResponse usersBasicResponse = new UsersBasicResponse();
        BeanUtils.copyProperties(entity.getCourse().getUserInstructor(), usersBasicResponse);

        coursesBasic.setUserInstructor(usersBasicResponse);
        lessonsResponse.setCourse(coursesBasic);

        List<AssignmentsBasicResponse> assignmentsList = new ArrayList<>();
        lessonsResponse.setAssignments(assignmentsList);

        if (entity.getAssignment() != null) {
            lessonsResponse.setAssignments(this.assignmentService.listToBasic(entity.getAssignment()));
        }
        // lessonsResponse.setAssignments(assignmentsBasicResponses);
        return lessonsResponse;
    }    

    private Lessons requestToEntity(LessonsRequest request){
        Lessons lesson = new Lessons();
        BeanUtils.copyProperties(request,lesson);
        return lesson;
    }

    private Lessons find(Integer id){
        return this.lessonsRepository.findById(id)
                .orElseThrow(()-> new BadRequestException("courses"));
    }

    @Override
    public LessonsResponse update(LessonsRequestUpdate request, Integer id) {
        Lessons lessons = this.find(id);
        BeanUtils.copyProperties(request,lessons);

        return this.entityToResponse(this.lessonsRepository.save(lessons));
    }
    public List<LessonsBasicResponse> listToBasic(List<Lessons> list) {
        List<LessonsBasicResponse> lessonsList = new ArrayList<>();
        lessonsList = list.stream()
                .map(lessons-> {
                    LessonsBasicResponse lessonsBasicResponse = new LessonsBasicResponse();
                    BeanUtils.copyProperties(lessons, lessonsBasicResponse);
                    return lessonsBasicResponse;
                })
                .collect(Collectors.toList());
        return lessonsList;
    }
}
