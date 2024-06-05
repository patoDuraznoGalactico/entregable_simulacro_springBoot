package riwi.riwi.riwi_education.infraestructure.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import riwi.riwi.riwi_education.api.dto.request.CoursesRequest;
import riwi.riwi.riwi_education.api.dto.request.CoursesRequestUpdate;
import riwi.riwi.riwi_education.api.dto.response.*;
import riwi.riwi.riwi_education.domain.entities.Courses;
import riwi.riwi.riwi_education.domain.entities.Enrollments;
import riwi.riwi.riwi_education.domain.entities.Messages;
import riwi.riwi.riwi_education.domain.entities.Users;
import riwi.riwi.riwi_education.domain.repositories.CoursesRepository;
import riwi.riwi.riwi_education.domain.repositories.UsersRepository;
import riwi.riwi.riwi_education.infraestructure.abstract_service.ICoursesService;
import riwi.riwi.riwi_education.utils.enums.Role;
import riwi.riwi.riwi_education.utils.enums.SortType;
import riwi.riwi.riwi_education.utils.exceptions.BadRequestException;
import riwi.riwi.riwi_education.utils.exceptions.RoleDenegateException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CoursesService  implements ICoursesService {

    @Autowired
    private final CoursesRepository coursesRepository;

    @Autowired
    private final UsersRepository usersRepository;
    @Autowired
    private final EnrollmentService enrollmentService;
    @Autowired
    private final LessonsService lessonsService;
    @Autowired
    private final MessageService messageService;


    @Override
    public CoursesResponse create(CoursesRequest request) {

        Courses courses = this.requestToEntity(request);
        Users users = this.usersRepository.findById(request.getUserInstructor()).orElseThrow(()-> new BadRequestException("User"));

        courses.setUserInstructor(users);
        CoursesResponse coursesResponse = new CoursesResponse();
        if (!courses.getUserInstructor().getRole().equals(Role.INSTRUCTOR)){
            throw new RoleDenegateException("instructor");
        }else {
            coursesResponse = this.entityToResp(this.coursesRepository.save(courses));
         }
        return coursesResponse;
    }

    @Override
    public CoursesResponse get(Integer integer) {
        return this.entityToResp(this.find(integer));
    }

    @Override
    public CoursesResponse update(CoursesRequest request, Integer integer) {
        Courses courses = this.find(integer);
        Users users = this.usersRepository.findById(request.getUserInstructor()).orElseThrow(()-> new BadRequestException("User"));
        
        Courses coursesUpdate = this.requestToEntity(request);
        coursesUpdate.setCourseId(integer);
        coursesUpdate.setUserInstructor(users);
        CoursesResponse coursesResponse = new CoursesResponse();
        if (!coursesUpdate.getUserInstructor().getRole().equals(Role.INSTRUCTOR)){
            throw new RoleDenegateException("instructor");
        }else {
            coursesResponse = this.entityToResp(this.coursesRepository.save(coursesUpdate));
        }
        return coursesResponse;
    }

    @Override
    public void delete(Integer integer) {
        Courses courses = this.find(integer);
        this.coursesRepository.delete(courses);
    }

    @Override
    public Page<CoursesResponse> getAll(int page, int size, SortType sort) {

        if(page<0) page = 0;
        PageRequest pagination = null;

        switch (sort){
            case NONE -> pagination = PageRequest.of(page, size);
            case ASC -> pagination = PageRequest.of(page,size, Sort.by(FIELD_BY_SORT).ascending());
            case DESC -> pagination = PageRequest.of(page,size, Sort.by(FIELD_BY_SORT).descending());
        }

        return this.coursesRepository.findAll(pagination).map(this::entityToResp);
    }

    private CoursesResponse entityToResp(Courses entity){
        CoursesResponse coursesResponse = new CoursesResponse();
        BeanUtils.copyProperties(entity, coursesResponse);

        UsersBasicResponse usersBasic = new UsersBasicResponse();
        BeanUtils.copyProperties(entity.getUserInstructor(),usersBasic);
        coursesResponse.setUserInstructor(usersBasic);

        List<LessonsBasicResponse> lessonsBasicList = new ArrayList<>();
        List<EnrollmentBasicResponse> enrollmentBasicList = new ArrayList<>();
        List<MessagesBasicResponse> messagesBasicList = new ArrayList<>();
        coursesResponse.setMessages(messagesBasicList);
        coursesResponse.setLessons(lessonsBasicList);
        coursesResponse.setEnrollments(enrollmentBasicList);
        if (entity.getLessons() != null) {
            coursesResponse.setLessons(this.lessonsService.listToBasic(entity.getLessons()));
        }
        if (entity.getEnrollments()!= null) {
            coursesResponse.setEnrollments(this.enrollmentService.listToBasic(entity.getEnrollments()));
        }
        if (entity.getMessages()!= null) {
            coursesResponse.setMessages(this.messageService.listToBasic(entity.getMessages()));
        }
        return coursesResponse;
    }

    private Courses requestToEntity(CoursesRequest request){
        Courses courses = new Courses();
        BeanUtils.copyProperties(request,courses);
        return courses;
    }
    private Courses find(Integer id){
        return this.coursesRepository.findById(id)
                .orElseThrow(()-> new BadRequestException("courses"));
    }

    @Override
    public CoursesResponse update(CoursesRequestUpdate request, Integer integer) {
        Courses courses = this.find(integer);

        BeanUtils.copyProperties(request,courses);
        return this.entityToResp(this.coursesRepository.save(courses));
    }

    public List<CoursesBasicResponse> listToBasic(List<Courses> list) {
        List<CoursesBasicResponse> coursesList = new ArrayList<>();
        coursesList = list.stream()
                .map(courses -> {
                    CoursesBasicResponse coursesBasicResponse = new CoursesBasicResponse();
                    BeanUtils.copyProperties(courses, coursesBasicResponse);
                    return coursesBasicResponse;
                })
                .collect(Collectors.toList());
        return coursesList;
    }
    public List<MessagesResponse> getMessagesById(Integer id) {
        Courses courses = this.find(id);
        List<MessagesResponse> list = new ArrayList<>();
        if (courses.getMessages()!= null) {
            return this.messageService.listToResponse(courses.getMessages());
        }
        System.out.println(courses.getMessages());
        System.out.println(list);
        return list;
    }


}
