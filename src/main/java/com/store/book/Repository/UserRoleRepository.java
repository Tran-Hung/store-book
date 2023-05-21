package com.store.book.Repository;

import com.store.book.entity.AppUser;
import com.store.book.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    UserRole findByAppUser(AppUser appUser);
}
