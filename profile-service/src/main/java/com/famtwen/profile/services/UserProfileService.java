package com.famtwen.profile.services;

import com.famtwen.profile.dtos.request.ProfileCreationRequest;
import com.famtwen.profile.dtos.response.UserProfileResponse;
import com.famtwen.profile.entities.UserProfile;
import com.famtwen.profile.exception.AppException;
import com.famtwen.profile.exception.ErrorCode;
import com.famtwen.profile.mappers.UserProfileMapper;
import com.famtwen.profile.repositories.UserProfileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserProfileService {
    UserProfileRepository userProfileRepository;

    UserProfileMapper userProfileMapper;

    public UserProfileResponse createProfile(ProfileCreationRequest request) {
        UserProfile userProfile = userProfileMapper.toUserProfile(request);

        userProfile = userProfileRepository.save(userProfile);

        return userProfileMapper.toUserProfileResponse(userProfile);
    }


    @PreAuthorize("hasRole('ADMIN')")
    public UserProfileResponse getProfile(String id) {
        UserProfile userProfile = userProfileRepository.findById(id)
                                                       .orElseThrow(() -> new RuntimeException("Profile not exsits"));
        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    public UserProfileResponse getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext()
                                                             .getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();

        String userId = jwt.getClaim("userId")
                           .toString();

        if (!StringUtils.hasText(userId)) {
            throw new IllegalArgumentException("Invalid User ID in JWT token");
        }


        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                                                       .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userProfileMapper.toUserProfileResponse(userProfile);
    }


    @PreAuthorize("hasRole('ADMIN')")
    public List<UserProfileResponse> getAllProfiles() {
        var profiles = userProfileRepository.findAll();

        return profiles.stream()
                       .map(userProfileMapper::toUserProfileResponse)
                       .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProfile(String userId) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                                                       .orElseThrow(() -> new RuntimeException("Profile not exsits"));
        userProfileRepository.deleteById(userProfile.getId());
    }
}
