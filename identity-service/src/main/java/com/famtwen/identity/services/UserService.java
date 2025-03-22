package com.famtwen.identity.services;

import java.util.HashSet;
import java.util.List;

import com.famtwen.identity.mapper.ProfileMapper;
import com.famtwen.identity.repositories.httpclients.ProfileClient;
import feign.FeignException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.famtwen.identity.constants.PredefinedRole;
import com.famtwen.identity.dto.request.UserCreationRequest;
import com.famtwen.identity.dto.request.UserUpdateRequest;
import com.famtwen.identity.dto.response.UserResponse;
import com.famtwen.identity.entity.Role;
import com.famtwen.identity.entity.User;
import com.famtwen.identity.exceptions.AppException;
import com.famtwen.identity.exceptions.ErrorCode;
import com.famtwen.identity.mapper.UserMapper;
import com.famtwen.identity.repositories.RoleRepository;
import com.famtwen.identity.repositories.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    ProfileClient profileClient;
    ProfileMapper profileMapper;


    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE)
                      .ifPresent(roles::add);

        user.setRoles(roles);
        userRepository.save(user);
        // Khi save xuong mysql thi db da generate userId roi

        try {
            var profileRequest = profileMapper.toProfileCreationRequest(request);

            // Luu userId cua mysql vao profile
            profileRequest.setUserId(user.getId());

            var profileResponse = profileClient.createProfile(profileRequest);

        } catch (FeignException exception) {
            log.error("Error occurred while creating profile: {}", exception.getMessage());
            throw new RuntimeException("Failed to create profile", exception);
        }
        return userMapper.toUserResponse(user);
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication()
                             .getName();

        User user = userRepository.findByUsername(name)
                                  .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String userId) {
        profileClient.deleteProfile(userId);
        userRepository.deleteById(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        log.info("In method get Users");
        return userRepository.findAll()
                             .stream()
                             .map(userMapper::toUserResponse)
                             .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(
                userRepository.findById(id)
                              .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }
}
