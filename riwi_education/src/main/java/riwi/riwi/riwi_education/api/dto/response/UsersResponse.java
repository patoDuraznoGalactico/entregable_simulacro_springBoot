package riwi.riwi.riwi_education.api.dto.response;

import java.util.List;

import riwi.riwi.riwi_education.domain.entities.Courses;
import riwi.riwi.riwi_education.domain.entities.Enrollments;
import riwi.riwi.riwi_education.domain.entities.Messages;
import riwi.riwi.riwi_education.domain.entities.Submissions;
import riwi.riwi.riwi_education.utils.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsersResponse {
    private int userId;
    private String userName;
    private String password;
    private String email;
    private String fullName;
    private Role role;
    private List<CoursesBasicResponse> courses;
    private List<EnrollmentBasicResponse> enrollments;
    private List<SubmissionBasicResponse> submissions;
    private List<MessagesBasicResponse> sentMessages;
    private List<MessagesBasicResponse> recieverMessages;
}
