import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class categoriesFollowTests {

    String driverPath = "C:\\chromedriver.exe";

    WebDriver driver;

    categoriesListPage categoriesListPage;

    @Before
    public void setUp() {
        //arrange
        System.setProperty("webdriver.chrome.driver", driverPath);
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(1500, TimeUnit.MILLISECONDS);
        driver.get("https://georgel8.wixsite.com/ait-interview/rfyourrukm");

        //TODO: better to get cookie via api and inject in browser before opening first page
        new landingPage(driver).ensureLoggedIn("test@mail.com", "password");

        categoriesListPage = new categoriesListPage(driver);
    }

    @Test
    public void testFollowCategory() {
        //act
        categoriesListPage.ensureCategoryUnfollowed(0).followCategory(0);

        //assert
        assertEquals("follow should be successful",
                categoriesListPage.getFollowButtonText(0), "Following");
    }

    @Test
    public void testUnfollowCategory() {
        //act
        categoriesListPage.ensureCategoryFollowed(0).unfollowCategory(0);

        //assert
        assertEquals("unfollow should be successful",
                categoriesListPage.getFollowButtonText(0), "Follow");
    }

    @After
    public void tearDown() {
        driver.close();
    }
}
