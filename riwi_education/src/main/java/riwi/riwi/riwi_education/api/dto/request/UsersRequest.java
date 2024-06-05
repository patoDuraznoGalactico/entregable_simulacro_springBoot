package riwi.riwi.riwi_education.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import riwi.riwi.riwi_education.utils.enums.Role;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsersRequest {
    @NotBlank(message = "El user es requerido")
    @Size(
        min = 1, 
        max = 100, 
        message = "El nombre debe tener entre 1 y 100 caracteres"
    )
    private String userName;
    @NotBlank(message = "La contraseña es requerido")
    @Size(
        min = 1, 
        max = 100, 
        message = "La constraseña debe tener entre 1 y 100 caracteres"
    )
    private String password;
    @Size(
        min = 1, 
        max = 100, 
        message = "El email debe tener entre 1 y 100 caracteres"
    )
    @Email
    private String email;
    @NotBlank(message = "El nombre completo es requerido")
    @Size(
        min = 1, 
        max = 100, 
        message = "El nombre debe tener entre 1 y 100 caracteres"
    )
    private String fullName;
    @NotNull(message = "El rol  es requerido")
    private Role role;
}
