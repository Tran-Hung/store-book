package com.store.book.Repository;

import com.store.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    void deleteByIdBook(String idBook);

    Book findByIdBook(String idBook);

    @Query(value = "SELECT Max(b.RECORD) FROM BOOK b", nativeQuery = true)
    Integer getMaxRecord();

    @Query(value = "select * from book s join CATEGORY_BOOK cb on s.CATEGORY_ID = cb.ID where s.NAME_BOOK like %:keyword% or cb.NAME like %:keyword%", nativeQuery = true)
    List<Book> findByKeyword(@Param("keyword") String keyword);
}
