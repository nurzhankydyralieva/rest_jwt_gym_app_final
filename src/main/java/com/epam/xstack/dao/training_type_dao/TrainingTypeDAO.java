package com.epam.xstack.dao.training_type_dao;

import com.epam.xstack.models.dto.training_type_dto.TrainingTypeDTO;

import java.util.List;

public interface TrainingTypeDAO {
    TrainingTypeDTO save(TrainingTypeDTO trainingTypeDTO);

    List<TrainingTypeDTO> findAll();
}
