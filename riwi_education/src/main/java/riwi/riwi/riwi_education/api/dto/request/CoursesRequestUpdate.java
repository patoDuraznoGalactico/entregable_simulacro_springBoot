package riwi.riwi.riwi_education.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoursesRequestUpdate {
    @NotBlank(message = "El nombre del curso es requerido")
    @Size(
        min = 1, 
        max = 100, 
        message = "El nombre del curso debe tener entre 1 y 100 caracteres"
    )
    private String course_name;
    @NotBlank(message = "La descripcion es requerida")
    @Size(
        min = 1, 
        max = 100, 
        message = "La descripcion debe tener entre 1 y 100 caracteres"
    )
    private String description;
}
