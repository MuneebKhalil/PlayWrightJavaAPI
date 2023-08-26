package testts;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;

public class APIDisposeTest {
    Playwright playwright;
    APIRequest request;
    APIRequestContext apiRequestContext;
    @BeforeTest
    public void setup(){
        playwright = Playwright.create();
        request = playwright.request();
        apiRequestContext = request.newContext();
    }

    @Test
    public void disposeResponseTest() throws IOException {
        APIResponse apiResponse = apiRequestContext.get("\n" +
                "https://gorest.co.in/public/v2/users");
        System.out.println(apiResponse.status());
        Assert.assertEquals(apiResponse.ok(),true);
        System.out.println(apiResponse.statusText());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(apiResponse.body());
        String jsonPretty = jsonNode.toPrettyString();
        System.out.println(jsonPretty);

        apiResponse.dispose();// will dispose only response body but status code, url, status text will remain

        System.out.println(apiResponse.text());
    }

    @AfterTest
    public void tearDown(){
        playwright.close();
    }
}
