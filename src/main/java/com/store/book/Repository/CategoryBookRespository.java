package com.store.book.Repository;

import com.store.book.entity.CategoryBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryBookRespository extends JpaRepository<CategoryBook, Long> {
}
