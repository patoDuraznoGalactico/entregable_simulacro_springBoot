package riwi.riwi.riwi_education.api.dto.response;
import riwi.riwi.riwi_education.domain.entities.Submissions;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentsResponse {
    private int assignmentId;
    private String title;   
    private String description;
    private LocalDate dueDate;
    private LessonsBasicResponse lesson;
    private List<SubmissionBasicResponse> Submissions;

}
