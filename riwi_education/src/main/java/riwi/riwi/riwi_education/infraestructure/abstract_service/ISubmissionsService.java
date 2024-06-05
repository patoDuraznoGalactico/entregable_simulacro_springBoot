package riwi.riwi.riwi_education.infraestructure.abstract_service;

import riwi.riwi.riwi_education.api.dto.request.SubmissionsRequest;
import riwi.riwi.riwi_education.api.dto.response.SubmissionResponse;

public interface ISubmissionsService extends CrudService<SubmissionsRequest,SubmissionResponse, Integer> {
    public String FIELD_BY_SORT = "submissionDate";
}
