package com.epam.xstack.dao.training_type_dao.impl;

import com.epam.xstack.aspects.training_type.annotations.SaveTrainingTypeAspectAnnotation;
import com.epam.xstack.aspects.training_type.annotations.SelectAllTrainingTypeAspectAnnotation;
import com.epam.xstack.dao.training_type_dao.TrainingTypeDAO;
import com.epam.xstack.mapper.training_type_mapper.TrainingTypeMapper;
import com.epam.xstack.models.dto.training_type_dto.TrainingTypeDTO;
import com.epam.xstack.models.entity.TrainingType;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TrainingTypeDAOImpl implements TrainingTypeDAO {
    private final SessionFactory sessionFactory;
    private final TrainingTypeMapper trainingTypeMapper;

    @Override
    @Transactional
    @SaveTrainingTypeAspectAnnotation
    public TrainingTypeDTO save(TrainingTypeDTO trainingTypeDTO) {
        Session session = sessionFactory.getCurrentSession();
        TrainingType trainingType = trainingTypeMapper.toEntity(trainingTypeDTO);
        session.save(trainingType);

        return trainingTypeMapper.toDto(trainingType);
    }

    @Override
    @Transactional
    @SelectAllTrainingTypeAspectAnnotation
    public List<TrainingTypeDTO> findAll() {
        Session session = sessionFactory.getCurrentSession();
        Query<TrainingType> trainingType = session.createQuery("FROM TrainingType", TrainingType.class);
        List<TrainingType> resultList = trainingType.getResultList();

        return trainingTypeMapper.toDtos(resultList);
    }
}
