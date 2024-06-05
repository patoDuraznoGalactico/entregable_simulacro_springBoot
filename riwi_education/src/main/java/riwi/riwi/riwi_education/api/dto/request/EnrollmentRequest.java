package riwi.riwi.riwi_education.api.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentRequest {
    @NotNull(message = "La fecha es requeridas")
    private LocalDate enrollmentDate;
    @NotNull(message = "El id del user es obligatorio")
    @Min(value = 1, message = "El id debe ser mayor a cero")
    private int userId;
    @NotNull(message = "El id del curso es obligatorio")
    @Min(value = 1, message = "El id debe ser mayor a cero")
    private int courseId;
}
