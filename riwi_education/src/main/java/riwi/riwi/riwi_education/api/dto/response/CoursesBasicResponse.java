package riwi.riwi.riwi_education.api.dto.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoursesBasicResponse {
    private int courseId;
    private String course_name;
    private String description;
    private UsersBasicResponse userInstructor;
}