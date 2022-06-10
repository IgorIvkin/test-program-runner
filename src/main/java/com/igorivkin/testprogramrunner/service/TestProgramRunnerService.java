package com.igorivkin.testprogramrunner.service;

import com.github.dockerjava.api.command.CreateContainerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;

/**
 * This service is intended to launch the educational examples that students
 * will load to check their results.
 */
@Slf4j
@Service
public class TestProgramRunnerService {

    private final ContainerService containerService;

    private final ImageService imageService;

    @Autowired
    public TestProgramRunnerService(ContainerService containerService,
                                    ImageService imageService) {
        this.containerService = containerService;
        this.imageService = imageService;
    }

    public String runTestProgram() {
        String imageId = null;
        String containerId = null;
        try {
            // TODO add the possibility to parametrize this file path
            File dockerFile = Paths.get("d:/dev/JavaProjects/Family/test-program-runner/samples/Dockerfile")
                    .toFile();
            imageId = imageService.createImage(dockerFile, "random-image-for-java" + Math.random());
            CreateContainerResponse container = containerService.createContainer(imageId);
            containerId = container.getId();
            return containerService.runContainer(containerId);
        } finally {
            if (containerId != null) {
                containerService.deleteContainer(containerId);
            }
            if (imageId != null) {
                imageService.deleteImage(imageId);
            }
        }
    }

}
