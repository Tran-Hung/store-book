package com.store.book.controller;

import com.store.book.Repository.ClientRepository;
import com.store.book.Repository.TypeClientRepository;
import com.store.book.entity.TypeClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/typeClient")
public class TypeClientController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    TypeClientRepository typeClientRepository;

    @RequestMapping(value="/list", method= RequestMethod.GET)
    public String listClient( ModelMap model) {
        List<TypeClient> typeClients = typeClientRepository.findAll();
        if (typeClients != null) {
            model.addAttribute("typeClients", typeClients);
        }
        return "listTypeClient";
    }

    @GetMapping(value = {"/create-edit", "/create-edit/{id}"})
    public String create(Model model, @PathVariable(value = "id", required = false) Long id) {
        if (id == null)
            model.addAttribute("typeClient", new TypeClient());
        else
            model.addAttribute("typeClient", typeClientRepository.getById(id));
        return "createType";
    }

    @PostMapping("/save")
    public String save(TypeClient typeClient, RedirectAttributes redirect) {
        typeClientRepository.save(typeClient);
        redirect.addFlashAttribute("success", "Saved type client successfully!");
        return "redirect:/typeClient/list";
    }

//    @GetMapping("/edit/{id}")
//    public String edit(@PathVariable Long id, Model model) {
//        model.addAttribute("typeClient", typeClientRepository.getById(id));
//        return "editType";
//    }
//
//    @PostMapping("/update")
//    public String update(TypeClient typeClient, RedirectAttributes redirect) {
//        typeClientRepository.save(typeClient);
//        redirect.addFlashAttribute("success", "Modified product successfully!");
//        return "redirect:/typeClient/list";
//    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Model model) {
        model.addAttribute("typeClient", typeClientRepository.getById(id));
        return "deleteType";
    }

    @PostMapping("/delete")
    @Transactional
    public String delete(TypeClient typeClient, RedirectAttributes redirect) {
        typeClientRepository.deleteById(typeClient.getId());
        redirect.addFlashAttribute("success", "Removed successfully!");
        return "redirect:/typeClient/list";
    }
}
