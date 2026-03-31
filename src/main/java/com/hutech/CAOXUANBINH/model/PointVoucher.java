package com.hutech.CAOXUANBINH.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "point_vouchers")
public class PointVoucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(length = 20)
    private String phoneNumber;

    @Column(length = 50)
    private String username;

    @Column(nullable = false)
    private int redeemedPoints;

    @Column(nullable = false)
    private double discountAmount;

    @Column(nullable = false)
    private boolean used;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime usedAt;
}
