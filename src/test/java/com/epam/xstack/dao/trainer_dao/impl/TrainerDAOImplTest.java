package com.epam.xstack.dao.trainer_dao.impl;

import com.epam.xstack.mapper.trainer_mapper.*;
import com.epam.xstack.models.dto.trainer_dto.request.*;
import com.epam.xstack.models.dto.trainer_dto.response.*;
import com.epam.xstack.models.entity.Trainer;
import com.epam.xstack.models.entity.Training;
import com.epam.xstack.models.enums.Code;
import com.epam.xstack.validation.ActivationValidator;
import com.epam.xstack.validation.UserNameExistenceValidator;
import com.epam.xstack.validation.generator.PasswordUserNameGenerator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TrainerDAOImplTest {
    @InjectMocks
    private TrainerDAOImpl service;
    @Mock
    private SessionFactory sessionFactory;
    @Mock
    private Session session;
    @Mock
    private Trainer trainer;
    @Mock
    private PasswordUserNameGenerator generator;
    @Mock
    private UserNameExistenceValidator checkUserNameExistence;
    @Mock
    private ActivationValidator checkActivation;
    @Mock
    private TrainerTrainingsListMapper trainerTrainingsListMapper;
    @Mock
    private TrainerRegistrationRequestMapper trainerRegistrationRequestMapper;
    @Mock
    private TrainerActivateDeActivateMapper activateDeActivateTrainerMapper;
    @Mock
    private TrainerProfileUpdateRequestMapper updateTrainerProfileRequestMapper;
    @Mock
    private TrainerProfileSelectRequestMapper getTrainerProfileRequestMapper;

    @Test
    public void testShouldSaveTrainer() {
        TrainerRegistrationRequestDTO requestDTO = new TrainerRegistrationRequestDTO();
        requestDTO.setLastName("Khayyam");
        requestDTO.setFirstName("Omar");

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(trainerRegistrationRequestMapper.toEntity(requestDTO)).thenReturn(new Trainer());
        when(generator.generateRandomPassword()).thenReturn("P5%^YrXKK1");
        when(generator.generateUserName("Omar", "Khayyam")).thenReturn("Omar.Khayyam");

        TrainerRegistrationResponseDTO responseDTO = service.saveTrainer(requestDTO);

        verify(sessionFactory).getCurrentSession();
        verify(trainerRegistrationRequestMapper).toEntity(requestDTO);
        verify(checkUserNameExistence).userNameExists("Omar.Khayyam");
        assertEquals("Omar.Khayyam", responseDTO.getUserName());
        assertEquals("P5%^YrXKK1", responseDTO.getPassword());
    }

    @Test
    public void testShouldUpdateTrainerProfile() {
        UUID trainerId = UUID.randomUUID();
        TrainerProfileUpdateRequestDTO requestDTO = new TrainerProfileUpdateRequestDTO();
        Trainer trainerToBeUpdated = new Trainer();
        trainerToBeUpdated.setId(trainerId);
        trainerToBeUpdated.setFirstName("Marilyn");
        trainerToBeUpdated.setLastName("Monroe");
        trainerToBeUpdated.setIsActive(true);

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.get(Trainer.class, trainerId)).thenReturn(trainerToBeUpdated);
        when(updateTrainerProfileRequestMapper.toEntity(requestDTO)).thenReturn(trainerToBeUpdated);

        TrainerProfileUpdateResponseDTO responseDTO = service.updateTrainerProfile(trainerId, requestDTO);

        verify(session).update(trainerToBeUpdated);
        assertNotNull(responseDTO);
    }

    @Test
    public void testShouldActivateOrDeActivateTrainer() {
        UUID id = UUID.randomUUID();
        Trainer trainerInDb = new Trainer();
        trainerInDb.setIsActive(false);
        trainerInDb.setUserName("Hafez.Shiraz");
        TrainerActivateDeActivateDTO dto = new TrainerActivateDeActivateDTO();
        dto.setIsActive(true);
        dto.setUserName("Hafez.Shiraz");

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(activateDeActivateTrainerMapper.toEntity(dto)).thenReturn(trainerInDb);
        when(session.get(Trainer.class, id)).thenReturn(trainerInDb);
        doNothing().when(checkActivation).checkActiveOrNotTrainerActive(id, dto);

        TrainerOkResponseDTO responseDTO = service.activateDe_ActivateTrainer(id, dto);

        verify(session).update(any(Trainer.class));
        verify(checkActivation).checkActiveOrNotTrainerActive(id, dto);
        assertNotNull(responseDTO);
        assertEquals(Code.STATUS_200_OK, responseDTO.getCode());
        assertEquals("Activate DeActivate Trainer updated", responseDTO.getResponse());
    }

    @Test
    public void testShouldSelectTrainerTrainingsList() {
        UUID id = UUID.randomUUID();
        List<Training> trainings = Arrays.asList(new Training(), new Training());
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.get(Trainer.class, id)).thenReturn(trainer);

        TrainerTrainingsListRequestDTO requestDTO = new TrainerTrainingsListRequestDTO();
        requestDTO.setUserName("Hafez.Shiraz");
        when(trainerTrainingsListMapper.toEntity(requestDTO)).thenReturn(trainer);
        when(trainer.getUserName()).thenReturn("Hafez.Shiraz");
        when(trainer.getTrainings()).thenReturn(trainings);

        TrainerTrainingsListResponseDTO responseDTO = service.selectTrainerTrainingsList(id, requestDTO);

        verify(session).get(Trainer.class, id);
        verify(trainer).getUserName();
        verify(sessionFactory).getCurrentSession();
        assertNotNull(responseDTO);
        assertEquals(trainings.size(), responseDTO.getTrainings().size());
    }

    @Test
    public void testShouldSelectTrainerProfileByUserName() {
        UUID id = UUID.randomUUID();
        Trainer trainerInDb = new Trainer();
        trainerInDb.setFirstName("Marilyn");
        trainerInDb.setLastName("Monro");
        trainerInDb.setUserName("Marilyn.Monro");
        trainerInDb.setIsActive(true);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.get(Trainer.class, id)).thenReturn(trainerInDb);

        TrainerProfileSelectRequestDTO requestDTO = new TrainerProfileSelectRequestDTO();
        requestDTO.setUserName("Marilyn.Monro");
        when(getTrainerProfileRequestMapper.toEntity(requestDTO)).thenReturn(trainerInDb);
        when(getTrainerProfileRequestMapper.toDto(trainerInDb)).thenReturn(requestDTO);

        TrainerProfileSelectResponseDTO responseDTO = service.selectTrainerProfileByUserName(id, requestDTO);

        assertEquals("Marilyn", responseDTO.getFirstName());
        assertEquals("Monro", responseDTO.getLastName());
        assertTrue(responseDTO.getIsActive());
        assertNotNull(responseDTO);
        verify(getTrainerProfileRequestMapper).toDto(trainerInDb);
        verify(sessionFactory).getCurrentSession();
    }
}
