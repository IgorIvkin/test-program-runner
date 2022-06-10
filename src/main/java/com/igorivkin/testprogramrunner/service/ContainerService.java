package com.igorivkin.testprogramrunner.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.igorivkin.testprogramrunner.api.dto.container.GetContainerResponse;
import com.igorivkin.testprogramrunner.exception.RunningTooLongException;
import com.igorivkin.testprogramrunner.mapper.DockerContainerMapper;
import com.igorivkin.testprogramrunner.util.StringBuilderLogReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ContainerService {

    private static final int SECONDS_TO_AWAIT = 1;

    private static final int SECONDS_THRESHOLD_TO_STOP = 15;

    private final DockerClient dockerClient;

    private final DockerContainerMapper dockerContainerMapper;

    @Autowired
    public ContainerService(DockerClient dockerClient,
                            DockerContainerMapper dockerContainerMapper) {
        this.dockerClient = dockerClient;
        this.dockerContainerMapper = dockerContainerMapper;
    }

    /**
     * Returns list of Docker containers available on the machine.
     *
     * @return list of docker containers available on the machine.
     */
    public List<GetContainerResponse> getContainers() {
        List<Container> containers = dockerClient.listContainersCmd().exec();
        return dockerContainerMapper.toListOfDto(containers);
    }

    /**
     * Creates a container from a given image and returns its response object.
     *
     * @param imageId image ID to create container from it
     * @return response of created container
     */
    public CreateContainerResponse createContainer(String imageId) {
        return dockerClient.createContainerCmd(imageId)
                .withName("testProgram")
                .withAttachStdin(true)
                .withTty(true)
                .exec();
    }

    /**
     * Runs a given container, awaits for its termination and provides textual logs from it.
     *
     * @param containerId ID of container to execute
     * @return textual log from container
     */
    public String runContainer(String containerId) {
        dockerClient.startContainerCmd(containerId).exec();

        int runningInSeconds = 0;
        InspectContainerResponse inspect = dockerClient.inspectContainerCmd(containerId).exec();
        while (Boolean.TRUE.equals(inspect.getState().getRunning())) {
            sleepInSeconds(SECONDS_TO_AWAIT);
            runningInSeconds += SECONDS_TO_AWAIT;
            if (runningInSeconds > SECONDS_THRESHOLD_TO_STOP) {
                stopContainer(containerId);
                throw new RunningTooLongException("Container " + containerId + " was running too long");
            }
            inspect = dockerClient.inspectContainerCmd(containerId).exec();
        }

        final StringBuilder stringBuilder = new StringBuilder();
        try (final StringBuilderLogReader logReader = new StringBuilderLogReader(stringBuilder)) {
            dockerClient.logContainerCmd(containerId)
                    .withStdErr(true)
                    .withStdOut(true)
                    .withTailAll()
                    .exec(logReader)
                    .awaitCompletion();
            log.info("Container {} was running for {} seconds", containerId, runningInSeconds);
            return logReader.getBuilder().toString();
        } catch (IOException | InterruptedException ex) {
            throw new IllegalStateException(ex.getMessage());
        }
    }

    /**
     * Stops a container by its given ID.
     *
     * @param containerId ID of container to stop
     */
    public void stopContainer(String containerId) {
        dockerClient.stopContainerCmd(containerId).exec();
    }

    /**
     * Removes a container by its given ID.
     *
     * @param containerId ID of container to remove
     */
    public void deleteContainer(String containerId) {
        dockerClient.removeContainerCmd(containerId).exec();
    }

    private void sleepInSeconds(int seconds) {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(seconds));
        } catch (InterruptedException ex) {
            throw new IllegalStateException(ex.getMessage());
        }
    }
}
