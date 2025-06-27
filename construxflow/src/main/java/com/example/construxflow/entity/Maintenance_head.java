package com.example.construxflow.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("Maintenance_Head")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Maintenance_head extends manager {

}
