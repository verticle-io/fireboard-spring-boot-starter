package io.verticle.oss.fireboard.springboot.starter.config;

import io.verticle.oss.fireboard.client.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Fireboard API endpoint connection
 * @author Jens Saade
 */
@ConfigurationProperties
public class FireboardProperties {

    private String apiEndpoint = Constants.DEFAULT_API_URL;

    @Value("${fireboard.api.tenant:}")
    private String tenant;

    @Value("${fireboard.api.bucket:}")
    private String bucket;

    @Value("${fireboard.api.auth.token:}")
    private String authToken;

    public String getApiEndpoint() {
        return apiEndpoint;
    }

    public void setApiEndpoint(String apiEndpoint) {
        this.apiEndpoint = apiEndpoint;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
