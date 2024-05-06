package com.stanislav.entities.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "riskProfile")
public class RiskProfile {

    @Id
    @Column
    private long id;

    private Account account;
}
