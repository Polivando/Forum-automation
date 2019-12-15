import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class searchPostsPageTests {

    String driverPath = "C:\\chromedriver.exe";

    WebDriver driver;

    searchPostsPage searchPostsPage;

    @Before
    public void setUp() {
        //arrange
        System.setProperty("webdriver.chrome.driver",driverPath);
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(1500, TimeUnit.MILLISECONDS);
        driver.get("https://georgel8.wixsite.com/ait-interview/rfyourrukm");
        searchPostsPage = new searchPostsPage(driver);
    }

    @Test
    public void testEmptySearch(){
        //act
        //TODO: understand why sending ENTER doesn't work from script, only manually
        searchPostsPage = searchPostsPage.searchPosts("gibberish");

        //assert
        assertEquals( "No Results Found We couldn’t find what you’re looking for. Try another search.",
                searchPostsPage.getEmptyResultsMessage());
    }

    @Test
    public void testSearchResultCount(){
        //act
        searchPostsPage = searchPostsPage.searchPosts("welcome");

        //assert
        assertTrue( "forum should have corresponding posts", searchPostsPage.getResultsCount() > 0);
    }

    @After
    public void tearDown(){
        driver.close();
    }
}