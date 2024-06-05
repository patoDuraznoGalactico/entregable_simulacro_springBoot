package riwi.riwi.riwi_education.infraestructure.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import riwi.riwi.riwi_education.api.dto.request.MessagesRequest;
import riwi.riwi.riwi_education.api.dto.response.*;
import riwi.riwi.riwi_education.domain.entities.Courses;
import riwi.riwi.riwi_education.domain.entities.Messages;
import riwi.riwi.riwi_education.domain.entities.Users;
import riwi.riwi.riwi_education.domain.repositories.CoursesRepository;
import riwi.riwi.riwi_education.domain.repositories.MessagesRepository;
import riwi.riwi.riwi_education.domain.repositories.UsersRepository;
import riwi.riwi.riwi_education.infraestructure.abstract_service.IMessageService;
import riwi.riwi.riwi_education.utils.enums.SortType;
import riwi.riwi.riwi_education.utils.exceptions.BadRequestException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MessageService implements IMessageService{

    @Autowired
    private final MessagesRepository messagesRepository;
    @Autowired
    private final UsersRepository usersRepository;
    @Autowired
    private final CoursesRepository coursesRepository;

    @Override
    public MessagesResponse create(MessagesRequest request) {
        Messages messages = this.requestToEntity(request);
        Users usersSender = this.usersRepository.findById(request.getUserSender()).orElseThrow(()-> new BadRequestException("envio"));
        Users usersReceiver = this.usersRepository.findById(request.getUserReceiver()).orElseThrow(()-> new BadRequestException("recibo"));
        Courses courses = this.coursesRepository.findById(request.getCourse()).orElseThrow(()-> new BadRequestException("curso"));

        messages.setUserSender(usersSender);
        messages.setUserReceiver(usersReceiver);
        messages.setCourse(courses);

        MessagesResponse messagesResponse = new MessagesResponse();
        messagesResponse = this.entityToResponse(this.messagesRepository.save(messages));
        return messagesResponse;
    }
    @Override
    public MessagesResponse get(Integer id) {
        return this.entityToResponse(this.find(id));
    }
    @Override
    public MessagesResponse update(MessagesRequest request, Integer id) {
        Messages messages = this.find(id);
        Users usersSender = this.usersRepository.findById(request.getUserSender()).orElseThrow(()-> new BadRequestException("envio"));
        Users usersReceiver = this.usersRepository.findById(request.getUserReceiver()).orElseThrow(()-> new BadRequestException("recibo"));
        Courses courses = this.coursesRepository.findById(request.getCourse()).orElseThrow(()-> new BadRequestException("curso"));

        Messages messagesUpdate = this.requestToEntity(request);
        messagesUpdate.setMessageId(id);
        messagesUpdate.setUserSender(usersSender);
        messagesUpdate.setUserReceiver(usersReceiver);
        messagesUpdate.setCourse(courses);

        MessagesResponse messagesResponse = new MessagesResponse();
        messagesResponse = this.entityToResponse(this.messagesRepository.save(messagesUpdate));
        return messagesResponse;
    }
    @Override
    public void delete(Integer id) {
        Messages messages = this.find(id);
        this.messagesRepository.delete(messages);
    }
    @Override
    public Page<MessagesResponse> getAll(int page, int size, SortType sort) {
        if(page<0) page = 0;
        PageRequest pagination = null;

        switch (sort){
            case NONE -> pagination = PageRequest.of(page, size);
            case ASC -> pagination = PageRequest.of(page,size, Sort.by(FIELD_BY_SORT).ascending());
            case DESC -> pagination = PageRequest.of(page,size, Sort.by(FIELD_BY_SORT).descending());
        }

        return this.messagesRepository.findAll(pagination).map(this::entityToResponse);
    }

    private MessagesResponse entityToResponse(Messages entity){
        MessagesResponse messagesResponse = new MessagesResponse();
        BeanUtils.copyProperties(entity, messagesResponse);

        UsersBasicResponse usersBasicSenderResponse = new UsersBasicResponse();
        BeanUtils.copyProperties(entity.getUserSender(), usersBasicSenderResponse);

        UsersBasicResponse usersRecieverBasicResponse = new UsersBasicResponse();
        BeanUtils.copyProperties(entity.getUserReceiver(), usersRecieverBasicResponse);

        UsersBasicResponse instructorResponse = new UsersBasicResponse();
        BeanUtils.copyProperties(entity.getCourse().getUserInstructor(), instructorResponse);
        CoursesBasicResponse coursesBasicResponse = new CoursesBasicResponse();
        BeanUtils.copyProperties(entity.getCourse(), coursesBasicResponse);
        coursesBasicResponse.setUserInstructor(instructorResponse);



        messagesResponse.setUserSender(usersBasicSenderResponse);
        messagesResponse.setUserReceiver(usersRecieverBasicResponse);
        messagesResponse.setCourse(coursesBasicResponse);
        return messagesResponse;
    }

    private Messages requestToEntity(MessagesRequest request){
        Messages messages = new Messages();
        BeanUtils.copyProperties(request,messages);
        return messages;
    }


    private Messages find(Integer id){
        return this.messagesRepository.findById(id)
                .orElseThrow(()-> new BadRequestException("mensajes"));
    }

    public List<MessagesBasicResponse> listToBasic(List<Messages> list) {
        List<MessagesBasicResponse> messagesBasicList = new ArrayList<>();
        messagesBasicList = list.stream()
                .map(messages -> {
                    MessagesBasicResponse messagesBasicResponse = new MessagesBasicResponse();
                    BeanUtils.copyProperties(messages, messagesBasicResponse);
                    return messagesBasicResponse;
                })
                .collect(Collectors.toList());
         return messagesBasicList;
    }

    public List<MessagesResponse> listToResponse(List<Messages> list) {
        List<MessagesResponse> messagesBasicList = new ArrayList<>();
        messagesBasicList = list.stream()
                .map(messages -> {
                    UsersBasicResponse usersBasicSent = new UsersBasicResponse();
                    BeanUtils.copyProperties(messages,usersBasicSent);

                    UsersBasicResponse usersBasicReceiver = new UsersBasicResponse();
                    BeanUtils.copyProperties(messages,usersBasicReceiver);

                    CoursesBasicResponse coursesBasic = new CoursesBasicResponse();
                    BeanUtils.copyProperties(messages,coursesBasic);

                    MessagesResponse messagesResponse = new MessagesResponse();
                    BeanUtils.copyProperties(messages, messagesResponse);
                    messagesResponse.setUserSender(usersBasicSent);
                    messagesResponse.setUserReceiver(usersBasicReceiver);
                    messagesResponse.setCourse(coursesBasic);
                    return messagesResponse;
                })
                .collect(Collectors.toList());
        return messagesBasicList;
    }

}
