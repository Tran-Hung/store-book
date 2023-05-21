package com.store.book.controller;

import com.store.book.Repository.CategoryBookRespository;
import com.store.book.Repository.ClientRepository;
import com.store.book.entity.CategoryBook;
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
@RequestMapping("/categoryBook")
public class CategoryBookController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CategoryBookRespository categoryBookRespository;


    @RequestMapping(value="/list", method= RequestMethod.GET)
    public String listClient( ModelMap model) {
        List<CategoryBook> list = categoryBookRespository.findAll();
        if (list != null) {
            model.addAttribute("list", list);
        }
        return "listCategoryBook";
    }

    @GetMapping(value = {"/create-edit", "/create-edit/{id}"})
    public String create(Model model, @PathVariable(value = "id", required = false) Long id) {
        if (id == null)
            model.addAttribute("categoryBook", new CategoryBook());
        else
            model.addAttribute("categoryBook", categoryBookRespository.getById(id));
        return "createCategoryBook";
    }

    @PostMapping("/save")
    public String save(CategoryBook categoryBook, RedirectAttributes redirect) {
        categoryBookRespository.save(categoryBook);
        redirect.addFlashAttribute("success", "Saved successfully!");
        return "redirect:/categoryBook/list";
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
        model.addAttribute("categoryBook", categoryBookRespository.getById(id));
        return "deleteCategoryBook";
    }

    @PostMapping("/delete")
    @Transactional
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        categoryBookRespository.deleteById(id);
        redirect.addFlashAttribute("success", "Removed successfully!");
        return "redirect:/categoryBook/list";
    }
}
