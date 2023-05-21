package com.store.book.Repository;

import com.store.book.entity.DetailOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DetailOrderRepository extends JpaRepository<DetailOrder, Long> {
    @Query(value = "select * from DETAIL_ORDER s where s.ORDER_ID = :idOrder and s.PRODUCT_ID = :idProduct", nativeQuery = true)
    DetailOrder findByAndOrderIdAndProductId(@Param("idOrder") String idOrder, @Param("idProduct") String idProduct);
}
