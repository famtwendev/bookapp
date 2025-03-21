package com.famtwen.profile.mappers;

import com.famtwen.profile.dtos.request.ProfileCreationRequest;
import com.famtwen.profile.dtos.response.UserProfileResponse;
import com.famtwen.profile.entities.UserProfile;
import org.mapstruct.Mapper;

//bao cho Mapstruct biet day la 1 bean va init
@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfile toUserProfile(ProfileCreationRequest request);
    UserProfileResponse toUserProfileResponse(UserProfile entity);
}
