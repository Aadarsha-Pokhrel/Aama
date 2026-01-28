package org.learncode.aama.entites;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Deposit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long despositId;
    @ManyToOne
    @JsonBackReference
    private Users users;
    private String month;
    private Double amount;
    private String status="Paid";
    private LocalDateTime createdAt= LocalDateTime.now();
}
