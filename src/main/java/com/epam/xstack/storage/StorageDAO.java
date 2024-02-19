package com.epam.xstack.storage;

import com.epam.xstack.models.entity.Trainee;
import com.epam.xstack.models.entity.Trainer;
import lombok.RequiredArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class StorageDAO {
    private final SessionFactory sessionFactory;

    @PostConstruct
    public void init() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

//            User user = new User();
//            user.setUserName("Oscar.Wild");
//            user.setPassword("$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6");
            Trainer trainer1 = new Trainer();
            trainer1.setUserName("Alexandr.Pushkin");
            trainer1.setFirstName("Alexandr");
            trainer1.setLastName("Pushkin");
            trainer1.setIsActive(true);
            trainer1.setIsAssigned(false);
            trainer1.setPassword("$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6");
            trainer1.setCriteria("1 Trainer criteria");
////
//            Trainer trainer2 = new Trainer();
//            trainer2.setUserName("Andrea.Bocelli");
//            trainer2.setFirstName("Andrea");
//            trainer2.setLastName("Bocelli");
//            trainer2.setIsAssigned(true);
//            trainer2.setIsActive(false);
//            trainer2.setPassword("5Tsb6rCqux");
//            trainer2.setCriteria("2 Trainer criteria");
//
//
            Trainee trainee1 = new Trainee();
            trainee1.setFirstName("Sam");
            trainee1.setLastName("Smith");
            trainee1.setUserName("Sam.Smith");
            trainee1.setPassword("$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6");
            trainee1.setIsAssigned(false);
            trainee1.setIsActive(true);
            trainee1.setCriteria("1 Trainee criteria");
//
//            Trainee trainee2 = new Trainee();
//            trainee2.setFirstName("Maxim");
//            trainee2.setLastName("Gorky");
//            trainee2.setUserName("Maxim.Gorky");
//            trainee2.setPassword("gorky");
//            trainee2.setIsAssigned(false);
//            trainee2.setIsActive(false);
//            trainee2.setCriteria("2 Trainee criteria");
//
//            Trainee trainee3 = new Trainee();
//            trainee3.setFirstName("Sultan");
//            trainee3.setLastName("Suleiman");
//            trainee3.setUserName("Sultan.Suleiman");
//            trainee3.setPassword("sultan");
//            trainee3.setIsAssigned(false);
//            trainee3.setIsActive(true);
//            trainee3.setCriteria("3 Trainee criteria");
//
//            trainer1.getTraineeList().add(trainee1);
//            trainer1.getTraineeList().add(trainee2);
//            trainer1.getTraineeList().add(trainee3);
//
//            trainee1.getTrainers().add(trainer1);
//            trainee2.getTrainers().add(trainer1);
//            trainee3.getTrainers().add(trainer1);
//
//            trainer2.getTraineeList().add(trainee2);
//            trainer2.getTraineeList().add(trainee3);
//
//            trainee2.getTrainers().add(trainer2);
//            trainee3.getTrainers().add(trainer2);
//
            session.save(trainer1);
            session.save(trainee1);
//            session.save(trainer2);
//
//            TrainingType trainingType1 = new TrainingType();
//            trainingType1.setTrainingType("Continuous training");
//            TrainingType trainingType2 = new TrainingType();
//            trainingType2.setTrainingType("Circuit Training");
//            TrainingType trainingType3 = new TrainingType();
//            trainingType3.setTrainingType("Flexibility Training");
//            session.save(trainingType1);
//            session.save(trainingType2);
//            session.save(trainingType3);
//
//            trainer1.setSpecialization(trainingType1);
//            trainer2.setSpecialization(trainingType2);
//              session.save(user);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }
}
