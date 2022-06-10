package com.igorivkin.testprogramrunner.mapper;

import com.github.dockerjava.api.model.Container;
import com.igorivkin.testprogramrunner.api.dto.container.GetContainerResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DockerContainerMapper {

    GetContainerResponse toDto(Container container);

    List<GetContainerResponse> toListOfDto(List<Container> containers);
}
