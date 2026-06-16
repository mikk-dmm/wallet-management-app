package com.example.wallet_management_app.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.wallet_management_app.entity.AlertSetting;

public interface AlertSettingRepository extends JpaRepository<AlertSetting, Long> {
    Optional<AlertSetting> findByUserId(Long userId);
}