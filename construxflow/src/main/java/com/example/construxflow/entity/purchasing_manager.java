
package com.example.construxflow.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("Purchasing_Manager")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class purchasing_manager extends manager {

    @OneToMany(mappedBy = "manager")
    private List<Quotation_request> quotationRequests;

}
