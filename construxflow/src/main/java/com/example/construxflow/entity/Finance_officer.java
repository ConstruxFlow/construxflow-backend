package com.example.construxflow.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("Finance_Officer")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Finance_officer extends manager {
    
}
