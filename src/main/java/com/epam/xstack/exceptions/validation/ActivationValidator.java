package com.epam.xstack.exceptions.validation;

import com.epam.xstack.exceptions.dao_exceptions.UserNameOrPasswordNotCorrectException;
import com.epam.xstack.models.dto.trainee_dto.request.TraineeActivateDeActivateDTO;
import com.epam.xstack.models.dto.trainer_dto.request.TrainerActivateDeActivateDTO;
import com.epam.xstack.models.entity.Trainee;
import com.epam.xstack.models.entity.Trainer;
import com.epam.xstack.models.enums.Code;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivationValidator {
    private final SessionFactory sessionFactory;

    public void checkActiveOrNotTraineeActive(UUID id, TraineeActivateDeActivateDTO dto) {
        Session session = sessionFactory.getCurrentSession();
        Trainee existingTrainee = session.get(Trainee.class, id);

        if (!existingTrainee.getUserName().equals(dto.getUserName())) {
            throw UserNameOrPasswordNotCorrectException.builder()
                    .codeStatus(Code.REQUEST_VALIDATION_ERROR)
                    .message("User name " + dto.getUserName() + " not correct")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    public void checkActiveOrNotTrainerActive(UUID id, TrainerActivateDeActivateDTO dto) {
        Session session = sessionFactory.getCurrentSession();
        Trainer existingTrainer = session.get(Trainer.class, id);

        if (!existingTrainer.getUserName().equals(dto.getUserName())) {
            throw UserNameOrPasswordNotCorrectException.builder()
                    .codeStatus(Code.REQUEST_VALIDATION_ERROR)
                    .message("User name " + dto.getUserName() + " not correct")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }
}
