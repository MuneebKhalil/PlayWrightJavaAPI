package testts;

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
import pojo.Users;

import java.io.IOException;

public class UpdateUserPutCall {

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

        //create users object: using builder pattern
        Users users =   Users.builder().name("Muneeb Khalil").email(getRandomEmail()).gender("male").status("active").build();

        //1. POST Call - Create User
        APIResponse apiResponsePost = apiRequestContext.post("https://gorest.co.in/public/v2/users", RequestOptions.create().setHeader("Content-Type", "application/json")
                .setHeader("Authorization", "Bearer 3c7990453528e2c5f048e605e24c3df616a573c0870b8bab06b8fba34447d684").setData(users));
        System.out.println(apiResponsePost.status());
        System.out.println(apiResponsePost.statusText());
        Assert.assertEquals(apiResponsePost.status(),201);
        Assert.assertEquals((apiResponsePost.statusText()),"Created");

        //convert response text/json to POJO -- deserialization
        ObjectMapper objectMapper = new ObjectMapper();
        Users actUser = objectMapper.readValue(apiResponsePost.text(), Users.class);
        Assert.assertEquals(actUser.getEmail(),users.getEmail());
        Assert.assertEquals(actUser.getName(),users.getName());
        Assert.assertEquals(actUser.getGender(),users.getGender());
        Assert.assertEquals(actUser.getStatus(),users.getStatus());
        Assert.assertNotNull(actUser.getId());
        String userId = actUser.getId();
        System.out.println("New user id is " + userId);

        //update status from active to inactive
        users.setStatus("inactive");

        //2. PUT Call - update user
        APIResponse apiResponsePut = apiRequestContext.put("https://gorest.co.in/public/v2/users/" + userId, RequestOptions.create().setHeader("Content-Type", "application/json")
                .setHeader("Authorization", "Bearer 3c7990453528e2c5f048e605e24c3df616a573c0870b8bab06b8fba34447d684").setData(users));

        System.out.println(apiResponsePut.status() + " : " + apiResponsePut.statusText());
        Assert.assertEquals(apiResponsePut.status(), 200);
        System.out.println("Updated User " + apiResponsePut.text());
        Users actUserPut = objectMapper.readValue(apiResponsePut.text(), Users.class);
        Assert.assertEquals(actUserPut.getId(), userId);
        Assert.assertEquals(actUserPut.getStatus(),users.getStatus());

    }
}
