package com.store.book.controller;

import com.store.book.Repository.*;
import com.store.book.entity.AppUser;
import com.store.book.entity.Client;
import com.store.book.entity.UserRole;
import com.store.book.DTO.RegistrationForm;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/register")
public class RegistrationController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private AppUserRepository appUserRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AppRoleRepository appRoleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private TypeClientRepository typeClientRepository;

    @Autowired
    private ClientRepository clientRepository;

    public RegistrationController(
            AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String registerForm(Model model) {
        model.addAttribute("registerForm", new RegistrationForm());
        return "registration";
    }

    @PostMapping
    public String processRegistration(RegistrationForm form, Model model) {
        if (!appUserRepository.existsByUserName(form.getUserName())){
            AppUser user = form.toUser(passwordEncoder);
            UserRole role = new UserRole();
            role.setAppUser(user);
            role.setAppRole(appRoleRepository.getById(2L));
            List<UserRole> listRole = user.getUserRole();
            listRole.add(role);
            user.setUserRole(listRole);
            Client client = form.toClient();
            client.setTypeClient(typeClientRepository.getById(1L));
            String prefix = "KH";
            Integer id = clientRepository.getMaxRecord();
            if (id == null) id = 0;
            String generatedId = prefix + StringUtils.leftPad(String.valueOf(id + 1), 5, "0");
            client.setIdClient(generatedId);
            appUserRepository.save(user);
            client.setIdAppUser(appUserRepository.getMaxRecord());
            clientRepository.save(client);
        }else {
            model.addAttribute("error", "Account name is existed");
            model.addAttribute("registerForm", new RegistrationForm());
            return "registration";
        }

        return "redirect:/login";
    }
}