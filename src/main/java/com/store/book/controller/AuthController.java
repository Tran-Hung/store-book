package com.store.book.controller;

import com.store.book.DTO.JwtResponse;
import com.store.book.DTO.LoginRequest;
import com.store.book.DTO.MessageResponse;
import com.store.book.DTO.SignupRequest;
import com.store.book.Repository.*;
import com.store.book.component.JwtUtils;
import com.store.book.entity.AppUser;
import com.store.book.entity.Client;
import com.store.book.entity.UserRole;
import com.store.book.impl.UserDetailsImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private AppRoleRepository appRoleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private TypeClientRepository typeClientRepository;

    @Autowired
    private ClientRepository clientRepository;

    @PostMapping("/signin")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles));
    }
    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody SignupRequest signUpRequest) {
        if (appUserRepository.existsByUserName(signUpRequest.getUserName())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        AppUser user = signUpRequest.toUser(encoder);

        List<String> asignRoles = signUpRequest.getRole();
        List<UserRole> roles = new ArrayList<>();

        // Nếu không truyền thì set role mặc định là ROLE_USER
        if (asignRoles == null) {
            try {
            UserRole role = new UserRole();
            role.setAppUser(user);
            role.setAppRole(appRoleRepository.getById(2L));
            roles.add(role);
            } catch (RuntimeException e){
                throw new RuntimeException("Error: Role is not found.");
            }
        } else {
            asignRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                    case "ADMIN":
                        UserRole adminRole = new UserRole();
                        adminRole.setAppUser(user);
                        adminRole.setAppRole(appRoleRepository.getById(1L));
                        roles.add(adminRole);
                        break;
//                    case "mod":
//                        Role modRole = roleRepository.findByName(RoleEnum.ROLE_MODERATOR.getRole())
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                        roles.add(modRole);
//
//                        break;
                    default:
                        UserRole userRole = new UserRole();
                        userRole.setAppUser(user);
                        userRole.setAppRole(appRoleRepository.getById(2L));
                        roles.add(userRole);
                }
            });
        }
        user.setUserRole(roles);
        appUserRepository.save(user);
        Client client = signUpRequest.toClient();
        client.setTypeClient(typeClientRepository.getById(1L));
        String prefix = "KH";
        Integer id = clientRepository.getMaxRecord();
        if (id == null) id = 0;
        String generatedId = prefix + StringUtils.leftPad(String.valueOf(id + 1), 5, "0");
        client.setIdClient(generatedId);
        client.setIdAppUser(appUserRepository.getMaxRecord());
        clientRepository.save(client);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
