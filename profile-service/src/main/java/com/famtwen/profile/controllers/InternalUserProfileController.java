package com.famtwen.profile.controllers;

import com.famtwen.profile.dtos.request.ProfileCreationRequest;
import com.famtwen.profile.dtos.response.UserProfileResponse;
import com.famtwen.profile.services.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalUserProfileController {
    UserProfileService userProfileService;

    @PostMapping("/internal/users")
    UserProfileResponse createProfile(@RequestBody ProfileCreationRequest request) {
        return userProfileService.createProfile(request);
    }


    @DeleteMapping("/internal/users/{profileId}")
    ResponseEntity<Void> deleteProfile(@PathVariable String profileId) {
        try {
            userProfileService.deleteProfile(profileId);
            return ResponseEntity.noContent()
                                 .build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .build();
        }
    }
}
