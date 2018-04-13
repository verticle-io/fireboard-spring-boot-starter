package io.verticle.oss.fireboard.springboot.starter.config;

import io.verticle.oss.fireboard.client.FireboardAccessConfig;
import io.verticle.oss.fireboard.client.FireboardClient;
import io.verticle.oss.fireboard.springboot.starter.actuator.FireboardActuatorEmitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Autoconfiguration for Fireboard Spring-Boot Starter
 * NOTE: logger is configured separately (logback.xml)
 *
 *
 * @author Jens Saade
 */
@Configuration
@ConditionalOnClass(FireboardActuatorEmitter.class)
@EnableConfigurationProperties(FireboardProperties.class)
public class FireboardAutoConfiguration {

    @Autowired
    FireboardProperties fireboardProperties;

    @Value("${spring:application.name:spring-boot}")
    private String appName;

    @Value("${fireboard.appender.log.level:warn}")
    private String appenderLogLevel;

    @Bean
    public FireboardActuatorEmitter createEmitter(){

        try{
            assert fireboardProperties.getBucket() != null;
            assert fireboardProperties.getTenant() != null;
            assert fireboardProperties.getAuthToken() != null;

            // expose for FireboardClient lib which has no access to spring app properties

            FireboardAccessConfig config = new FireboardAccessConfig();
            config.setApiEndpointUrl(fireboardProperties.getApiEndpoint());
            config.setAuthToken(fireboardProperties.getAuthToken());
            config.setBucketId(fireboardProperties.getBucket());
            config.setTenantId(fireboardProperties.getTenant());

            FireboardClient.lazyInit(config);

        } catch (AssertionError e) {
            System.out.println("check your fireboard properties");
        }



        return new FireboardActuatorEmitter();
    }

}
