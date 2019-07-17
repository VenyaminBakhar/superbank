package com.service;


import com.exception.InvalidTransactionParametersException;
import com.model.Account;
import com.model.TransactionModel;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.util.SessionFactorySingleton;

import java.util.Date;
import java.util.List;


public class TransactionService {
    private static final String HISTORY_QUERY = "FROM TRANSACTION_HISTORY T WHERE T.idFrom =:idFrom OR T.idTo =:idTo ORDER BY T.transactionDate";

    private static final SessionFactory sessionFactory = SessionFactorySingleton.getSessionFactoryInstance();
    private static volatile TransactionService transactionServiceInstance;

    private TransactionService() {}

    public static TransactionService getTransactionServiceInstance() {
        TransactionService localInstance = transactionServiceInstance;
        if (localInstance == null) {
            synchronized (TransactionService.class) {
                localInstance = transactionServiceInstance;
                if (localInstance == null) {
                    transactionServiceInstance = localInstance = new TransactionService();
                }
            }
        }
        return localInstance;
    }

    public synchronized void doTransaction(TransactionModel transactionModel) throws InvalidTransactionParametersException {
        if (!TransactionValidator.isValidTransaction(transactionModel)) {
            throw new InvalidTransactionParametersException();
        }

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Account accountFrom = session.load(Account.class, transactionModel.getIdFrom());
            double accountFromNewBalance = accountFrom.getBalance() - transactionModel.getAmount();
            accountFrom.setBalance(accountFromNewBalance);
            session.saveOrUpdate(accountFrom);

            Account accountTo = session.load(Account.class, transactionModel.getIdTo());
            double accountToNewBalance = accountTo.getBalance() + transactionModel.getAmount();
            accountTo.setBalance(accountToNewBalance);
            session.saveOrUpdate(accountTo);

            transactionModel.setTransactionDate(new Date());

            session.persist(transactionModel);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

            throw e;
        }
    }

    public List<TransactionModel> getTransactionHistory(int accountId) {
        List<TransactionModel> transactionModels;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery(HISTORY_QUERY);
            query.setParameter("idFrom", accountId);
            query.setParameter("idTo", accountId);
            transactionModels = query.list();
            transaction.commit();
        }

        return transactionModels;
    }


}
