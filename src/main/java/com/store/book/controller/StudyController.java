package com.store.book.controller;

import com.store.book.Repository.BookRepository;
import com.store.book.Repository.CategoryBookRespository;
import com.store.book.Repository.CategoryStudyRespository;
import com.store.book.Repository.StudyRepository;
import com.store.book.entity.Book;
import com.store.book.entity.CategoryBook;
import com.store.book.entity.CategoryStudy;
import com.store.book.entity.Study;
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
@RequestMapping("/study")
public class StudyController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private CategoryStudyRespository categoryStudyRespository;

    @Autowired
    private PagingService pagingService;

    @RequestMapping(value="/list", method= RequestMethod.GET)
    public String listClient(ModelMap model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size, String keyword) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(2);
        List<Study> studies = new ArrayList<Study>();
        if (keyword!=null) {
            studies = studyRepository.findByKeyword(keyword);
        }
        else {
            studies = studyRepository.findAll();
        }
        Page<Study> studyPages = pagingService.findPaginatedStudy(PageRequest.of(currentPage - 1, pageSize), studies);
        if (studies != null) {
            model.addAttribute("studyPages", studyPages);
            model.addAttribute("currentPage", currentPage);
        }
        int totalPages = studyPages.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "studyList";
    }


    @GetMapping(value = {"/create-edit", "/create-edit/{idStudy}"})
    public String create(@PathVariable(value = "idStudy", required = false) String idStudy, Model model) {
        List<CategoryStudy> category = categoryStudyRespository.findAll();
        if (category != null) {
            model.addAttribute("categories", category);
        }
        if (idStudy == null)
            model.addAttribute("study", new Study());
        else
            model.addAttribute("study", studyRepository.findByIdStudy(idStudy));
        return "createStudy";
    }

    @PostMapping("/save")
    public String save(Study study, RedirectAttributes redirect) {
        if (!StringUtils.isNotBlank(study.getIdStudy())) {
            String prefix = "S";
            Integer id = studyRepository.getMaxRecord();
            if (id == null) id = 0;
            String generatedId = prefix + StringUtils.leftPad(String.valueOf(id + 1), 4, "0");
            study.setIdStudy(generatedId);
        }
        studyRepository.save(study);
        redirect.addFlashAttribute("success", "Saved successfully!");
        return "redirect:/study/list";
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

    @GetMapping("/delete/{idStudy}")
    public String delete(@PathVariable String idStudy, Model model) {
        model.addAttribute("study", studyRepository.findByIdStudy(idStudy));
        return "deleteStudy";
    }

    @PostMapping("/delete")
    @Transactional
    public String delete(Study study, RedirectAttributes redirect) {
        studyRepository.deleteByIdStudy(study.getIdStudy());
        redirect.addFlashAttribute("success", "Removed successfully!");
        return "redirect:/study/list";
    }

}
