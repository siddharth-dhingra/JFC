package com.capstone.JFC.repository;

import com.capstone.JFC.model.TenantConcurrencyConfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantConcurrencyConfigRepository extends JpaRepository<TenantConcurrencyConfig, Long> {
    Optional<TenantConcurrencyConfig> findByCategoryConfigKeyAndTenantConfigKey(String categoryConfigKey, String tenantConfigKey);
}