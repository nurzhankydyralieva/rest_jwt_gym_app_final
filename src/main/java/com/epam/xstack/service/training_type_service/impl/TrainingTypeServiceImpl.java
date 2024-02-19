package com.epam.xstack.service.training_type_service.impl;

import com.epam.xstack.dao.training_type_dao.TrainingTypeDAO;
import com.epam.xstack.models.dto.training_type_dto.TrainingTypeDTO;
import com.epam.xstack.service.training_type_service.TrainingTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingTypeServiceImpl implements TrainingTypeService {
    private final TrainingTypeDAO trainingTypeDAO;

    @Override
    public TrainingTypeDTO save(TrainingTypeDTO trainingTypeDTO) {
        return trainingTypeDAO.save(trainingTypeDTO);
    }

    @Override
    public List<TrainingTypeDTO> findAll() {
        return trainingTypeDAO.findAll();
    }
}
