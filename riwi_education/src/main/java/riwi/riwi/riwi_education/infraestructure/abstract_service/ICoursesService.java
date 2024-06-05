package riwi.riwi.riwi_education.infraestructure.abstract_service;

import riwi.riwi.riwi_education.api.dto.request.CoursesRequest;
import riwi.riwi.riwi_education.api.dto.request.CoursesRequestUpdate;
import riwi.riwi.riwi_education.api.dto.response.CoursesResponse;
import riwi.riwi.riwi_education.api.dto.response.MessagesBasicResponse;
import riwi.riwi.riwi_education.api.dto.response.MessagesResponse;
import riwi.riwi.riwi_education.domain.entities.Messages;

import java.util.List;

public interface ICoursesService extends CrudService<CoursesRequest,CoursesResponse, Integer> {
    public CoursesResponse update(CoursesRequestUpdate request, Integer integer);
    public List<MessagesResponse> getMessagesById(Integer id);
    public String FIELD_BY_SORT = "courseName";
}
