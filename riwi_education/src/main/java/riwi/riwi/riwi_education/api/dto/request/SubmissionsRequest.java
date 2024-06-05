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
public class SubmissionsRequest {
    @NotBlank(message = "El nombre es requerido")
    @Size(
        min = 1, 
        max = 100, 
        message = "El nombre debe tener entre 1 y 100 caracteres"
    )    
    private String content;
    @NotNull(message = "La fecha es requeridas")
    private LocalDate date;
    @NotNull(message = "El grado requeridas")
    private Double grade;
    @NotNull(message = "El id del usuario es obligatorio")
    @Min(value = 1, message = "El id debe ser mayor a cero")
    private int userId;
    @NotNull(message = "El id de la es obligatoria")
    @Min(value = 1, message = "El id debe ser mayor a cero")
    private int assignmentId;
}
