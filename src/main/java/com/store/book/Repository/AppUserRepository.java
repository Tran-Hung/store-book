package com.store.book.Repository;

import com.store.book.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByUserName(String userName);

    boolean existsByUserName(String userName);

    @Query(value = "SELECT Max(c.User_Id) FROM App_User c", nativeQuery = true)
    Long getMaxRecord();
}
