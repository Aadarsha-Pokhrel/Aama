package org.learncode.aama.entites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class NotificationReceipent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Users user;
    @ManyToOne
    private Notice notice;
    private String status="Unread";

}
