package riwi.riwi.riwi_education.api.dto.response;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LessonsResponse {
    private int lessonId;
    private String lessonTitle;
    private String content;
    private CoursesBasicResponse course;
    private List<AssignmentsBasicResponse> assignments;
}
