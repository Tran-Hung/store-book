package com.store.book.controller;

import com.store.book.Repository.BookRepository;
import com.store.book.entity.Book;
import com.store.book.service.PagingService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/book")
@PreAuthorize("hasRole('ADMIN')")
public class BookRestController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PagingService pagingService;

    @GetMapping("/list")
    public ResponseEntity<Page<Book>> getListBook(@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size){
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(2);
        List<Book> books = bookRepository.findAll();
        Page<Book> bookPages = pagingService.findPaginatedBook(PageRequest.of(currentPage - 1, pageSize), books);
        return new ResponseEntity<>(bookPages, HttpStatus.OK);
    }

    @GetMapping("findById/{id}")
    public ResponseEntity<Book> tacoById(@PathVariable("id") Long id) {
        Optional<Book> optTaco = bookRepository.findById(id);
        if (optTaco.isPresent()) {
            return new ResponseEntity<>(optTaco.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping(path="/create", consumes="application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Book createBook(@RequestBody Book book){
        if (!StringUtils.isNotBlank(book.getIdBook())) {
            String prefix = "B";
            Integer id = bookRepository.getMaxRecord();
            if (id == null) id = 0;
            String generatedId = prefix + StringUtils.leftPad(String.valueOf(id + 1), 4, "0");
            book.setIdBook(generatedId);
        }
        return bookRepository.save(book);
    }

    @PutMapping(path="put/{id}", consumes="application/json")
    public Book putBook(@PathVariable("id") Long id, @RequestBody Book book) {
        book.setRecord(id);
        return bookRepository.save(book);
    }

    @PatchMapping(path="patch/{idBook}", consumes="application/json")
    public Book patchBook(@PathVariable("idBook") String idBook, @RequestBody Book book) {

        Book order = bookRepository.findByIdBook(idBook);
        if (book.getNameBook() != null) {
            order.setNameBook(book.getNameBook());
        }
        if (book.getPrice() != null) {
            order.setPrice(book.getPrice());
        }
        if (book.getAmount() != null) {
            order.setAmount(book.getAmount());
        }
        if (book.getAuthor() != null) {
            order.setAuthor(book.getAuthor());
        }
        if (book.getPublisher() != null) {
            order.setPublisher(book.getPublisher());
        }
        if (book.getDatePublish() != null) {
            order.setDatePublish(book.getDatePublish());
        }
        if (book.getCategoryBook() != null) {
            order.setCategoryBook(book.getCategoryBook());
        }

        return bookRepository.save(order);
    }

    @DeleteMapping("delete/{idBook}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable("idBook") String idBook) {
        try {
            bookRepository.deleteByIdBook(idBook);
        } catch (EmptyResultDataAccessException e) {}
    }
}
