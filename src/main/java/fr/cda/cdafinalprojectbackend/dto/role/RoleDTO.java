package fr.cda.cdafinalprojectbackend.dto.role;

import fr.cda.cdafinalprojectbackend.configuration.security.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoleDTO {
    private RoleEnum type;
}