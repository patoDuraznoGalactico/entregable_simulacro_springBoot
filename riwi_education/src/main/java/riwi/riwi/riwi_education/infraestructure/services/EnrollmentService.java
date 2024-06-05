package riwi.riwi.riwi_education.infraestructure.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import riwi.riwi.riwi_education.api.dto.request.EnrollmentRequest;
import riwi.riwi.riwi_education.api.dto.response.*;
import riwi.riwi.riwi_education.domain.entities.Courses;
import riwi.riwi.riwi_education.domain.entities.Enrollments;
import riwi.riwi.riwi_education.domain.entities.Messages;
import riwi.riwi.riwi_education.domain.entities.Users;
import riwi.riwi.riwi_education.domain.repositories.CoursesRepository;
import riwi.riwi.riwi_education.domain.repositories.EnrollmentsRepository;
import riwi.riwi.riwi_education.domain.repositories.UsersRepository;
import riwi.riwi.riwi_education.infraestructure.abstract_service.IEnrollmentService;
import riwi.riwi.riwi_education.utils.enums.Role;
import riwi.riwi.riwi_education.utils.enums.SortType;
import riwi.riwi.riwi_education.utils.exceptions.BadRequestException;
import riwi.riwi.riwi_education.utils.exceptions.RoleDenegateException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EnrollmentService implements IEnrollmentService{
    
    @Autowired
    private final EnrollmentsRepository enrollmentsRepository;

    @Autowired
    private final CoursesRepository coursesRepository;

    @Autowired
    private final UsersRepository usersRepository;


    @Override
    public EnrollmentResponse create(EnrollmentRequest request) {

        Enrollments enrollments = this.requestToEntity(request);
        Users user = this.usersRepository.findById(request.getUserId()).orElseThrow(()-> new BadRequestException("User"));
        Courses course = this.coursesRepository.findById(request.getCourseId()).orElseThrow(()-> new BadRequestException("Courses"));

        enrollments.setUser(user);
        enrollments.setCourse(course);

        EnrollmentResponse enrollmentResponse = new EnrollmentResponse();
        if (!enrollments.getUser().getRole().equals(Role.STUDENT)){
            throw new RoleDenegateException("estudiante");
        }else {
            enrollmentResponse = this.entityToResponse(this.enrollmentsRepository.save(enrollments));
         }
        return enrollmentResponse;
    }
    

    @Override
    public EnrollmentResponse get(Integer id) { 
        return this.entityToResponse(this.find(id));
    }

    @Override
    public EnrollmentResponse update(EnrollmentRequest request, Integer id) {
        Enrollments enrollments = this.find(id);
        Users user = this.usersRepository.findById(request.getUserId())
                        .orElseThrow(()-> new BadRequestException("User"));
        Courses course = this.coursesRepository.findById(request.getCourseId()) 
                        .orElseThrow(()-> new BadRequestException("Course"));

        Enrollments enrollmentsUpdate = this.requestToEntity(request);

        enrollmentsUpdate.setEnrollmentId(id);
        enrollmentsUpdate.setUser(user);
        enrollmentsUpdate.setCourse(course);

        EnrollmentResponse enrollmentResponse = new EnrollmentResponse();
        if (!enrollmentsUpdate.getUser().getRole().equals(Role.STUDENT)){
            throw new RoleDenegateException("estudiante");
        }else {
            enrollmentResponse = this.entityToResponse(this.enrollmentsRepository.save(enrollmentsUpdate));
        }
        return enrollmentResponse; 
    }

    @Override
    public void delete(Integer id) {
        Enrollments enrollments = this.find(id);
        this.enrollmentsRepository.delete(enrollments);
    }

    @Override
    public Page<EnrollmentResponse> getAll(int page, int size, SortType sort) {

        if(page<0) page = 0;
        PageRequest pagination = null;

        switch (sort){
            case NONE -> pagination = PageRequest.of(page, size);
            case ASC -> pagination = PageRequest.of(page,size, Sort.by(FIELD_BY_SORT).ascending());
            case DESC -> pagination = PageRequest.of(page,size, Sort.by(FIELD_BY_SORT).descending());
        }

        return this.enrollmentsRepository.findAll(pagination).map(this::entityToResponse);
    }

    private EnrollmentResponse entityToResponse(Enrollments entity){
        EnrollmentResponse enrollmentResponse = new EnrollmentResponse();
        BeanUtils.copyProperties(entity, enrollmentResponse);

        UsersBasicResponse userBasic = new UsersBasicResponse();
        BeanUtils.copyProperties(entity.getUser(), userBasic);

        CoursesBasicResponse courseBasic = new CoursesBasicResponse();
        BeanUtils.copyProperties(entity.getCourse(), courseBasic);

        UsersBasicResponse instructorBasic = new UsersBasicResponse();
        BeanUtils.copyProperties(entity.getCourse().getUserInstructor(), instructorBasic);
        
        courseBasic.setUserInstructor(instructorBasic);
        enrollmentResponse.setUser(userBasic);
        enrollmentResponse.setCourse(courseBasic);;

        return enrollmentResponse;

    }

    private Enrollments requestToEntity(EnrollmentRequest request){
        Enrollments enrollments = new Enrollments();
        BeanUtils.copyProperties(request, enrollments);
        return enrollments;
    }
    
    private Enrollments find(Integer id){
        return this.enrollmentsRepository.findById(id)
                .orElseThrow(()-> new BadRequestException("enrollments"));
    }

    public List<EnrollmentBasicResponse> listToBasic(List<Enrollments> list) {
        List<EnrollmentBasicResponse> enrollmentList = new ArrayList<>();
        enrollmentList = list.stream()
                .map(enrollments -> {
                    EnrollmentBasicResponse enrollmentBasicResponse = new EnrollmentBasicResponse();
                    BeanUtils.copyProperties(enrollments, enrollmentBasicResponse);
                    return enrollmentBasicResponse;
                })
                .collect(Collectors.toList());
        return enrollmentList;
    }
}
