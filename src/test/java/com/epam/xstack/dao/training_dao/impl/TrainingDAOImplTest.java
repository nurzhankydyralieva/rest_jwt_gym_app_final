package com.epam.xstack.dao.training_dao.impl;

import com.epam.xstack.mapper.training_mapper.TrainingMapper;
import com.epam.xstack.models.dto.training_dto.request.TrainingSaveRequestDTO;
import com.epam.xstack.models.dto.training_dto.response.TrainingSaveResponseDTO;
import com.epam.xstack.models.entity.Training;
import com.epam.xstack.models.enums.Code;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TrainingDAOImplTest {
    @InjectMocks
    private TrainingDAOImpl service;
    @Mock
    private SessionFactory sessionFactory;
    @Mock
    private Session session;
    @Mock
    private TrainingMapper trainingMapper;

    @Test
    public void testShouldSaveTraining() {
        TrainingSaveRequestDTO requestDTO = new TrainingSaveRequestDTO();
        Training training = new Training();
        training.setTrainingName("Swimming");

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(trainingMapper.toEntity(requestDTO)).thenReturn(training);

        TrainingSaveResponseDTO responseDTO = service.saveTraining(requestDTO);

        assertNotNull(responseDTO);
        assertEquals("Training is saved", responseDTO.getResponse());
        assertEquals(Code.STATUS_200_OK, responseDTO.getCode());

        verify(sessionFactory).getCurrentSession();
        verify(trainingMapper).toEntity(requestDTO);
        verify(session).save(training);
        verify(trainingMapper).toDto(training);
    }
}
