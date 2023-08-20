import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.HttpHeader;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.http.HttpHeaders;
import java.sql.SQLOutput;
import java.util.List;
import java.util.Map;

public class APIResponseHeaderTest {

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
    public void getHeaderTest(){

        APIResponse apiResponse = apiRequestContext.get("\n" +
                "https://gorest.co.in/public/v2/users");
        Map<String,String> headersMap = apiResponse.headers();
        headersMap.forEach((k,v) -> System.out.println(k + ":" + v));
        Assert.assertEquals(headersMap.get("server"),("cloudflare"));

        System.out.println("-------------------------");
        //UsingList
        List<HttpHeader> headersList = apiResponse.headersArray();
        for(HttpHeader e : headersList){
            System.out.println(e.name + ":" + e.value);
        }
    }
}
