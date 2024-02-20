package com.epam.xstack.dao.training_type_dao.impl;

import com.epam.xstack.mapper.training_type_mapper.TrainingTypeMapper;
import com.epam.xstack.models.dto.training_type_dto.TrainingTypeDTO;
import com.epam.xstack.models.entity.TrainingType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TrainingTypeDAOImplTest {
    @InjectMocks
    private TrainingTypeDAOImpl service;
    @Mock
    private SessionFactory sessionFactory;
    @Mock
    private Session session;
    @Mock
    private TrainingTypeMapper trainingTypeMapper;
    @Mock
    private Query<TrainingType> query;

    @Test
    public void testShouldSaveTrainingType() {
        TrainingTypeDTO inputDTO = new TrainingTypeDTO();
        TrainingType trainingTypeEntity = new TrainingType();
        TrainingTypeDTO outputDTO = new TrainingTypeDTO();
        inputDTO.setId(1L);
        inputDTO.setTrainingType("Swimming");

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(trainingTypeMapper.toEntity(inputDTO)).thenReturn(trainingTypeEntity);
        when(trainingTypeMapper.toDto(trainingTypeEntity)).thenReturn(outputDTO);

        TrainingTypeDTO result = service.save(inputDTO);

        verify(session).save(trainingTypeEntity);
        Assertions.assertEquals(outputDTO, result);
    }

    @Test
    public void testShouldFindAllTrainingType() {
        List<TrainingType> trainingTypes = Arrays.asList(new TrainingType(), new TrainingType());

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.createQuery("FROM TrainingType", TrainingType.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(trainingTypes);
        when(trainingTypeMapper.toDtos(trainingTypes)).thenReturn(Arrays.asList(new TrainingTypeDTO(), new TrainingTypeDTO()));

        List<TrainingTypeDTO> result = service.findAll();

        verify(sessionFactory).getCurrentSession();
        verify(session).createQuery("FROM TrainingType", TrainingType.class);
        verify(query).getResultList();
        verify(trainingTypeMapper).toDtos(trainingTypes);

        assertNotNull(result);
        assertEquals(2, trainingTypes.size());
    }
}
