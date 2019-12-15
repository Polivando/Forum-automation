import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class landingPageTest {

    String driverPath = "C:\\chromedriver.exe";

    WebDriver driver;

    landingPage landingPage;

    @Before
    public void setUp() {
        //arrange
        System.setProperty("webdriver.chrome.driver",driverPath);
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(1500, TimeUnit.MILLISECONDS);
        driver.get("https://georgel8.wixsite.com/ait-interview/rfyourrukm");
        landingPage = new landingPage(driver);
    }

    @Test
    public void testSignUp(){
        //act
        //TODO: investigate why initial wait for first button works only in debug
        landingPage = landingPage.signUpViaEmail("test@mail.com", "password");

        //assert
        assertTrue( "user should be logged in by now", landingPage.isUserLoggedIn());
    }

    @Test
    public void testLogin(){
        //act
        landingPage = landingPage.loginViaEmail("test@mail.com", "password");

        //assert
        assertTrue( "user should be logged in by now", landingPage.isUserLoggedIn());
    }

    @After
    public void tearDown() {
        driver.close();
    }
}