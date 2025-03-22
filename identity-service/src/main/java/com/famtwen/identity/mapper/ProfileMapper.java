package com.famtwen.identity.mapper;

import com.famtwen.identity.dto.request.ProfileCreationRequest;
import com.famtwen.identity.dto.request.UserCreationRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileCreationRequest toProfileCreationRequest(UserCreationRequest request);
}
