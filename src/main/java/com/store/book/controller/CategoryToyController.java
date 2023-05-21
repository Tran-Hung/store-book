package com.store.book.controller;

import com.store.book.Repository.CategoryToyRespository;
import com.store.book.entity.CategoryToy;
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
@RequestMapping("/categoryToy")
public class CategoryToyController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CategoryToyRespository categoryToyRespository;


    @RequestMapping(value="/list", method= RequestMethod.GET)
    public String listClient( ModelMap model) {
        List<CategoryToy> list = categoryToyRespository.findAll();
        if (list != null) {
            model.addAttribute("list", list);
        }
        return "listCategoryToy";
    }

    @GetMapping(value = {"/create-edit", "/create-edit/{id}"})
    public String create(Model model, @PathVariable(value = "id", required = false) Long id) {
        if (id == null)
            model.addAttribute("categoryToy", new CategoryToy());
        else
            model.addAttribute("categoryToy", categoryToyRespository.getById(id));
        return "createCategoryToy";
    }

    @PostMapping("/save")
    public String save(CategoryToy categoryToy, RedirectAttributes redirect) {
        categoryToyRespository.save(categoryToy);
        redirect.addFlashAttribute("success", "Saved successfully!");
        return "redirect:/categoryToy/list";
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
//
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Model model) {
        model.addAttribute("categoryToy", categoryToyRespository.getById(id));
        return "deleteCategoryToy";
    }

    @PostMapping("/delete")
    @Transactional
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        categoryToyRespository.deleteById(id);
        redirect.addFlashAttribute("success", "Removed successfully!");
        return "redirect:/categoryToy/list";
    }
}
