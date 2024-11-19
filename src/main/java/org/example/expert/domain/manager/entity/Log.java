package org.example.expert.domain.manager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "log")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private LocalDateTime logTime;
    @Column
    private Long managerId;
    @Column
    private String action;

    public Log(Long managerId, String action){
        this.managerId = managerId;
        this.action = action;
        this.logTime = LocalDateTime.now();
    }


}
