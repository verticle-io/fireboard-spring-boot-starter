package io.verticle.oss.fireboard.springboot.starter.test;

import io.verticle.oss.fireboard.springboot.starter.actuator.FireboardActuatorEmitter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Jens Saade
 */

@EnableAutoConfiguration()
@SpringBootApplication
public class TestApplication {

    Log logger = LogFactory.getLog(this.getClass().getName());

    @Autowired
    FireboardActuatorEmitter emitter;

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);

        TestApplication app = new TestApplication();
        app.testLogging();

    }

    public void testLogging(){
        logger.error("a NullPointer!");
    }

}
