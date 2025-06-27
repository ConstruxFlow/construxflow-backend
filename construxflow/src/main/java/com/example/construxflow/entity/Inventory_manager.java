package com.example.construxflow.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("Inventory_Manager")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Inventory_manager extends manager {

}
