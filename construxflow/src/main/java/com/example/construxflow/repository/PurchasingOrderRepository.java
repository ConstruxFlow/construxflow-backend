package com.example.construxflow.repository;

import com.example.construxflow.entity.PurchasingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.Optional;
import java.util.List;

@Repository
public interface PurchasingOrderRepository extends JpaRepository<PurchasingOrder, Long> {

    @Query("SELECT po FROM PurchasingOrder po " +
            "LEFT JOIN FETCH po.materials pom " +
            "LEFT JOIN FETCH pom.material " +
            "LEFT JOIN FETCH po.deliveries " +
            "LEFT JOIN FETCH po.docs " +
            "LEFT JOIN FETCH po.order_payment " +
            "LEFT JOIN FETCH po.supplier " +
            "WHERE po.po_id = :id")
    Optional<PurchasingOrder> findByIdWithAllRelations(@Param("id") Long id);

    Optional<PurchasingOrder> findByPonumber(String ponumber);

    List<PurchasingOrder> findByStatus(String status);

    @Query("SELECT po FROM PurchasingOrder po WHERE po.supplier.supplier_id = :supplierId")
    List<PurchasingOrder> findBySupplier(@Param("supplierId") String supplierId);

    @Query("SELECT po FROM PurchasingOrder po ORDER BY po.createdDate DESC LIMIT 1")
    List<PurchasingOrder> findAllOrderByOrderDateDesc();



}
