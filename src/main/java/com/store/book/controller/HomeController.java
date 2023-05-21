package com.store.book.controller;

import com.store.book.Repository.AppRoleRepository;
import com.store.book.Repository.AppUserRepository;
import com.store.book.Repository.ClientRepository;
import com.store.book.Repository.UserRoleRepository;
import com.store.book.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AppRoleRepository appRoleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @GetMapping("/home")
    public String home(Model model, Principal principal) {
        String userName = principal.getName();
        AppUser user = appUserRepository.findByUserName(userName);
        Client client = clientRepository.findByIdAppUser(user.getUserId());
        UserRole role = userRoleRepository.findByAppUser(user);
        AppRole appRole = role.getAppRole();
        if (appRole.getRoleName().equals("ROLE_ADMIN")) {
            model.addAttribute("admin", appRole.getRoleName());
        } else {
            model.addAttribute("user", appRole.getRoleName());
        }
        model.addAttribute("client", client);
        return "homePage";
    }

    @GetMapping("/historyOrder/{idClient}")
    public String historyOrder(Model model, @PathVariable(value = "idClient") String idClient) {
        Client client = clientRepository.findByIdClient(idClient);
        List<Orders> orders = client.getOrder();
        List<Orders> list = new ArrayList<Orders>();
        for (Orders o : orders){
            if (o.getStatus().equals("D")) list.add(o);
        }
        model.addAttribute("orders", list);
        return "historyOrder";
    }
}
