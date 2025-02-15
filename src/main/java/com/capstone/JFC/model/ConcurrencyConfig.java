package com.capstone.JFC.model;

import jakarta.persistence.*;

@Entity
@Table(name = "concurrency_config")
public class ConcurrencyConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "config_key", unique = true, nullable = false)
    private String configKey;
    
    @Column(name = "config_value", nullable = false)
    private Integer configValue;
    
    public ConcurrencyConfig() {}

    public ConcurrencyConfig(String configKey, Integer configValue) {
        this.configKey = configKey;
        this.configValue = configValue;
    }

    public Long getId() {
        return id;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public Integer getConfigValue() {
        return configValue;
    }

    public void setConfigValue(Integer configValue) {
        this.configValue = configValue;
    }
}