import jdk.jshell.spi.ExecutionControl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;
import java.util.Date;

import static org.junit.Assert.*;
public class singleCategoryTest {

    String driverPath = "C:\\chromedriver.exe";

    WebDriver driver;

    singleCategoryPage categoryPage;

    String homePageUrl = "https://georgel8.wixsite.com/ait-interview/rfyourrukm";

    int postIndex;

    @Before
    public void setUp() {
        //arrange
        System.setProperty("webdriver.chrome.driver", driverPath);
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(1500, TimeUnit.MILLISECONDS);
        driver.get(homePageUrl);

        //TODO: better to get cookie via api and inject in browser before opening first page
        new landingPage(driver).ensureLoggedIn("test@mail.com", "password");
    }

    @Test
    public void testCreatePost() {
        //assert
        var category = "music";
        var title = "Test create post";
        var titleUrlFormat = "test-create-post";
        var content = "test post content at: " + new Date().toString();
        driver.get(homePageUrl + "/" + category);
        categoryPage = new singleCategoryPage(driver);

        //act
        var postPage = categoryPage.createPost(title, content);

        //assert
        assertTrue(driver.getCurrentUrl().endsWith(category + "/" + titleUrlFormat));
        assertEquals(postPage.getCategoryNameFromMenu().toLowerCase(), category);
        assertEquals(postPage.getPostTitleFromMenu(), title);
    }

    @Test
    public void testSharePost() {
        //arrange
        var category = "music";
        var categoryUrl = homePageUrl + "/" + category;
        postIndex = 0;
        driver.get(categoryUrl);
        categoryPage = new singleCategoryPage(driver);
        //TODO: create post as user different from used in scenario
        //TODO: ensure category is not empty via api
        categoryPage.ensurePostsExist();

        //act
        var copiedLink = categoryPage.sharePost(postIndex, ShareVia.link);

        //assert
        assertTrue(copiedLink.length() > categoryUrl.length() && copiedLink.startsWith(categoryUrl));
    }

    @Test
    public void testReportPost() {
        //arrange
        var category = "music";
        var categoryUrl = homePageUrl + "/" + category;
        postIndex = 0;
        driver.get(categoryUrl);
        categoryPage = new singleCategoryPage(driver);
        //TODO: create post as user different from used in scenario
        //TODO: ensure category is not empty via api
        categoryPage.ensurePostsExist();

        //act
        var postTitle = categoryPage.getPostTitle(postIndex);
        categoryPage.reportPost(postIndex, "Spam");

        //assert
        var newFirstPostTitle = categoryPage.getPostTitle(postIndex);
        assertEquals(postTitle, newFirstPostTitle);
    }

    @Test
    public void testFollowPost() {
        //arrange
        var category = "music";
        var categoryUrl = homePageUrl + "/" + category;
        postIndex = 0;
        driver.get(categoryUrl);
        categoryPage = new singleCategoryPage(driver);
        //TODO: create post as user different from used in scenario
        //TODO: ensure category is not empty via api
        categoryPage.ensurePostsExist();

        //act
        categoryPage.followPost(postIndex);

        //assert
        var postFollowStatus = categoryPage.getPostFollowStatus(postIndex);
        assertEquals("Following", postFollowStatus);
    }

    @Test
    public void testSortPosts() throws ExecutionControl.NotImplementedException {
        //arrange
        var category = "music";
        var categoryUrl = homePageUrl + "/" + category;
        var criterion = SortCriterion.mostViewed;
        postIndex = 0;
        driver.get(categoryUrl);
        categoryPage = new singleCategoryPage(driver);
        //TODO: create post as user different from used in scenario
        //TODO: ensure category is not empty via api
        categoryPage.ensurePostsCount(2);

        //act
        categoryPage.sortPosts(criterion);

        //assert
        assertTrue(categoryPage.isSortedBy(criterion));
    }

    @After
    public void tearDown() {
        //TODO: delete any posts that were created during script execution
        driver.close();
    }
}
