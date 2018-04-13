# fireboard-spring-boot-starter
A Spring Boot starter for Fireboard (log propagation and health checks)


## Features

### Log forwarding

The starter forwards logger entries as status messages with a configurable level.

### Health Check forwarding
It sends Actuator health checks to Fireboard.

## Setup & Usage

### Requirements

A Spring Boot application


### Maven

This library can be retrieved via Jitpack:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

```xml
<dependency>
    <groupId>com.github.verticleio</groupId>
    <artifactId>fireboard-spring-boot-starter</artifactId>
    <version>LATEST</version>
</dependency>
```

### Configuration

#### Fireboard API access

Add these properties to your application.properties/.yml

Mandatory properties:
* `fireboard.api.tenant` - your tenant ID
* `fireboard.api.bucket` - the bucket you want to store the status to
* `fireboard.api.auth.token` - your API token (open Fireboard UI to retrieve it)

Optional:
* `fireboard.api.endpoint` - is preset but can be altered if necessary
* `fireboard.debug` - to enable debug output
* `fireboard.logging.enabled` - true by default

#### Fireboard Log Appender

The appender is based on Logback (Spring Boot default)


##### Appender Properties

| field           | type   | description                 | example                |
| --------------- | ------ | --------------------------- | ---------------------- |
| logLevel        | enum `trace,warn,error,info` | default: warn  | 'warn'
| ident           | string | a qualifier for the log message           | 'backend.elasticsearch.bootstrap' |
| category        | string `[a-z0-9._-]`| a category name              | 'backend_services' |
| debug           | boolean | enable debugging messages, default: false|  |
| encoder.pattern |  | see https://logback.qos.ch/manual/encoders.html              | 'The backend service has successfully started and connected to elasticsearch' |

### Default Appender Configuration

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>

    <!-- dont forget to add spring's default configuration -->
    <include resource="org/springframework/boot/logging/logback/base.xml"/>

    <appender name="FIREBOARD" class="io.verticle.oss.fireboard.springboot.starter.logger.logback.FireboardAppender">
        <logLevel>warn</logLevel>
        <ident>application.spring.boot.ident</ident>
        <category>application.spring.boot.category</category>
        <debug>false</debug>
        <encoder>
            <pattern>%d ${CONTEXT_NAME} %level %msg %logger{50}%n</pattern>
        </encoder>
    </appender>

    <root>
        <appender-ref ref="FIREBOARD" />
    </root>
</configuration>
```

## Licence

MIT, see LICENSE file