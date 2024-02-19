package com.epam.xstack.service.trainee_service.impl;

import com.epam.xstack.dao.trainee_dao.TraineeDAO;
import com.epam.xstack.models.dto.trainee_dto.request.*;
import com.epam.xstack.models.dto.trainee_dto.response.*;
import com.epam.xstack.service.trainee_service.TraineeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TraineeServiceImpl implements TraineeService {
    private final TraineeDAO traineeDAO;

    @Override
    public TraineeRegistrationResponseDTO saveTrainee(TraineeRegistrationRequestDTO requestDTO) {
        return traineeDAO.saveTrainee(requestDTO);
    }
    @Override
    public TraineeProfileSelectResponseDTO selectTraineeProfileByUserName(UUID id, TraineeProfileSelectRequestDTO requestDTO) {
        return traineeDAO.selectTraineeProfileByUserName(id, requestDTO);
    }

    @Override
    public TraineeProfileUpdateResponseDTO updateTraineeProfile(UUID id, TraineeProfileUpdateRequestDTO requestDTO) {
        return traineeDAO.updateTraineeProfile(id, requestDTO);
    }

    @Override
    public TraineeOkResponseDTO activateDe_ActivateTrainee(UUID id, TraineeActivateDeActivateDTO dto) {
        return traineeDAO.activateDe_ActivateTrainee(id, dto);
    }

    @Override
    public TraineeOkResponseDTO deleteTraineeByUserName(UUID id, TraineeProfileSelectRequestDTO requestDTO) {
        return traineeDAO.deleteTraineeByUserName(id, requestDTO);
    }

    @Override
    public TraineeTrainingsListResponseDTO selectTraineeTrainingsList(UUID id, TraineeTrainingsListRequestDTO requestDTO) {
        return traineeDAO.selectTraineeTrainingsList(id, requestDTO);
    }

    @Override
    public TraineesTrainerListUpdateResponseDTO updateTraineesTrainerList(UUID id, TraineesTrainerListUpdateRequestDTO requestDTO) {
        return traineeDAO.updateTraineesTrainerList(id, requestDTO);
    }

    @Override
    public TraineesTrainerActiveAndNotAssignedResponseDTO selectNotAssignedOnTraineeActiveTrainers(UUID id, TraineesTrainerActiveAndNotAssignedRequestDTO requestDTO) {
        return traineeDAO.selectNotAssignedOnTraineeActiveTrainers(id, requestDTO);
    }
}
