package com.capstone.JFC.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tenant_concurrency_config")
public class TenantConcurrencyConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "category_config_key", unique = true, nullable = false)
    private String categoryConfigKey;

    @Column(name = "tenant_config_key", unique = true, nullable = false)
    private String tenantConfigKey;
    
    @Column(name = "config_value", nullable = false)
    private Integer configValue;
    
    public TenantConcurrencyConfig() {}

    public TenantConcurrencyConfig(String categoryConfigKey, String tenantConfigKey, Integer configValue) {
        this.categoryConfigKey = categoryConfigKey;
        this.tenantConfigKey = tenantConfigKey;
        this.configValue = configValue;
    }

    public Long getId() {
        return id;
    }

    public String getCategoryConfigKey() {
        return categoryConfigKey;
    }

    public void setCategoryConfigKey(String categoryConfigKey) {
        this.categoryConfigKey = categoryConfigKey;
    }

    public String getTenantConfigKey() {
        return tenantConfigKey;
    }

    public void setTenantConfigKey(String tenantConfigKey) {
        this.tenantConfigKey = tenantConfigKey;
    }

    public Integer getConfigValue() {
        return configValue;
    }

    public void setConfigValue(Integer configValue) {
        this.configValue = configValue;
    }
}