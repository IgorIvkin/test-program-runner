package com.igorivkin.testprogramrunner.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.docker")
public class AppDockerConfigurationProperties {

    /**
     * Host to connect to Docker
     */
    private String host;

    /**
     * Max number of connections for HTTP-client.
     */
    private Integer maxConnections;

    /**
     * Settings of timeouts.
     */
    private Timeouts timeouts;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Timeouts {

        private Integer connectionTimeout;

        private Integer responseTimeout;
    }
}
