package com.epam.xstack.dao.trainer_dao.impl;

import com.epam.xstack.aspects.trainer_aspects.authentication_aspects.annotations.*;
import com.epam.xstack.dao.trainer_dao.TrainerDAO;
import com.epam.xstack.exceptions.dao_exceptions.UserIdNotFoundException;
import com.epam.xstack.exceptions.dao_exceptions.UserNameNotExistsException;
import com.epam.xstack.mapper.trainee_mapper.TraineeMapper;
import com.epam.xstack.mapper.trainer_mapper.*;
import com.epam.xstack.mapper.training_mapper.TrainingListMapper;
import com.epam.xstack.models.dto.trainer_dto.request.*;
import com.epam.xstack.models.dto.trainer_dto.response.*;
import com.epam.xstack.models.entity.Trainer;
import com.epam.xstack.models.enums.Code;

import com.epam.xstack.exceptions.validation.ActivationValidator;
import com.epam.xstack.exceptions.validation.UserNameExistenceValidator;
import com.epam.xstack.exceptions.validation.generator.PasswordUserNameGenerator;
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
public class TrainerDAOImpl implements TrainerDAO {
    private final SessionFactory sessionFactory;
    private final TrainerRegistrationRequestMapper registrationRequestMapper;
    private final TrainerProfileSelectRequestMapper getTrainerProfileRequestMapper;
    private final TrainerProfileUpdateRequestMapper updateTrainerProfileRequestMapper;
    private final TrainerActivateDeActivateMapper activateDeActivateTrainerMapper;
    private final TrainerTrainingsListMapper trainerTrainingsListMapper;
    private final PasswordUserNameGenerator generator;
    private final UserNameExistenceValidator checkUserNameExistence;
    private final ActivationValidator checkActivation;

    @Override
    @Transactional
    @SelectTrainerTrainingsListAspectAnnotation
    public TrainerTrainingsListResponseDTO selectTrainerTrainingsList(UUID id, TrainerTrainingsListRequestDTO requestDTO) {
        Session session = sessionFactory.getCurrentSession();
        Trainer trainerId = session.get(Trainer.class, id);
        trainerTrainingsListMapper.toEntity(requestDTO);

        if (trainerId != null && trainerId.getUserName().equals(requestDTO.getUserName())) {
            return TrainerTrainingsListResponseDTO
                    .builder()
                    .trainings(TrainingListMapper.INSTANCE.toDtos(trainerId.getTrainings()))
                    .build();
        } else {
            throw UserIdNotFoundException.builder()
                    .codeStatus(Code.USER_ID_NOT_FOUND)
                    .message("User id:  " + trainerId.getId() + " or user name: " + trainerId.getUserName() + " not correct.")
                    .httpStatus(HttpStatus.CONFLICT)
                    .build();
        }
    }

    @Override
    @Transactional
    @ActivateDe_ActivateTrainerAspectAnnotation
    public TrainerOkResponseDTO activateDe_ActivateTrainer(UUID id, TrainerActivateDeActivateDTO dto) {
        Session session = sessionFactory.getCurrentSession();
        Trainer trainer = activateDeActivateTrainerMapper.toEntity(dto);
        Trainer existingTrainer = session.get(Trainer.class, id);

        checkActivation.checkActiveOrNotTrainerActive(id, dto);

        existingTrainer.setIsActive(dto.getIsActive());
        session.update(existingTrainer);
        activateDeActivateTrainerMapper.toDto(trainer);
        return TrainerOkResponseDTO
                .builder()
                .code(Code.STATUS_200_OK)
                .response("Activate DeActivate Trainer updated")
                .build();
    }

    @Override
    @Transactional
    @UpdateTrainerProfileAspectAnnotation
    public TrainerProfileUpdateResponseDTO updateTrainerProfile(UUID id, TrainerProfileUpdateRequestDTO requestDTO) {
        Session session = sessionFactory.getCurrentSession();
        Trainer trainer = updateTrainerProfileRequestMapper.toEntity(requestDTO);
        Trainer trainerToBeUpdated = session.get(Trainer.class, id);
        if (trainerToBeUpdated.getId() == id) {
            trainerToBeUpdated.setFirstName(requestDTO.getFirstName());
            trainerToBeUpdated.setLastName(requestDTO.getLastName());
            trainerToBeUpdated.setIsActive(requestDTO.getIsActive());

            session.update(trainerToBeUpdated);
            updateTrainerProfileRequestMapper.toDto(trainer);
        }

        return TrainerProfileUpdateResponseDTO
                .builder()
                .userName(trainerToBeUpdated.getUserName())
                .firstName(trainerToBeUpdated.getFirstName())
                .lastName(trainerToBeUpdated.getLastName())
                .specialization(trainerToBeUpdated.getSpecialization())
                .isActive(trainerToBeUpdated.getIsActive())
                .trainees(TraineeMapper.INSTANCE.toDtos(trainerToBeUpdated.getTraineeList()))
                .build();
    }

    @Override
    @Transactional
    @SelectTrainerProfileByUserNameAspectAnnotation
    public TrainerProfileSelectResponseDTO selectTrainerProfileByUserName(UUID id, TrainerProfileSelectRequestDTO requestDTO) {
        Session session = sessionFactory.getCurrentSession();
        Trainer trainer = getTrainerProfileRequestMapper.toEntity(requestDTO);
        Trainer trainerId = session.get(Trainer.class, id);

        if (trainerId.getUserName().equals(requestDTO.getUserName())) {
            getTrainerProfileRequestMapper.toDto(trainer);

            return TrainerProfileSelectResponseDTO
                    .builder()
                    .firstName(trainerId.getFirstName())
                    .lastName(trainerId.getLastName())
                    .specialization(trainerId.getSpecialization())
                    .isActive(trainerId.getIsActive())
                    .traineeList(TraineeMapper.INSTANCE.toDtos(trainerId.getTraineeList()))
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
    @SaveTraineeAspectAnnotation
    public TrainerRegistrationResponseDTO saveTrainer(TrainerRegistrationRequestDTO requestDTO) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Session session = sessionFactory.getCurrentSession();
        Trainer trainer = registrationRequestMapper.toEntity(requestDTO);
        String generatedPassword = generator.generateRandomPassword();
        String encodedPassword = passwordEncoder.encode(generatedPassword);
        String createdUserName = generator.generateUserName(requestDTO.getFirstName(), requestDTO.getLastName());

        trainer.setUserName(createdUserName);
        trainer.setFirstName(requestDTO.getFirstName());
        trainer.setLastName(requestDTO.getLastName());
        trainer.setPassword(encodedPassword);
        trainer.setIsActive(true);
        checkUserNameExistence.userNameExists(createdUserName);
        session.save(trainer);

        return TrainerRegistrationResponseDTO
                .builder()
                .userName(trainer.getUserName())
                .password(generatedPassword)
                .build();
    }
}
