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
import pojo.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class CreateUserPOJO {
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

    public static  String getRandomEmail(){
        return "testauotamtion" + System.currentTimeMillis() + "@gmail.com";
    }

    @Test
    public void createUserTest() throws IOException {

        User user = new User("Muneeb Khalil", getRandomEmail(),"male", "active");

        APIResponse apiResponsePost = apiRequestContext.post("https://gorest.co.in/public/v2/users", RequestOptions.create().setHeader("Content-Type", "application/json")
                .setHeader("Authorization", "Bearer 3c7990453528e2c5f048e605e24c3df616a573c0870b8bab06b8fba34447d684").setData(user));
        System.out.println(apiResponsePost.status());
        System.out.println(apiResponsePost.statusText());
        Assert.assertEquals(apiResponsePost.status(),201);
        Assert.assertEquals((apiResponsePost.statusText()),"Created");

        //convert response text/json to POJO -- deserialization
        ObjectMapper objectMapper = new ObjectMapper();
        User actUser = objectMapper.readValue(apiResponsePost.text(), User.class);
        Assert.assertEquals(actUser.getEmail(),user.getEmail());
        Assert.assertEquals(actUser.getName(),user.getName());
        Assert.assertEquals(actUser.getGender(),user.getGender());
        Assert.assertEquals(actUser.getStatus(),user.getStatus());
        Assert.assertNotNull(actUser.getId());






    }
}
