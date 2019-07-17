package com.service;

import com.model.CustomResponse;

import java.util.concurrent.ConcurrentHashMap;


public class AccountCacheService {
    private ConcurrentHashMap<Integer, CustomResponse> customResponseCache = new ConcurrentHashMap<>();
    private static volatile AccountCacheService accountCacheService;

    private AccountCacheService() {
    }

    public static AccountCacheService getAccountCacheService() {
        AccountCacheService localInstance = accountCacheService;
        if (localInstance == null) {
            synchronized (AccountCacheService.class) {
                localInstance = accountCacheService;
                if (localInstance == null) {
                    accountCacheService = localInstance = new AccountCacheService();
                }
            }
        }
        return localInstance;
    }

    public void addOrUpdateCache(int id, CustomResponse response) {
        customResponseCache.put(id, response);
    }

    public CustomResponse getFromCache(int id) {
        return customResponseCache.get(id);
    }

    public void deleteFromCache(int id) {
        customResponseCache.remove(id);
    }
}
