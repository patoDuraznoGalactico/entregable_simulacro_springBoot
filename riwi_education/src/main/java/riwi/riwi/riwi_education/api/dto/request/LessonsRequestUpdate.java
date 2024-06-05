package riwi.riwi.riwi_education.api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LessonsRequestUpdate {
    @NotBlank(message = "El titulo de la leccion es requerido")
    @Size(
            min = 1,
            max = 100,
            message = "El titulo de la leccion debe tener entre 1 y 100 caracteres"
    )
    private String lessonTitle;
    @NotBlank(message = "El contenido es requerido")
    @Size(
            min = 1,
            message = "El contenido debe tener entre 1 y 100 caracteres"
    )
    private String content;
}
