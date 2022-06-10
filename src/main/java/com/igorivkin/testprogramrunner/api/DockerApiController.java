package com.igorivkin.testprogramrunner.api;

import com.igorivkin.testprogramrunner.api.dto.container.GetContainerResponse;
import com.igorivkin.testprogramrunner.service.ContainerService;
import com.igorivkin.testprogramrunner.service.TestProgramRunnerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class DockerApiController {

    private final TestProgramRunnerService testProgramRunnerService;

    private final ContainerService containerService;

    @Autowired
    public DockerApiController(ContainerService containerService,
                               TestProgramRunnerService testProgramRunnerService) {
        this.containerService = containerService;
        this.testProgramRunnerService = testProgramRunnerService;
    }

    @Operation(summary = "Get list of running containers",
            description = "Returns list of running containers including their IDs and image names")
    @GetMapping("/v1/containers")
    public List<GetContainerResponse> getContainers() {
        return containerService.getContainers();
    }

    @Operation(summary = "Runs a given test program and returns result of its execution",
            description = "Creates a new image and container for test program, runs it and returns result of its " +
                    "execution.")
    @GetMapping("/v1/run")
    public String runTestProgram() {
        return testProgramRunnerService.runTestProgram();
    }
}
