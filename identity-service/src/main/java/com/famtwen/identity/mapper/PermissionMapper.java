package com.famtwen.identity.mapper;

import org.mapstruct.Mapper;

import com.famtwen.identity.dto.request.PermissionRequest;
import com.famtwen.identity.dto.response.PermissionResponse;
import com.famtwen.identity.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
