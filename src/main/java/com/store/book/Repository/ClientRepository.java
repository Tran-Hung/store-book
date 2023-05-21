package com.store.book.Repository;

import com.store.book.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ClientRepository extends JpaRepository<Client, Long> {
    void deleteByIdClient(String idClient);

    Client findByIdClient(String idClient);

    @Query(value = "SELECT Max(c.RECORD) FROM CLIENT c", nativeQuery = true)
    Integer getMaxRecord();

    @Query(value = "select * from client s join TYPE_CLIENT tc on s.TYPE_ID = tc.ID where s.NAME_CLIENT like %:keyword% or tc.NAME_TYPE like %:keyword%", nativeQuery = true)
    List<Client> findByKeyword(@Param("keyword") String keyword);

    Client findByIdAppUser(Long id);

    @Query(value = "select s.photos from client s  where s.ID_CLIENT = :idClient", nativeQuery = true)
    String getNamePhoto(@Param("idClient") String idClient);

}
