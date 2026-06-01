package com.example.wallet_management_app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.FetchType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
    name = "alert_settings",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id"})
    }
)
public class AlertSetting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "monthly_budget_alert_enabled", nullable = false)
    private Boolean monthlyBudgetAlertEnabled;

    @Column(name = "monthly_budget_alert_threshold", nullable = false)
    private Integer monthlyBudgetAlertThreshold;

    @Column(name = "remaining_amount_alert_enabled", nullable = false)
    private Boolean remainingAmountAlertEnabled;

    @Column(name = "remaining_amount_threshold", nullable = false)
    private Long remainingAmountThreshold;
}