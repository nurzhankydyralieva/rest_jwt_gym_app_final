package com.epam.xstack.mapper.training_type_mapper;

import com.epam.xstack.models.dto.training_type_dto.TrainingTypeDTO;
import com.epam.xstack.models.entity.TrainingType;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrainingTypeMapper {
    TrainingTypeDTO toDto(TrainingType trainingType);

    TrainingType toEntity(TrainingTypeDTO requestDTO);

    List<TrainingType> toEntities(List<TrainingTypeDTO> requestDTOS);


    List<TrainingTypeDTO> toDtos(List<TrainingType> trainingType);
}
