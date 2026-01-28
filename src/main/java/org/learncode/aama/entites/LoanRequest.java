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
public class LoanRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long loanReqId;
    private Double Amount;
    private String purpose;
    private LocalDateTime createdAt=LocalDateTime.now();
    private String status="pending";

    // CHANGE: OneToOne -> ManyToOne
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Users users;
}