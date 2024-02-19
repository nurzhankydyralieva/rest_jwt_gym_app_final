package com.epam.xstack.dao.trainee_dao.impl;

import com.epam.xstack.aspects.trainee_aspects.dao_aspects.annotations.*;
import com.epam.xstack.dao.trainee_dao.TraineeDAO;
import com.epam.xstack.exceptions.dao_exceptions.UserIdNotFoundException;
import com.epam.xstack.exceptions.dao_exceptions.UserNameNotExistsException;
import com.epam.xstack.mapper.trainee_mapper.*;
import com.epam.xstack.mapper.trainer_mapper.TrainerMapper;
import com.epam.xstack.mapper.training_mapper.TraineeTrainingMapper;
import com.epam.xstack.models.dto.trainee_dto.request.*;
import com.epam.xstack.models.dto.trainee_dto.response.*;
import com.epam.xstack.models.entity.Trainee;
import com.epam.xstack.models.enums.Code;
import com.epam.xstack.validation.ActivationValidator;
import com.epam.xstack.validation.UserNameExistenceValidator;
import com.epam.xstack.validation.generator.PasswordUserNameGenerator;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TraineeDAOImpl implements TraineeDAO {
    private final SessionFactory sessionFactory;
    private final UserNameExistenceValidator checkUserNameExistence;
    private final ActivationValidator checkActivation;
    private final PasswordUserNameGenerator generator;
    private final TraineeRegistrationRequestMapper registrationRequestMapper;
    private final TraineeProfileSelectRequestMapper getTraineeProfileRequestMapper;
    private final TraineeProfileUpdateRequestMapper updateTraineeProfileRequestMapper;
    private final TraineeActivateDeActivateMapper activateDeActivateTraineeMapper;
    private final TraineeTrainingsListMapper traineeTrainingsListMapper;
    private final TraineesTrainerListUpdateMapper traineesTrainerListUpdateMapper;

    @Override
    @Transactional
    @SaveTraineeAspectAnnotation
    public TraineeRegistrationResponseDTO saveTrainee(TraineeRegistrationRequestDTO requestDTO) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Session session = sessionFactory.getCurrentSession();
        Trainee trainee = registrationRequestMapper.toEntity(requestDTO);
        String generatedPassword = generator.generateRandomPassword();
        String encodedPassword = passwordEncoder.encode(generatedPassword);
        String createdUserName = generator.generateUserName(requestDTO.getFirstName(), requestDTO.getLastName());

        trainee.setUserName(createdUserName);
        trainee.setFirstName(requestDTO.getFirstName());
        trainee.setLastName(requestDTO.getLastName());
        trainee.setPassword(encodedPassword);
        trainee.setIsActive(true);

        checkUserNameExistence.userNameExists(createdUserName);

        session.save(trainee);
        return TraineeRegistrationResponseDTO
                .builder()
                .userName(trainee.getUserName())
                .password(generatedPassword)
                .build();
    }

    @Override
    @Transactional
    @SelectTraineeProfileByUserNameAspectAnnotation
    public TraineeProfileSelectResponseDTO selectTraineeProfileByUserName(UUID id, TraineeProfileSelectRequestDTO requestDTO) {
        Session session = sessionFactory.getCurrentSession();
        Trainee trainee = getTraineeProfileRequestMapper.toEntity(requestDTO);
        Trainee traineeId = session.get(Trainee.class, id);

        if (traineeId.getUserName().equals(requestDTO.getUserName())) {
            getTraineeProfileRequestMapper.toDto(trainee);

            return TraineeProfileSelectResponseDTO
                    .builder()
                    .firstName(traineeId.getFirstName())
                    .lastName(traineeId.getLastName())
                    .address(traineeId.getAddress())
                    .isActive(traineeId.getIsActive())
                    .dateOfBirth(traineeId.getDateOfBirth())
                    .trainers(TrainerMapper.INSTANCE.toDtos(traineeId.getTrainers()))
                    .build();
        } else {
            throw UserNameNotExistsException.builder()
                    .codeStatus(Code.USER_NOT_FOUND)
                    .message("User with name - " + requestDTO.getUserName() + " not exists in database")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @Override
    @Transactional
    @UpdateTraineeProfileAspectAnnotation
    public TraineeProfileUpdateResponseDTO updateTraineeProfile(UUID id, TraineeProfileUpdateRequestDTO requestDTO) {
        Session session = sessionFactory.getCurrentSession();
        Trainee trainee = updateTraineeProfileRequestMapper.toEntity(requestDTO);
        Trainee traineeToBeUpdated = session.get(Trainee.class, id);

        if (traineeToBeUpdated.getId() == id) {
            traineeToBeUpdated.setFirstName(requestDTO.getFirstName());
            traineeToBeUpdated.setLastName(requestDTO.getLastName());
            traineeToBeUpdated.setDateOfBirth(requestDTO.getDateOfBirth());
            traineeToBeUpdated.setAddress(requestDTO.getAddress());
            traineeToBeUpdated.setIsActive(requestDTO.getIsActive());

            session.update(traineeToBeUpdated);
            updateTraineeProfileRequestMapper.toDto(trainee);
        }

        return TraineeProfileUpdateResponseDTO
                .builder()
                .userName(traineeToBeUpdated.getUserName())
                .firstName(traineeToBeUpdated.getFirstName())
                .lastName(traineeToBeUpdated.getLastName())
                .dateOfBirth(traineeToBeUpdated.getDateOfBirth())
                .address(traineeToBeUpdated.getAddress())
                .isActive(traineeToBeUpdated.getIsActive())
                .trainers(TrainerMapper.INSTANCE.toDtos(traineeToBeUpdated.getTrainers()))
                .build();
    }

    @Override
    @Transactional
    @ActivateDe_ActivateTraineeAspectAnnotation
    public TraineeOkResponseDTO activateDe_ActivateTrainee(UUID id, TraineeActivateDeActivateDTO dto) {
        Session session = sessionFactory.getCurrentSession();
        Trainee trainee = activateDeActivateTraineeMapper.toEntity(dto);
        Trainee existingTrainee = session.get(Trainee.class, id);

        checkActivation.checkActiveOrNotTraineeActive(id, dto);

        existingTrainee.setIsActive(dto.getIsActive());
        session.update(existingTrainee);
        activateDeActivateTraineeMapper.toDto(trainee);
        return TraineeOkResponseDTO
                .builder()
                .code(Code.STATUS_200_OK)
                .response("Activate DeActivate Trainee updated")
                .build();

    }

    @Override
    @Transactional
    @DeleteTraineeByUserNameAspectAnnotation
    public TraineeOkResponseDTO deleteTraineeByUserName(UUID id, TraineeProfileSelectRequestDTO requestDTO) {
        Session session = sessionFactory.getCurrentSession();
        Trainee traineeId = session.get(Trainee.class, id);

        if (traineeId.getUserName().equals(requestDTO.getUserName())) {
            session.remove(traineeId);
            return TraineeOkResponseDTO
                    .builder()
                    .response("Trainee is deleted from database")
                    .code(Code.STATUS_200_OK)
                    .build();
        } else {
            throw UserNameNotExistsException
                    .builder()
                    .message("Trainee is not available in database")
                    .codeStatus(Code.USER_NOT_FOUND)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @Override
    @Transactional
    @SelectTraineeTrainingsListAspectAnnotation
    public TraineeTrainingsListResponseDTO selectTraineeTrainingsList(UUID id, TraineeTrainingsListRequestDTO requestDTO) {
        Session session = sessionFactory.getCurrentSession();
        Trainee traineeId = session.get(Trainee.class, id);
        Trainee trainee = traineeTrainingsListMapper.toEntity(requestDTO);

        if (traineeId != null && traineeId.getUserName().equals(requestDTO.getUserName())) {
            traineeTrainingsListMapper.toDto(trainee);
            return TraineeTrainingsListResponseDTO
                    .builder()
                    .trainings(TraineeTrainingMapper.INSTANCE.toDtos(traineeId.getTrainings()))
                    .build();
        } else {
            throw UserIdNotFoundException.builder()
                    .codeStatus(Code.USER_ID_NOT_FOUND)
                    .message("User id:  " + traineeId.getId() + " or user name: " + requestDTO.getUserName() + " not correct.")
                    .httpStatus(HttpStatus.CONFLICT)
                    .build();
        }
    }

    @Override
    @Transactional
    @UpdateTraineesTrainerListAspectAnnotation
    public TraineesTrainerListUpdateResponseDTO updateTraineesTrainerList(UUID id, TraineesTrainerListUpdateRequestDTO requestDTO) {
        Session session = sessionFactory.openSession();
        Trainee traineeToBeUpdated = session.get(Trainee.class, id);
        Trainee trainee = traineesTrainerListUpdateMapper.toEntity(requestDTO);

        if (traineeToBeUpdated.getId() == null || !traineeToBeUpdated.getUserName().equals(requestDTO.getUserName())) {
            throw UserNameNotExistsException
                    .builder()
                    .codeStatus(Code.STATUS_VALIDATION_ERROR)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("User or id not exists.")
                    .build();
        } else {
            traineeToBeUpdated.getTrainers().forEach(trainer -> {
                trainer.setUserName(traineeToBeUpdated.getUserName());
            });
            session.update(traineeToBeUpdated);
            traineesTrainerListUpdateMapper.toDto(trainee);
            return TraineesTrainerListUpdateResponseDTO
                    .builder()
                    .trainers(TrainerMapper.INSTANCE.toDtos(traineeToBeUpdated.getTrainers()))
                    .build();
        }
    }

    @Override
    @Transactional
    @SelectNotAssignedOnTraineeActiveTrainersAspectAnnotation
    public TraineesTrainerActiveAndNotAssignedResponseDTO selectNotAssignedOnTraineeActiveTrainers(UUID id, TraineesTrainerActiveAndNotAssignedRequestDTO requestDTO) {
        Session session = sessionFactory.getCurrentSession();
        Trainee traineeUserName = session.get(Trainee.class, id);
        if (traineeUserName.getUserName().equals(requestDTO.getUserName()) && traineeUserName.getIsActive() && !traineeUserName.getIsAssigned()) {
            return TraineesTrainerActiveAndNotAssignedResponseDTO
                    .builder()
                    .trainers(TrainerMapper.INSTANCE.toDtos(traineeUserName.getTrainers()))
                    .build();
        } else {
            throw new RuntimeException("Not available");
        }
    }

}
