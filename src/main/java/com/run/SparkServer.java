package com.run;

import com.controller.AccountController;
import com.controller.TransactionController;
import com.util.SessionFactorySingleton;
import spark.Spark;

public class SparkServer {
    public static void startApp(int port, int maxThreads) {
        Spark.port(port);
        Spark.threadPool(maxThreads);
        initControllers();
        Spark.after((req, res) -> res.type("application/json"));
        Spark.awaitInitialization();
    }

    private static void initControllers() {
        new AccountController();
        new TransactionController();
    }

    public static void stop() {
        Spark.stop();
        SessionFactorySingleton.shutDown();
    }
}
