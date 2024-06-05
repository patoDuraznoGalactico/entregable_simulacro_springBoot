package riwi.riwi.riwi_education.infraestructure.abstract_service;

import riwi.riwi.riwi_education.api.dto.request.AssignmentsRequest;
import riwi.riwi.riwi_education.api.dto.request.AssignmentsRequestUpdate;
import riwi.riwi.riwi_education.api.dto.response.AssignmentsResponse;

public interface IAssignmentService extends CrudService<AssignmentsRequest,AssignmentsResponse, Integer> {
    public AssignmentsResponse update(AssignmentsRequestUpdate request, Integer id);
    public String FIELD_BY_SORT = "assigmentTitle";
}