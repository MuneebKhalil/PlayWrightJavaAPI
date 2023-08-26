package testts;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class TokenTest {
    Playwright playwright;
    APIRequest request;
    APIRequestContext apiRequestContext;

    @BeforeTest
    public void setup() {
        playwright = Playwright.create();
        request = playwright.request();
        apiRequestContext = request.newContext();
    }

    @AfterTest
    public void tearDown() {
        playwright.close();
    }

    @Test
    public void getToken() throws IOException {

        String reqTokenJsonBody = "{\n" +
                "    \"username\" : \"admin\",\n" +
                "    \"password\" : \"password123\"\n" +
                "}";

        byte[] fileBytes = null;
        File file = new File("./src/test/data/user.json");
        fileBytes = Files.readAllBytes(file.toPath());

        APIResponse apiTokenResponsePost = apiRequestContext.post("https://restful-booker.herokuapp.com/auth", RequestOptions.create()
                .setHeader("Content-Type", "application/json")
               .setData(reqTokenJsonBody));
        System.out.println(apiTokenResponsePost.status());
        System.out.println(apiTokenResponsePost.statusText());
        Assert.assertEquals(apiTokenResponsePost.status(), 200);
        Assert.assertEquals((apiTokenResponsePost.statusText()), "OK");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(apiTokenResponsePost.body());
        System.out.println(jsonNode.toPrettyString());

        //CAPTURE TOKEN
        String tokenID = jsonNode.get("token").asText();
        System.out.println("token id : " + tokenID);
        Assert.assertNotNull(tokenID);
    }
}
