package br.com.biblioteca.domain.user;

import br.com.biblioteca.core.ApplicationResponse;
import br.com.biblioteca.validations.groups.UpdateValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.io.IOException;
import java.net.URI;

@Tag(name = "User")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;


    @Tag(name = "Create User")
    @Operation(summary = "Create a new user")
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Void> createUser(
            @RequestPart("dto") UserDTO userDto,
            @RequestPart(value = "file", required = false) MultipartFile file,
            HttpServletRequest request) throws IOException {

        User user = userMapper.toEntity(userDto);
        User savedEntity = userService.createUser(user, file, request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedEntity.getId())
                .toUri();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(location)
                .build();
    }

    @Tag(name = "Search Users with filter")
    @GetMapping
    @Operation(summary = "Search users with filters or all users")
    public ResponseEntity<ApplicationResponse<Page<UserDTO>>> searchUsers(
            @RequestParam(value = "role", required = false) String role,
            @RequestParam(value = "cpf", required = false) String cpf,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "institution", required = false) String institution,
            @RequestParam(value = "course", required = false) String course,
            @RequestParam(value = "enabled", required = false) Boolean enabled,
            Pageable pageable) {

        Specification<User> specification = Specification.where(null);

        if (role != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(criteriaBuilder.lower(root.get("role")), role.toLowerCase()));
        }
        if (cpf != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("cpf"), cpf));
        }
        if (email != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
        }
        if (institution != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("institution")), "%" + institution.toLowerCase() + "%"));
        }
        if (course != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("course")), "%" + course.toLowerCase() + "%"));
        }
        if (enabled != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("enabled"), enabled));
        }

        Page<User> userPage = userService.searchUsers(specification, pageable);
        Page<UserDTO> userDTOPage = userMapper.toDto(userPage);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApplicationResponse.ofSuccess(userDTOPage));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Search User by ID")
    public ResponseEntity<ApplicationResponse<UserDTO>> findById(
            @PathVariable Long id
    ) {
        User user = userService.findById(id);
        UserDTO userDTO = userMapper.toDto(user);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApplicationResponse.ofSuccess(userDTO));
    }


    @PutMapping("/{id}")
    @Operation(summary = "Update an existing user")
    public ResponseEntity<ApplicationResponse<UserDTO>> updateUser(
            @PathVariable Long id,
            @Validated(UpdateValidation.class)
            @RequestBody UserDTO userDtoUpdates) {
        User userUpdated = userService.updateUser(id, user -> userMapper.mergeNonNull(userDtoUpdates, user));
        UserDTO updatedUserDto = userMapper.toDto(userUpdated);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApplicationResponse.ofSuccess(updatedUserDto));
    }

    @Tag(name = "Update User Status")
    @Operation(summary = "Update the status of a User")
    @PatchMapping("/{id}/enabled")
    public ResponseEntity<ApplicationResponse<String>> updateEnabled(
            @PathVariable Long id,
            @RequestParam String enabled) {

        Boolean status = Boolean.valueOf(enabled);

        User updatedUser = userService.disableUser(id, status);

        return ResponseEntity
                .status(HttpStatus.PARTIAL_CONTENT)
                .body(ApplicationResponse.ofSuccess(updatedUser.getEnabled().toString()));
    }

}
