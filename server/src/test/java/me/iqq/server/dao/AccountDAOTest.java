package me.iqq.server.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AccountDAOTest {

    AccountDAO accountDAO = new AccountDAO();

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
        var account = accountDAO.findByUID("1234567");
        Assertions.assertNotNull(account);
        System.out.println(account);
    }

    @Test
    void findUserByIds() {
        var list = accountDAO.findByUIds("1234567", "2345678", "3456789");
        System.out.println(list.size());
        for (var u : list) {
            System.out.println(u);
            Assertions.assertNotNull(u);
        }
    }
}
