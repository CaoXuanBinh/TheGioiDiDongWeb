package com.hutech.CAOXUANBINH.repository;

import com.hutech.CAOXUANBINH.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    void deleteByProductId(Long productId);
}
