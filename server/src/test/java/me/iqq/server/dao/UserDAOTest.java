package me.iqq.server.dao;

import me.iqq.server.dao.impl.UserDAOImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDAOTest {

    UserDAO userDAO = new UserDAOImpl();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void save() {
    }

    @Test
    void remove() {
    }

    @Test
    void update() {
    }

    @Test
    void find() {
    }

    @Test
    void findUserByIds() {
        for (var u : userDAO.findUserByIds("1234567", "2345678", "3456789")) {
            System.out.println(u);
        }
    }
}
