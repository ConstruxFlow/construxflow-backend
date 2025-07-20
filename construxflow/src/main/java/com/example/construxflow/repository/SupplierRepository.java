package com.example.construxflow.repository;

import com.example.construxflow.entity.Supplier;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SupplierRepository extends JpaRepository<Supplier,String> {

    // @Query("SELECT s FROM Supplier s LEFT JOIN FETCH s.userDetails ORDER BY s.createdAt DESC")
    // Optional<Supplier> findLatestSupplier();
}
