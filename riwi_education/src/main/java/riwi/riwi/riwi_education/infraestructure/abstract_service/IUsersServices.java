package riwi.riwi.riwi_education.infraestructure.abstract_service;

import riwi.riwi.riwi_education.api.dto.request.UsersRequest;
import riwi.riwi.riwi_education.api.dto.response.UsersResponse;


public interface IUsersServices extends CrudService<UsersRequest,UsersResponse, Integer> {
    public String FIELD_BY_SORT = "firstName";
}