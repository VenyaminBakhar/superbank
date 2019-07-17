package com.service;

import com.exception.CreateAccountException;
import com.exception.DeleteAccountException;
import com.model.Account;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import com.util.SessionFactorySingleton;


public class AccountService {
    private static final SessionFactory sessionFactory = SessionFactorySingleton.getSessionFactoryInstance();

    private static volatile AccountService accountServiceInstance;

    private AccountService(){}

    public static AccountService getAccountServiceInstance() {
        AccountService localInstance = accountServiceInstance;
        if (localInstance == null) {
            synchronized (AccountService.class) {
                localInstance = accountServiceInstance;
                if (localInstance == null) {
                    accountServiceInstance = localInstance = new AccountService();
                }
            }
        }
        return localInstance;
    }

    public Account getAccountById(int id) {
        Account account;

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            account = session.get(Account.class, id);
            transaction.commit();
        }

        return account;
    }

    public synchronized void createAccount(Account account) throws CreateAccountException {
        account.validateForm();
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(account);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new CreateAccountException(e);
        }
    }

    public void deleteAccount(Account account) throws DeleteAccountException {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.delete(account);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DeleteAccountException(e);
        }
    }
}
