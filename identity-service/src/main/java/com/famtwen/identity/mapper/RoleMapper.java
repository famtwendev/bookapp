package com.famtwen.identity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.famtwen.identity.dto.request.RoleRequest;
import com.famtwen.identity.dto.response.RoleResponse;
import com.famtwen.identity.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
