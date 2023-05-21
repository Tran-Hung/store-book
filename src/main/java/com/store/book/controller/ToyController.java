package com.store.book.controller;


import com.store.book.Repository.CategoryToyRespository;
import com.store.book.Repository.ToyRepository;
import com.store.book.entity.CategoryToy;
import com.store.book.entity.Toy;
import com.store.book.service.PagingService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/toy")
public class ToyController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ToyRepository toyRepository;

    @Autowired
    private CategoryToyRespository categoryToyRespository;

    @Autowired
    private PagingService pagingService;

    @RequestMapping(value="/list", method= RequestMethod.GET)
    public String listClient(ModelMap model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size, String keyword) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(2);
        List<Toy> toys = new ArrayList<Toy>();
        if (keyword!=null) {
            toys = toyRepository.findByKeyword(keyword);
        }
        else {
            toys = toyRepository.findAll();
        }
        Page<Toy> toyPages = pagingService.findPaginatedToy(PageRequest.of(currentPage - 1, pageSize), toys);
        if (toys != null) {
            model.addAttribute("toyPages", toyPages);
            model.addAttribute("currentPage", currentPage);
        }
        int totalPages = toyPages.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "toyList";
    }


    @GetMapping(value = {"/create-edit", "/create-edit/{idToy}"})
    public String create(@PathVariable(value = "idToy", required = false) String idToy, Model model) {
        List<CategoryToy> category = categoryToyRespository.findAll();
        if (category != null) {
            model.addAttribute("categories", category);
        }
        if (idToy == null)
            model.addAttribute("toy", new Toy());
        else
            model.addAttribute("toy", toyRepository.findByIdToy(idToy));
        return "createToy";
    }

    @PostMapping("/save")
    public String save(Toy toy, RedirectAttributes redirect) {
        if (!StringUtils.isNotBlank(toy.getIdToy())) {
            String prefix = "T";
            Integer id = toyRepository.getMaxRecord();
            if (id == null) id = 0;
            String generatedId = prefix + StringUtils.leftPad(String.valueOf(id + 1), 4, "0");
            toy.setIdToy(generatedId);
        }
        toyRepository.save(toy);
        redirect.addFlashAttribute("success", "Saved successfully!");
        return "redirect:/toy/list";
    }

//    @GetMapping("/edit/{idClient}")
//    public String edit(@PathVariable String idClient, Model model) {
//        model.addAttribute("client", clientRepository.findByIdClient(idClient));
//        List<TypeClient> typeClients = typeClientRepository.findAll();
//        if (typeClients != null) {
//            model.addAttribute("typeClients", typeClients);
//        }
//        return "createClient";
//    }
//
//    @PostMapping("/update")
//    public String update(Client client, RedirectAttributes redirect) {
//        clientRepository.save(client);
//        redirect.addFlashAttribute("success", "Modified product successfully!");
//        return "redirect:/client/list";
//    }

    @GetMapping("/delete/{idToy}")
    public String delete(@PathVariable String idToy, Model model) {
        model.addAttribute("toy", toyRepository.findByIdToy(idToy));
        return "deleteToy";
    }

    @PostMapping("/delete")
    @Transactional
    public String delete(Toy toy, RedirectAttributes redirect) {
        toyRepository.deleteByIdToy(toy.getIdToy());
        redirect.addFlashAttribute("success", "Removed successfully!");
        return "redirect:/toy/list";
    }

}
