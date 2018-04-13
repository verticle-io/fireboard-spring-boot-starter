package io.verticle.oss.fireboard.springboot.starter.actuator;

import io.verticle.oss.fireboard.client.FireboardClient;
import io.verticle.oss.fireboard.client.FireboardMessageBuilder;
import io.verticle.oss.fireboard.client.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.HealthEndpoint;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.scheduling.annotation.Scheduled;

import java.net.URL;

/**
 * Fireboard emitter for Spring Boot Actuator Health checks
 * @author Jens Saade
 */
public class FireboardActuatorEmitter {

    @Autowired
    HealthEndpoint healthEndpoint;


    private Health getHealth(){
        return healthEndpoint.invoke();
    }

    @Scheduled(fixedRate = 60000)
    private void emit(){

        String ident = "application.test";
        String category = "spring.boot.health";
        String message = "" + getHealth().getStatus().getDescription();
        String link = "https://fireboard.verticle.io";
        String event = getHealth().getStatus().getCode();

        StatusEnum status = StatusEnum.success;
        if (Status.UP.equals(getHealth().getStatus())) {
            status = StatusEnum.success;
        } else  if (Status.DOWN.equals(getHealth().getStatus())) {
            status = StatusEnum.error;
        } else  if (Status.UNKNOWN.equals(getHealth().getStatus())) {
            status = StatusEnum.warn;
        } else  if (Status.OUT_OF_SERVICE.equals(getHealth().getStatus())) {
            status = StatusEnum.info;
        }


        try {
            FireboardClient.post(
                    new FireboardMessageBuilder()
                            .status(status)
                            .event(event)
                            .message(message)
                            .category(category)
                            .severity(3)
                            .ident(ident)
                            .link(new URL(link))
                            .build()
            );

        } catch (Exception e) {
            // catch anything
            //  so your own logic is not interrupted
            e.printStackTrace();
        }
    }

}
