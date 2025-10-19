package com.example.construxflow.repository;

import com.example.construxflow.dto.UserResponseDetailsDTO;
import com.example.construxflow.entity.UserDetails;
import com.example.construxflow.entity.User_Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    @Query("SELECT u FROM UserDetails u " +
            "LEFT JOIN FETCH u.manager m " +
            "WHERE u.supplier IS NULL " +
            "AND TYPE(m) IN :allowedManagerTypes")
    List<UserDetails> findUsersWithAllowedManagersExceptSuppliers(@Param("allowedManagerTypes") List<Class<?>> allowedManagerTypes);
}
