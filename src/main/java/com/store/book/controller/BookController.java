package com.store.book.controller;

import com.store.book.Repository.BookRepository;
import com.store.book.Repository.CategoryBookRespository;
import com.store.book.Repository.ClientRepository;
import com.store.book.Repository.TypeClientRepository;
import com.store.book.entity.Book;
import com.store.book.entity.CategoryBook;
import com.store.book.entity.Client;
import com.store.book.entity.TypeClient;
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
@RequestMapping("/book")
public class BookController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryBookRespository categoryBookRespository;

    @Autowired
    private PagingService pagingService;

    @RequestMapping(value="/list", method= RequestMethod.GET)
    public String listClient(ModelMap model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size, String keyword) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(2);
        List<Book> books = new ArrayList<Book>();
        if (keyword!=null) {
            books = bookRepository.findByKeyword(keyword);
        }
        else {
            books = bookRepository.findAll();
        }
        Page<Book> bookPages = pagingService.findPaginatedBook(PageRequest.of(currentPage - 1, pageSize), books);
        if (books != null) {
            model.addAttribute("bookPages", bookPages);
            model.addAttribute("currentPage", currentPage);
        }
        int totalPages = bookPages.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "bookList";
    }


    @GetMapping(value = {"/create-edit", "/create-edit/{idBook}"})
    public String create(@PathVariable(value = "idBook", required = false) String idBook, Model model) {
        List<CategoryBook> category = categoryBookRespository.findAll();
        if (category != null) {
            model.addAttribute("categories", category);
        }
        if (idBook == null)
            model.addAttribute("book", new Book());
        else
            model.addAttribute("book", bookRepository.findByIdBook(idBook));
        return "createBook";
    }

    @PostMapping("/save")
    public String save(Book book, RedirectAttributes redirect) {
        if (!StringUtils.isNotBlank(book.getIdBook())) {
            String prefix = "B";
            Integer id = bookRepository.getMaxRecord();
            if (id == null) id = 0;
            String generatedId = prefix + StringUtils.leftPad(String.valueOf(id + 1), 4, "0");
            book.setIdBook(generatedId);
        }
        bookRepository.save(book);
        redirect.addFlashAttribute("success", "Saved successfully!");
        return "redirect:/book/list";
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

    @GetMapping("/delete/{idBook}")
    public String delete(@PathVariable String idBook, Model model) {
        model.addAttribute("book", bookRepository.findByIdBook(idBook));
        return "deleteBook";
    }

    @PostMapping("/delete")
    @Transactional
    public String delete(Book book, RedirectAttributes redirect) {
        bookRepository.deleteByIdBook(book.getIdBook());
        redirect.addFlashAttribute("success", "Removed successfully!");
        return "redirect:/book/list";
    }

}
