package com.epam.xstack.dao.training_dao;

import com.epam.xstack.models.dto.training_dto.request.TrainingSaveRequestDTO;
import com.epam.xstack.models.dto.training_dto.response.TrainingSaveResponseDTO;

public interface TrainingDAO {
     TrainingSaveResponseDTO saveTraining(TrainingSaveRequestDTO requestDTO);
}
