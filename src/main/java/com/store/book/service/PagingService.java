package com.store.book.service;

import com.store.book.entity.Book;
import com.store.book.entity.Client;
import com.store.book.entity.Study;
import com.store.book.entity.Toy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Collections;
import java.util.List;

@Service
public class PagingService {

    public Page<Client> findPaginated(Pageable pageable, List<Client> clients) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Client> list;

        if (clients.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, clients.size());
            list = clients.subList(startItem, toIndex);
        }

        Page<Client> clientPage
                = new PageImpl<Client>(list, PageRequest.of(currentPage, pageSize), clients.size());

        return clientPage;
    }

    public Page<Book> findPaginatedBook(Pageable pageable, List<Book> books) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Book> list;

        if (books.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, books.size());
            list = books.subList(startItem, toIndex);
        }

        Page<Book> bookPage
                = new PageImpl<Book>(list, PageRequest.of(currentPage, pageSize), books.size());

        return bookPage;
    }

    public Page<Study> findPaginatedStudy(Pageable pageable, List<Study> studies) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Study> list;

        if (studies.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, studies.size());
            list = studies.subList(startItem, toIndex);
        }

        Page<Study> studyPage
                = new PageImpl<Study>(list, PageRequest.of(currentPage, pageSize), studies.size());

        return studyPage;
    }

    public Page<Toy> findPaginatedToy(Pageable pageable, List<Toy> toys) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Toy> list;

        if (toys.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, toys.size());
            list = toys.subList(startItem, toIndex);
        }

        Page<Toy> toyPage
                = new PageImpl<Toy>(list, PageRequest.of(currentPage, pageSize), toys.size());

        return toyPage;
    }
}
