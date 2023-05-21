package com.store.book.controller;

import com.store.book.Repository.CategoryBookRespository;
import com.store.book.Repository.CategoryStudyRespository;
import com.store.book.entity.CategoryBook;
import com.store.book.entity.CategoryStudy;
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
@RequestMapping("/categoryStudy")
public class CategoryStudyController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CategoryStudyRespository categoryStudyRespository;


    @RequestMapping(value="/list", method= RequestMethod.GET)
    public String listClient( ModelMap model) {
        List<CategoryStudy> list = categoryStudyRespository.findAll();
        if (list != null) {
            model.addAttribute("list", list);
        }
        return "listCategoryStudy";
    }

    @GetMapping(value = {"/create-edit", "/create-edit/{id}"})
    public String create(Model model, @PathVariable(value = "id", required = false) Long id) {
        if (id == null)
            model.addAttribute("categoryStudy", new CategoryBook());
        else
            model.addAttribute("categoryStudy", categoryStudyRespository.getById(id));
        return "createCategoryStudy";
    }

    @PostMapping("/save")
    public String save(CategoryStudy categoryStudy, RedirectAttributes redirect) {
        categoryStudyRespository.save(categoryStudy);
        redirect.addFlashAttribute("success", "Saved successfully!");
        return "redirect:/categoryStudy/list";
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
        model.addAttribute("categoryStudy", categoryStudyRespository.getById(id));
        return "deleteCategoryStudy";
    }

    @PostMapping("/delete")
    @Transactional
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        categoryStudyRespository.deleteById(id);
        redirect.addFlashAttribute("success", "Removed successfully!");
        return "redirect:/categoryStudy/list";
    }
}
