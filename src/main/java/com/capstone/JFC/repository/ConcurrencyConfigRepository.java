package com.capstone.JFC.repository;

import com.capstone.JFC.model.ConcurrencyConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConcurrencyConfigRepository extends JpaRepository<ConcurrencyConfig, Long> {
    Optional<ConcurrencyConfig> findByConfigKey(String configKey);
}
