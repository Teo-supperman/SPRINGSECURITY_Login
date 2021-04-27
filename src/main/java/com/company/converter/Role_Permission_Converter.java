package com.company.converter;

import org.springframework.stereotype.Component;

import com.company.dto.Permission_dto;
import com.company.dto.Role_Permission_dto;
import com.company.model.Account.Permission;
import com.company.model.Account.Role_Permission;

@Component
public class Role_Permission_Converter {
	public Role_Permission role(Role_Permission_dto R_P_dto) {
		Role_Permission R_P = new Role_Permission();
		
		return R_P;
	}

	public Permission_dto permission_dto(Permission permission) {
		Permission_dto permission_dto = new Permission_dto();
		permission_dto.setIdPermission(permission.getIdPermission());
		permission_dto.setName(permission.getName());
		return permission_dto;
	}

	public Permission permission(Permission_dto permission_dto, Permission permission) {
		permission.setIdPermission(permission_dto.getIdPermission());
		permission.setName(permission_dto.getName());
		return permission;
	}
}
