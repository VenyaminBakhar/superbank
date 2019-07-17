import com.exception.CreateAccountException;
import com.model.Account;
import com.model.CustomResponse;
import com.run.SparkServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import com.util.AccountTestUtil;
import com.util.RestRequestsUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static com.constants.MessageConstants.TRANSACTION_SUCCESS;
import static org.junit.Assert.assertEquals;

public class TransactionAPITests {
    private static final int SPARK_TEST_PORT = 3535;
    private static final int SPARK_THREADS_MAX_COUNT = 30;

    private static final String TRANSACTION_API_URL = "http://localhost:" + SPARK_TEST_PORT + "/transactions";

    @BeforeClass
    public static void setUp() throws CreateAccountException {
        SparkServer.startApp(SPARK_TEST_PORT, SPARK_THREADS_MAX_COUNT);
        AccountTestUtil.populateAccountDBForTransactionAPITests();
    }

    @AfterClass
    public static void tearDown() {
        SparkServer.stop();
    }

    @Test
    public void testDoTransaction_correctTransaction_ShouldCompleteTransactionCorrectlyAndShowMessage() throws IOException {
        //GIVEN
        int idFrom = 1;
        int idTo = 2;
        double amount = 2000.1;

        Account accountFrom = AccountTestUtil.getAccountFromDBById(idFrom);
        assert accountFrom != null;
        Account accountTo = AccountTestUtil.getAccountFromDBById(idTo);
        assert accountTo != null;

        assert accountFrom.getBalance() > amount;

        double accountFromExpectedBalance = accountFrom.getBalance() - amount;
        double accountToExpectedBalance = accountTo.getBalance() + amount;

        CustomResponse expectedResponse = new CustomResponse(200, TRANSACTION_SUCCESS);

        String body = "{\"idFrom\" : \"" + idFrom + "\", \"idTo\" : \"" + idTo + "\", \"amount\" : \"" + amount + "\"}";

        //WHEN
        CustomResponse response = RestRequestsUtil.doRequest(TRANSACTION_API_URL, "POST", body);

        //THEN
        assertEquals(expectedResponse, response);

        Account accountFromUpdated = AccountTestUtil.getAccountFromDBById(idFrom);
        Account accountToUpdated = AccountTestUtil.getAccountFromDBById(idTo);

        assert accountFromExpectedBalance == accountFromUpdated.getBalance();
        assert accountToExpectedBalance == accountToUpdated.getBalance();
    }

    @Test
    public void testDoTransaction_BadFormedTransaction_ShouldReturnErrorMessageAboutInvalidParameters() throws IOException {
        //GIVEN
        String expectedMessage = "Transaction was failed during parameters verification. Please, check all parameters.";
        CustomResponse expectedResponse = new CustomResponse(400, expectedMessage);
        String bodyWithBadParameters = "{\"idFrom\" : \"0\", \"idTo\" : \"0\", \"amount\" : \"0\"}";

        //WHEN
        CustomResponse response = RestRequestsUtil.doRequest(TRANSACTION_API_URL, "POST", bodyWithBadParameters);

        //THEN
        assertEquals(expectedResponse, response);
    }

    @Test
    public void testDoTransaction_ParrallelTransactions_StateShouldFitACID() throws InterruptedException, ExecutionException {
        //GIVEN
        final int idFrom = 3;
        final int idTo = 4;
        final double amount = 10;
        final String body = "{\"idFrom\" : \"" + idFrom + "\", \"idTo\" : \"" + idTo + "\", \"amount\" : \"" + amount + "\"}";

        Account accountFrom = AccountTestUtil.getAccountFromDBById(idFrom);
        assert accountFrom != null;
        Account accountTo = AccountTestUtil.getAccountFromDBById(idTo);
        assert accountTo != null;

        assert accountFrom.getBalance() > amount * SPARK_THREADS_MAX_COUNT;

        double accountFromExpectedBalance = accountFrom.getBalance() - amount * SPARK_THREADS_MAX_COUNT;
        double accountToExpectedBalance = accountTo.getBalance() + amount * SPARK_THREADS_MAX_COUNT;

        CustomResponse expectedResponse = new CustomResponse(200, TRANSACTION_SUCCESS);

        //WHEN
        ExecutorService executorService = Executors.newFixedThreadPool(SPARK_THREADS_MAX_COUNT);
        List<Callable<CustomResponse>> callableList = new ArrayList<>();

        for (int i = 0; i < SPARK_THREADS_MAX_COUNT; i++) {
            callableList.add(() -> RestRequestsUtil.doRequest(TRANSACTION_API_URL, "POST", body));
        }

        List<Future<CustomResponse>> responseFutures = executorService.invokeAll(callableList);

        //THEN
        for (Future<CustomResponse> responseFuture : responseFutures) {
            assertEquals(expectedResponse, responseFuture.get());
        }

        assert accountFromExpectedBalance == AccountTestUtil.getAccountFromDBById(idFrom).getBalance();
        assert accountToExpectedBalance == AccountTestUtil.getAccountFromDBById(idTo).getBalance();
    }
}
