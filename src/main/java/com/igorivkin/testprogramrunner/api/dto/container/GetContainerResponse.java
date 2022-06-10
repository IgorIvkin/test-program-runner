package com.igorivkin.testprogramrunner.api.dto.container;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "List of containers response item")
public class GetContainerResponse {

    @Schema(description = "ID of container")
    private String id;

    @Schema(description = "Image ID")
    private String imageId;

    @Schema(description = "Image of container")
    private String image;

    @Schema(description = "Command to run container")
    private String command;
}
