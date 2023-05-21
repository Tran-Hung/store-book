package com.store.book.Repository;

import com.store.book.entity.Book;
import com.store.book.entity.Toy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ToyRepository extends JpaRepository<Toy, Long> {
    void deleteByIdToy(String idToy);

    Toy findByIdToy(String idToy);

    @Query(value = "SELECT Max(b.RECORD) FROM toy b", nativeQuery = true)
    Integer getMaxRecord();

    @Query(value = "select * from toy s join CATEGORY_TOY ct on s.CATEGORY_ID = ct.ID where s.NAME_TOY like %:keyword% or ct.NAME like %:keyword%", nativeQuery = true)
    List<Toy> findByKeyword(@Param("keyword") String keyword);
}
