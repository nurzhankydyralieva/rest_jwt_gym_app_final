package com.epam.xstack.dao.trainee_dao.impl;

import com.epam.xstack.exceptions.dao_exceptions.UserNameNotExistsException;
import com.epam.xstack.mapper.trainee_mapper.*;
import com.epam.xstack.models.dto.trainee_dto.request.*;
import com.epam.xstack.models.dto.trainee_dto.response.*;
import com.epam.xstack.models.entity.Trainee;
import com.epam.xstack.models.entity.Training;
import com.epam.xstack.models.enums.Code;
import com.epam.xstack.validation.ActivationValidator;
import com.epam.xstack.validation.UserNameExistenceValidator;
import com.epam.xstack.validation.generator.PasswordUserNameGenerator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TraineeDAOImplTest {
    @InjectMocks
    private TraineeDAOImpl service;
    @Mock
    private SessionFactory sessionFactory;
    @Mock
    private Session session;
    @Mock
    private Trainee trainee;
    @Mock
    private PasswordUserNameGenerator generator;
    @Mock
    private UserNameExistenceValidator checkUserNameExistence;
    @Mock
    private ActivationValidator checkActivation;
    @Mock
    private TraineeRegistrationRequestMapper registrationRequestMapper;
    @Mock
    private TraineeTrainingsListMapper traineeTrainingsListMapper;
    @Mock
    private TraineeActivateDeActivateMapper activateDeActivateTraineeMapper;
    @Mock
    private TraineeProfileUpdateRequestMapper updateTraineeProfileRequestMapper;
    @Mock
    private TraineeProfileSelectRequestMapper getTraineeProfileRequestMapper;
    @Mock
    private TraineesTrainerListUpdateMapper traineesTrainerListUpdateMapper;

    @Test
    public void testShouldSaveTrainee() {
        TraineeRegistrationRequestDTO requestDTO = new TraineeRegistrationRequestDTO();
        requestDTO.setFirstName("Maxim");
        requestDTO.setLastName("Gorky");

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(registrationRequestMapper.toEntity(requestDTO)).thenReturn(new Trainee());
        when(generator.generateRandomPassword()).thenReturn("P5%^YrXKK1");
        when(generator.generateUserName("Maxim", "Gorky")).thenReturn("Maxim.Gorky");

        TraineeRegistrationResponseDTO responseDTO = service.saveTrainee(requestDTO);

        verify(sessionFactory).getCurrentSession();
        verify(registrationRequestMapper).toEntity(requestDTO);
        verify(checkUserNameExistence).userNameExists("Maxim.Gorky");
        assertEquals("Maxim.Gorky", responseDTO.getUserName());
        assertEquals("P5%^YrXKK1", responseDTO.getPassword());
    }

    @Test
    public void testShouldSelectTraineeTrainingsList() {
        UUID id = UUID.randomUUID();
        List<Training> trainings = Arrays.asList(new Training(), new Training());
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.get(Trainee.class, id)).thenReturn(trainee);

        TraineeTrainingsListRequestDTO requestDTO = new TraineeTrainingsListRequestDTO();
        requestDTO.setUserName("Alexandr.Pushkin");
        when(traineeTrainingsListMapper.toEntity(requestDTO)).thenReturn(trainee);
        when(trainee.getUserName()).thenReturn("Alexandr.Pushkin");
        when(trainee.getTrainings()).thenReturn(trainings);

        TraineeTrainingsListResponseDTO responseDTO = service.selectTraineeTrainingsList(id, requestDTO);

        verify(session).get(Trainee.class, id);
        verify(trainee).getUserName();
        verify(sessionFactory).getCurrentSession();
        assertNotNull(responseDTO);
        assertEquals(trainings.size(), responseDTO.getTrainings().size());
    }

    @Test
    public void testShouldActivateOrDeActivateTrainee() {
        UUID id = UUID.randomUUID();
        Trainee traineeInDb = new Trainee();
        traineeInDb.setIsActive(false);
        traineeInDb.setUserName("Alexandr.Pushkin");
        TraineeActivateDeActivateDTO dto = new TraineeActivateDeActivateDTO();
        dto.setIsActive(true);
        dto.setUserName("Alexandr.Pushkin");

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(activateDeActivateTraineeMapper.toEntity(dto)).thenReturn(traineeInDb);
        when(session.get(Trainee.class, id)).thenReturn(traineeInDb);
        doNothing().when(checkActivation).checkActiveOrNotTraineeActive(id, dto);

        TraineeOkResponseDTO responseDTO = service.activateDe_ActivateTrainee(id, dto);

        verify(session).update(any(Trainee.class));
        verify(checkActivation).checkActiveOrNotTraineeActive(id, dto);
        assertNotNull(responseDTO);
        assertEquals(Code.STATUS_200_OK, responseDTO.getCode());
        assertEquals("Activate DeActivate Trainee updated", responseDTO.getResponse());
    }

    @Test
    public void testShouldUpdateTraineeProfile() {
        UUID traineeId = UUID.randomUUID();
        TraineeProfileUpdateRequestDTO requestDTO = new TraineeProfileUpdateRequestDTO();
        Trainee traineeToBeUpdated = new Trainee();
        traineeToBeUpdated.setId(traineeId);
        traineeToBeUpdated.setFirstName("Scarlett");
        traineeToBeUpdated.setLastName("Johansson");
        traineeToBeUpdated.setIsActive(false);

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.get(Trainee.class, traineeId)).thenReturn(traineeToBeUpdated);
        when(updateTraineeProfileRequestMapper.toEntity(requestDTO)).thenReturn(traineeToBeUpdated);

        TraineeProfileUpdateResponseDTO responseDTO = service.updateTraineeProfile(traineeId, requestDTO);

        verify(session).update(traineeToBeUpdated);
        assertNotNull(responseDTO);
    }

    @Test
    public void testShouldSelectTraineeProfileByUserName() {
        UUID id = UUID.randomUUID();
        Trainee traineeInDb = new Trainee();
        traineeInDb.setFirstName("Emilia");
        traineeInDb.setLastName("Clarke");
        traineeInDb.setUserName("Emilia.Clarke");
        traineeInDb.setIsActive(true);
        TraineeProfileSelectRequestDTO requestDTO = new TraineeProfileSelectRequestDTO();
        requestDTO.setUserName("Emilia.Clarke");

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.get(Trainee.class, id)).thenReturn(traineeInDb);
        when(getTraineeProfileRequestMapper.toEntity(requestDTO)).thenReturn(traineeInDb);
        when(getTraineeProfileRequestMapper.toDto(traineeInDb)).thenReturn(requestDTO);

        TraineeProfileSelectResponseDTO responseDTO = service.selectTraineeProfileByUserName(id, requestDTO);

        assertEquals("Emilia", responseDTO.getFirstName());
        assertEquals("Clarke", responseDTO.getLastName());
        assertTrue(responseDTO.getIsActive());
        assertNotNull(responseDTO);
        verify(getTraineeProfileRequestMapper).toDto(traineeInDb);
        verify(sessionFactory).getCurrentSession();
    }

    @Test
    public void testShouldDeleteTraineeByUserName() {
        UUID id = UUID.randomUUID();
        Trainee traineeInDb = new Trainee();
        traineeInDb.setFirstName("Emilia");
        traineeInDb.setLastName("Clarke");
        traineeInDb.setUserName("Emilia.Clarke");
        traineeInDb.setIsActive(true);
        TraineeProfileSelectRequestDTO requestDTO = new TraineeProfileSelectRequestDTO();
        requestDTO.setUserName("Emilia.Clarke");

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.get(Trainee.class, id)).thenReturn(traineeInDb);
        TraineeOkResponseDTO responseDTO = service.deleteTraineeByUserName(id, requestDTO);

        assertEquals(Code.STATUS_200_OK, responseDTO.getCode());
        assertEquals("Trainee is deleted from database", responseDTO.getResponse());
    }

    @Test
    public void testShouldSelectNotAssignedOnTraineeActiveTrainers() {
        UUID id = UUID.randomUUID();
        TraineesTrainerActiveAndNotAssignedRequestDTO requestDTO = new TraineesTrainerActiveAndNotAssignedRequestDTO();
        requestDTO.setUserName("Johnny.Depp");
        Trainee traineeActive = new Trainee();
        traineeActive.setId(id);
        traineeActive.setUserName("Johnny.Depp");
        traineeActive.setIsActive(true);
        traineeActive.setIsAssigned(false);

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.get(Trainee.class, id)).thenReturn(traineeActive);

        TraineesTrainerActiveAndNotAssignedResponseDTO responseDTO = service.selectNotAssignedOnTraineeActiveTrainers(id, requestDTO);

        assertNotNull(responseDTO);
        assertTrue(responseDTO.getTrainers().isEmpty());
    }

    @Test
    public void testShouldUpdateTraineesTrainerList() {
        UUID id = UUID.randomUUID();
        TraineesTrainerListUpdateRequestDTO requestDTO = new TraineesTrainerListUpdateRequestDTO();
        requestDTO.setUserName("Oscar.Wild");
        Trainee traineeToBeUpdated = mock(Trainee.class);

        when(sessionFactory.openSession()).thenReturn(session);
        when(session.get(Trainee.class, id)).thenReturn(traineeToBeUpdated);
        when(traineeToBeUpdated.getId()).thenReturn(null);
        when(traineesTrainerListUpdateMapper.toEntity(requestDTO)).thenReturn(trainee);

        assertThrows(UserNameNotExistsException.class, () -> {
            service.updateTraineesTrainerList(id, requestDTO);
        });
    }
}

