package com.capstone.JFC.repository;

import com.capstone.JFC.model.CategoryConcurrencyConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryConcurrencyConfigRepository extends JpaRepository<CategoryConcurrencyConfig, Long> {
    Optional<CategoryConcurrencyConfig> findByConfigKey(String configKey);
}
