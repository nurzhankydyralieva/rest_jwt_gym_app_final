package com.epam.xstack.validation.generator;

import com.epam.xstack.models.entity.User;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserNameInDB {
    private final SessionFactory sessionFactory;

    public User userNameExists(String userName) {

        Session session = sessionFactory.openSession();

        return session.createQuery("FROM User u WHERE u.userName=:userName", User.class)
                .setParameter("userName", userName).uniqueResult();
    }
}




