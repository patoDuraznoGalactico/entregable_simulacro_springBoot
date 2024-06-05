package riwi.riwi.riwi_education.api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentsRequestUpdate {
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
}
