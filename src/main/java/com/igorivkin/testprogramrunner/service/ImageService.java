package com.igorivkin.testprogramrunner.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Set;

/**
 * Provides methods to manipulate images (create, update, remove an image).
 */
@Slf4j
@Service
public class ImageService {

    private final DockerClient dockerClient;

    @Autowired
    public ImageService(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    /**
     * Creates an image from a given Dockerfile with given image tag.
     *
     * @param file     Dockerfile
     * @param imageTag image tag to create image
     * @return ID of created image
     */
    public String createImage(File file, String imageTag) {
        return dockerClient.buildImageCmd()
                .withDockerfile(file)
                .withPull(true)
                .withNoCache(true)
                .withTags(Set.of(imageTag))
                .exec(new BuildImageResultCallback())
                .awaitImageId();
    }

    /**
     * Removes an image defined by its ID.
     *
     * @param imageId ID of image to delete.
     */
    public void deleteImage(String imageId) {
        dockerClient.removeImageCmd(imageId).exec();
    }
}
