package com.igorivkin.testprogramrunner.config;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Includes initialization of Docker client and transport configuration for
 * interconnections with Docker client.
 */
@Configuration
public class DockerClientConfiguration {

    private final AppDockerConfigurationProperties dockerConfigurationProperties;

    @Autowired
    public DockerClientConfiguration(AppDockerConfigurationProperties dockerConfigurationProperties) {
        this.dockerConfigurationProperties = dockerConfigurationProperties;
    }

    @Bean
    public DockerClientConfig dockerClientConfig() {
        return DefaultDockerClientConfig.createDefaultConfigBuilder()
                //.withDockerHost(dockerConfigurationProperties.getHost())
                .withDockerTlsVerify(false)
                .build();
    }
    @Bean
    public DockerHttpClient dockerHttpClient(DockerClientConfig dockerClientConfig) {
        return new ApacheDockerHttpClient.Builder()
                .dockerHost(dockerClientConfig.getDockerHost())
                .sslConfig(dockerClientConfig.getSSLConfig())
                .maxConnections(dockerConfigurationProperties.getMaxConnections())
                .connectionTimeout(Duration.ofSeconds(dockerConfigurationProperties.getTimeouts().getConnectionTimeout()))
                .responseTimeout(Duration.ofSeconds(dockerConfigurationProperties.getTimeouts().getResponseTimeout()))
                .build();
    }

    @Bean
    public DockerClient dockerClient(DockerClientConfig dockerClientConfig, DockerHttpClient dockerHttpClient) {
        return DockerClientImpl.getInstance(dockerClientConfig, dockerHttpClient);
    }
}
