package com.famtwen.identity.repositories.httpclients;

import com.famtwen.identity.configuration.Authenticationrequestinterceptor;
import com.famtwen.identity.dto.request.ApiResponse;
import com.famtwen.identity.dto.request.ProfileCreationRequest;
import com.famtwen.identity.dto.response.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "profile-service", url = "${app.services.profile}",
        configuration = {Authenticationrequestinterceptor.class})
public interface ProfileClient {
    @PostMapping(value = "/internal/users", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<UserProfileResponse> createProfile(@RequestBody ProfileCreationRequest request);

    @DeleteMapping(value = "/internal/users/{profileId}")
    void deleteProfile(@PathVariable("profileId") String profileId);
}
