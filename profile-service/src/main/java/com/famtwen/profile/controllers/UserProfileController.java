package com.famtwen.profile.controllers;

import com.famtwen.profile.dtos.request.ProfileCreationRequest;
import com.famtwen.profile.dtos.response.UserProfileResponse;
import com.famtwen.profile.services.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileController {
    UserProfileService userProfileService;

    @PostMapping("/users/")
    UserProfileResponse createProfile(@RequestBody ProfileCreationRequest request)
    {
        return userProfileService.createProfile(request);
    }


    @GetMapping("/users/{profileId}")
    UserProfileResponse getProfile(@PathVariable String profileId)
    {
        return userProfileService.getProfile(profileId);
    }

}
