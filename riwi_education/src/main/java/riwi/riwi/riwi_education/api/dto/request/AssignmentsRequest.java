package riwi.riwi.riwi_education.api.dto.request;

import java.time.LocalDate;
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
public class AssignmentsRequest {
    @NotBlank(message = "El titulo es requerido")
    @Size(
        min = 1, 
        max = 100, 
        message = "El titulo debe tener entre 1 y 100 caracteres"
    )
    private String title;
    @NotBlank(message = "La descripcion es requerida")
    @Size(
        min = 1, 
        max = 100, 
        message = "El descricpion debe tener entre 1 y 100 caracteres"
    )    
    private String description;
    @NotNull(message = "La fecha es requeridas")    
    private LocalDate dueDate;
    @NotNull(message = "El id de la leccion es obligatorio")
    @Min(value = 1, message = "El id debe ser mayor a cero")
    private int lessonId;
}
