package br.com.biblioteca.domain.user;


import br.com.biblioteca.core.ApplicationResponse;
import br.com.biblioteca.validations.groups.CreateValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;

@Tag(name = "User")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;


        @Tag(name="Create User")
        @PostMapping
        @Operation(summary = "Create a new user")
        public ResponseEntity<Void> createUser(
                @Validated(CreateValidation.class)
                @RequestBody UserDTO userDto) {
            User user = userMapper.toEntity(userDto);
            User savedEntity = userService.createUser(user);
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

    @Tag(name="Search Users with filter")
    @GetMapping
    @Operation(summary = "Search users with filters or all users")
    public ResponseEntity<ApplicationResponse<Page<UserDTO>>> searchUsers(
            @RequestParam(value = "role", required = false) String role,
            @RequestParam(value = "cpf", required = false) String cpf,
            @RequestParam(value = "email", required = false) String email,
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

    @Tag(name = "Update User")
    @Operation(summary = "Update an existing user")
    @PutMapping("/{id}")
    public ResponseEntity<ApplicationResponse<UserDTO>> updateUser(
            @PathVariable Long id,
            @RequestBody UserDTO userDto) {

        User user = userMapper.toEntity(userDto);
        User updatedUser = userService.updateUser(id, user);
        UserDTO updatedUserDto = userMapper.toDto(updatedUser);

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


    @Tag(name = "Delete User")
    @Operation(summary = "Delete a User by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}

