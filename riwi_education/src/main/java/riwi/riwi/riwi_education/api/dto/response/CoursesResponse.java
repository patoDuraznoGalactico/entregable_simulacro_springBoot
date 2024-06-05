package riwi.riwi.riwi_education.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import riwi.riwi.riwi_education.domain.entities.Enrollments;
import riwi.riwi.riwi_education.domain.entities.Lessons;
import riwi.riwi.riwi_education.domain.entities.Messages;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoursesResponse {
    private int courseId;
    private String course_name;
    private String description;
    private UsersBasicResponse userInstructor;
    private List<EnrollmentBasicResponse> enrollments;
    private List<LessonsBasicResponse> lessons;
    private List<MessagesBasicResponse> messages;
}
