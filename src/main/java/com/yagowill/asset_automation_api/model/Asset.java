package com.yagowill.asset_automation_api.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "asset")
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rp_number", nullable = false)
    private String rpNumber;

    @Column(nullable = false)
    private String description;

    @Column(name = "origin_agency", nullable = false)
    private String originAgency;

    @Column(name = "term_number", nullable = false)
    private String termNumber;

    @Column(name = "destination_unit", nullable = false)
    private String destinationUnit;

    @Column(name = "incorporation_date")
    private LocalDateTime incorporationDate;

    @Column(nullable = false)
    private String status;

    @Column(columnDefinition = "TEXT", name = "log_message")
    private String logMessage;
}
