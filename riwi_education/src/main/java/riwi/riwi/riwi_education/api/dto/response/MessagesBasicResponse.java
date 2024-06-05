package riwi.riwi.riwi_education.api.dto.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessagesBasicResponse {
    private int messageId;
    private String messageContent;
    private LocalDate sentDate;
}
