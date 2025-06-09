package fr.cda.cdafinalprojectbackend.mapper;

import fr.cda.cdafinalprojectbackend.dto.role.RoleDTO;
import fr.cda.cdafinalprojectbackend.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {
    public RoleDTO toDTO(Role role) {
        if (role == null) {
            return null;
        }

        return new RoleDTO(
                role.getType()
        );
    }
}
