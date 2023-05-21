package com.store.book.Repository;

import com.store.book.entity.Book;
import com.store.book.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudyRepository extends JpaRepository<Study, Long> {
    void deleteByIdStudy(String idBook);

    Study findByIdStudy(String idBook);

    @Query(value = "SELECT Max(b.RECORD) FROM STUDY b", nativeQuery = true)
    Integer getMaxRecord();

    @Query(value = "select * from STUDY s join CATEGORY_STUDY cs on s.CATEGORY_ID = cs.ID where s.NAME_STUDY like %:keyword% or cs.NAME like %:keyword%", nativeQuery = true)
    List<Study> findByKeyword(@Param("keyword") String keyword);
}
