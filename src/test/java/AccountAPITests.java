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

import static com.constants.MessageConstants.ACC_NOT_FOUND;
import static org.junit.Assert.*;


public class AccountAPITests {
    private static final String EMPTY_BODY = "";
    private static final int SPARK_TEST_PORT = 3535;
    private static final int ID_NOT_EXIST = 1234567;
    private static final int MAX_APP_THREADS_COUNT = 10;
    private static final String ACCOUNT_API_URL = "http://localhost:" + SPARK_TEST_PORT + "/accounts";
    private static final String DELETE = "delete";

    @BeforeClass
    public static void setUp() throws CreateAccountException {
        SparkServer.startApp(SPARK_TEST_PORT, MAX_APP_THREADS_COUNT);
        AccountTestUtil.populateAccountDBForAccountAPITests();
    }

    @AfterClass
    public static void tearDown() {
        SparkServer.stop();

    }

    @Test
    public void testGetAccountById_IdNotExist_ShouldResponseAccountNotFound() throws IOException {
        //Given
        CustomResponse expectedResponse = new CustomResponse(404, ACC_NOT_FOUND);
        String url = ACCOUNT_API_URL + "/" + ID_NOT_EXIST;

        //When
        CustomResponse response = RestRequestsUtil.doRequest(url, "GET", EMPTY_BODY);

        //Then
        assertEquals(expectedResponse, response);
    }

    @Test
    public void testGetAccountById_CorrectId_ShouldReturnAccount() throws IOException {
        //Given
        final int accId = 2;
        Account expectedAccount = AccountTestUtil.getAccountFromDBById(accId);
        assert expectedAccount != null;

        CustomResponse expectedResponse = new CustomResponse(200, expectedAccount.toString());
        String url = ACCOUNT_API_URL + "/" + accId;

        //When
        CustomResponse response = RestRequestsUtil.doRequest(url, "GET", EMPTY_BODY);

        //Then
        assertEquals(expectedResponse, response);
    }

    @Test
    public void testDeleteAccountById_IdNotExist_ShouldReturnAccountNotFound() throws IOException {
        //Given
        CustomResponse expectedResponse = new CustomResponse(404, ACC_NOT_FOUND);
        String url = ACCOUNT_API_URL + "/" + DELETE + "/" + ID_NOT_EXIST;

        //When
        CustomResponse response = RestRequestsUtil.doRequest(url, "DELETE", EMPTY_BODY);

        //Then
        assertEquals(expectedResponse, response);
    }

    @Test
    public void testDeleteAccountById_CorrectId_ShouldRemoveAccount() throws IOException {
        final int accId = 1;
        Account account = AccountTestUtil.getAccountFromDBById(accId);
        assert account != null;

        //Given
        CustomResponse expectedResponse = new CustomResponse(204, "");
        String url = ACCOUNT_API_URL + "/" + DELETE  +"/" + accId;

        //When
        CustomResponse response = RestRequestsUtil.doRequest(url, "DELETE", EMPTY_BODY);

        //Then
        assertEquals(expectedResponse, response);
        assertNull(AccountTestUtil.getAccountFromDBById(accId));
    }

    @Test
    public void testCreateAccount_CorrectParams_ShouldCreateAccountAndReturnIt() throws IOException {
        //Given
        final int newAccountId = 4;
        assert AccountTestUtil.getAccountFromDBById(newAccountId) == null;

        final Account expectedAccount = AccountTestUtil.createAccount(4, "NEW HOLDER", 123.17);
        CustomResponse expectedResponse = new CustomResponse(201, expectedAccount.toString());

        String requestBody = "{\"holderName\": \"NEW HOLDER\", \"balance\": \"123.17\"}";

        //When
        CustomResponse response = RestRequestsUtil.doRequest(ACCOUNT_API_URL, "POST", requestBody);

        //Then
        assertEquals(expectedResponse, response);
        assertEquals(expectedAccount.toString().trim(), AccountTestUtil.getAccountFromDBById(newAccountId).toString().trim());
    }

    @Test
    public void testCreateAccount_BadFormedParams_ShouldReturnBadRequest() throws IOException {
        //Given
        CustomResponse expectedResponse = new CustomResponse(400, "Holder name can't be null or empty");
        String requestBody = "{\"holderName\": \"\", \"balance\": \"123\"}";

        //When
        CustomResponse response = RestRequestsUtil.doRequest(ACCOUNT_API_URL, "POST", requestBody);

        //Then
        assertEquals(expectedResponse, response);
    }

}