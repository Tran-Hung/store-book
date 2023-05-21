package com.store.book.Repository;

import com.store.book.entity.Client;
import com.store.book.entity.TypeClient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeClientRepository extends JpaRepository<TypeClient, Long> {
}
