package riwi.riwi.riwi_education.infraestructure.abstract_service;

import riwi.riwi.riwi_education.api.dto.request.EnrollmentRequest;
import riwi.riwi.riwi_education.api.dto.response.EnrollmentResponse;

public interface IEnrollmentService extends CrudService<EnrollmentRequest,EnrollmentResponse, Integer> {
    public String FIELD_BY_SORT = "enrollmentDate";
}