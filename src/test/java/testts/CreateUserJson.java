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
import java.util.HashMap;
import java.util.Map;

public class CreateUserJson {

    Playwright playwright;
    APIRequest request;
    APIRequestContext apiRequestContext;
    @BeforeTest
    public void setup(){
        playwright = Playwright.create();
        request = playwright.request();
        apiRequestContext = request.newContext();
    }

    @AfterTest
    public void tearDown(){
        playwright.close();
    }

    @Test
    public void createUserTest() throws IOException {

        byte[] fileBytes = null;
        File file = new File("./src/test/data/user.json");
        fileBytes = Files.readAllBytes(file.toPath());

        APIResponse apiResponsePost = apiRequestContext.post("https://gorest.co.in/public/v2/users", RequestOptions.create().setHeader("Content-Type", "application/json")
                .setHeader("Authorization", "Bearer 3c7990453528e2c5f048e605e24c3df616a573c0870b8bab06b8fba34447d684").setData(fileBytes));
        System.out.println(apiResponsePost.status());
        System.out.println(apiResponsePost.statusText());
        Assert.assertEquals(apiResponsePost.status(),201);
        Assert.assertEquals((apiResponsePost.statusText()),"Created");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(apiResponsePost.body());
        System.out.println(jsonNode.toPrettyString());

        //Get Call : Fetch user tht is created
        String id = jsonNode.get("id").asText();

        APIResponse apiResponseGet =  apiRequestContext.get("https://gorest.co.in/public/v2/users/" + id, RequestOptions.create().
                setHeader("Authorization", "Bearer 3c7990453528e2c5f048e605e24c3df616a573c0870b8bab06b8fba34447d684"));
        Assert.assertEquals(apiResponseGet.status(), 200);
        Assert.assertEquals(apiResponseGet.statusText(),"OK");
        JsonNode jsonNode1 = objectMapper.readTree(apiResponseGet.body());
        System.out.println(jsonNode1.toPrettyString());
        Assert.assertTrue(apiResponseGet.text().contains(id));
        Assert.assertTrue(apiResponseGet.text().contains("Muneeb Khalil"));




    }
}
