package com.store.book.Repository;

import com.store.book.entity.CategoryBook;
import com.store.book.entity.CategoryToy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryToyRespository extends JpaRepository<CategoryToy, Long> {
}
