package com.example.construxflow.repository;

import com.example.construxflow.entity.UserDetails;
import com.example.construxflow.entity.User_Role;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserDetails, Long> {
    Optional<UserDetails> findByFirebaseUid(String firebaseUid);

    
    @Query("SELECT u.supplier.supplier_id " +
       "FROM UserDetails u " +
       "WHERE u.userRole = :userRole " +
       "ORDER BY u.createdAt DESC " +
       "LIMIT 1")
    Optional<String> findLatestSupplierId(@Param("userRole") User_Role userRole);


}
