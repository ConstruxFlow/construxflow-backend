package com.example.construxflow.repository;

import com.example.construxflow.entity.I_Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface I_MaterialRepository extends JpaRepository<I_Material, Long> {
}
