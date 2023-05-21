package com.store.book.Repository;

import com.store.book.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    @Query(value = "select * from orders o join client c on o.CLIENT_ID = c.ID_CLIENT where o.status = :status and o.CLIENT_ID = :clientId", nativeQuery = true)
    Orders findByClientIdAndStatus(@Param("clientId") String clientId, @Param("status") String status);

    @Query(value = "SELECT Max(b.RECORD) FROM orders b", nativeQuery = true)
    Integer getMaxRecord();

    Orders findByIdOrder(String idOrder);
}
