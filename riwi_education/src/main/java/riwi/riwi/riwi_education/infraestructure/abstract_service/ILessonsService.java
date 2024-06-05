package riwi.riwi.riwi_education.infraestructure.abstract_service;

import riwi.riwi.riwi_education.api.dto.request.LessonsRequest;
import riwi.riwi.riwi_education.api.dto.request.LessonsRequestUpdate;
import riwi.riwi.riwi_education.api.dto.response.LessonsResponse;

public interface ILessonsService extends CrudService<LessonsRequest,LessonsResponse, Integer> {
    public LessonsResponse update(LessonsRequestUpdate request, Integer id);
    public String FIELD_BY_SORT = "lessonTitle";
}

