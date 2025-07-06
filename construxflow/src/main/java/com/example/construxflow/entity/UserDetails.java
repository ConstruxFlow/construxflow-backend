package com.example.construxflow.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.google.firebase.database.annotations.NotNull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long User_id;

    @Column(nullable = false, unique = true)
    private String firebaseUid;

    private String user_name;
    private String email;
    private Long phone_number1;
    private Long phone_number2;
    private String address;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)

    private String password;

    @JsonProperty("userRole")
    @Enumerated(EnumType.STRING)
    private User_Role userRole;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", referencedColumnName = "manager_id")
    private Manager manager;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", referencedColumnName = "supplier_id")
    @JsonIgnore
    private Supplier supplier;

}
