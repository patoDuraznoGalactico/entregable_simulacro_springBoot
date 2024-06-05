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
public class MessagesRequest {
    @NotBlank(message = "El contenido es requerido")
    @Size(
        min = 1, 
        max = 100, 
        message = "El contenido debe tener entre 1 y 100 caracteres"
    )    
    private String messageContent;
    @NotNull(message = "La fecha es requerida")    
    private LocalDate sentDate;
    @NotNull(message = "El id del remitente es obligatorio")
    @Min(value = 1, message = "El id debe ser mayor a cero")
    private int userSender;
    @NotNull(message = "El id del receptor es obligatorio")
    @Min(value = 1, message = "El id debe ser mayor a cero")
    private int userReceiver;
    @NotNull(message = "El id del curso es obligatorio")
    @Min(value = 1, message = "El id debe ser mayor a cero")
    private int course;
}
