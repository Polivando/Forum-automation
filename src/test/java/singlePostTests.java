import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class singlePostTests {
    String driverPath = "C:\\chromedriver.exe";

    WebDriver driver;

    postPage postPage;

    String homePageUrl = "https://georgel8.wixsite.com/ait-interview/rfyourrukm";

    @Before
    public void setUp() {
        //arrange
        System.setProperty("webdriver.chrome.driver", driverPath);
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(1500, TimeUnit.MILLISECONDS);
        driver.get(homePageUrl);

        //TODO: better to get cookie via api and inject in browser before opening first page
        new landingPage(driver).ensureLoggedIn("test@mail.com", "password");

        //TODO: ensure we have a post with correct name via api and go straight to the post page
        var category = "music";
        var title = "Test create post";
        var content = "test post content at: " + new Date().toString();
        driver.get(homePageUrl + "/" + category);
        new singleCategoryPage(driver).createPost(title, content);
        postPage = new postPage(driver);
    }

    @Test
    public void testAddLike() {
        //arrange
        var likesBefore = postPage.getLikesCount();

        //act
        var likesAfter = postPage.addLike().getLikesCount();

        //assert
        assertTrue(likesAfter > likesBefore);
        assertTrue(postPage.hasLikeFromCurrentUser());
    }

    @Test
    public void testReply() {
        //arrange
        var replyText = "this is reply";

        //act
        postPage = postPage.replyToPost(replyText);

        //assert
        assertEquals(replyText, postPage.getLastCommentText());
        assertEquals("a few seconds ago", postPage.getLastCommentDate());
    }

    @Test
    public void testFollow() {
        //arrange
        postPage = postPage.ensurePostUnfollowed();

        //act
        postPage = postPage.followPost();

        //assert
        assertEquals("Following", postPage.getFollowStatus());
    }

    @Test
    public void testUnfollow() {
        //arrange
        postPage = postPage.ensurePostFollowed();

        //act
        postPage = postPage.unfollowPost();

        //assert
        assertEquals("Follow", postPage.getFollowStatus());
    }

    @After
    public void tearDown() {
        //TODO: delete any posts created during test execution
        driver.close();
    }
}
