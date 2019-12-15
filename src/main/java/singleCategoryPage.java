import jdk.jshell.spi.ExecutionControl.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;

public class singleCategoryPage {
    WebDriver driver;
    WebDriverWait wait;
    Actions builder;

    @FindBy(css="[data-hook='create-post']")
    WebElement createPostButton;

    @FindBy(css = "[data-hook='post-list-item']")
    ArrayList<WebElement> posts;

    @FindBy(css = "[data-hook='modal-layout']")
    WebElement postPageModal;

    @FindBy(css = "[data-hook='sorting-select']")
    WebElement sortingSelect;

    @FindBy(css = "[data-hook='sorting-select'] [data-hook='actions']")
    WebElement sortingMenu;

    public singleCategoryPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5),Duration.ofMillis(50));
        builder = new Actions(driver);
    }

    public postPage createPost(String name, String text) {
        ElementBehaviors.waitAndClick(createPostButton, wait);
        return new createPostPage(driver).createPost(name, text);
    }

    public singleCategoryPage followPost(int index) {
        var postMenu = getPostMenu(index);
        postMenu.findElement(By.cssSelector("[data-hook='post-actions__subscriptions']")).click();

        return this;
    }

    /**
     * @param index index of post on page
     * @param method currently only ShareVia.link is supported
     * @return returns shareable link
     */
    public String sharePost(int index, ShareVia method) {
        var postMenu = getPostMenu(index);
        postMenu.findElement(By.cssSelector("[data-hook='post-actions__share']")).click();
        postPageModal.findElement(By.cssSelector("[data-hook='share-button__" + method.name() + "']")).click();
        var link = postPageModal.findElement(By.cssSelector("[data-hook='submit-button']"))
                .getAttribute("data-clipboard-text");
        postPageModal.findElement(By.cssSelector(".modal__close")).click();
        return link;
    }

    public singleCategoryPage reportPost(int index, String reason) {
        var postMenu = getPostMenu(index);
        postMenu.findElement(By.cssSelector("[data-hook='post-actions__report']")).click();
        var reasonElement = postPageModal.findElements(By.cssSelector("[data-hook='report-reason']"))
                .stream().filter((r) -> r.getText() == reason).findFirst().orElseThrow(NotFoundException::new);
        ElementBehaviors.waitAndClick(reasonElement, wait);
        postPageModal.findElement(By.cssSelector("button[type='submit']")).click();
        return this;
    }

    public boolean isModalPresent() {
        return postPageModal.isDisplayed();
    }

    public void ensurePostsExist() {
        if (posts.size() == 0)
            createPost("test", "test");
    }

    public void ensurePostsCount(int count) {
        while (posts.size() < count) {
            createPost("test", "test");
        }
    }

    public String getPostTitle(int index) {
        var post = posts.get(index);
        return post.findElement(By.cssSelector("[data-hook='post-title']")).getText();
    }

    public String getPostFollowStatus(int index) {
        var menu = getPostMenu(index);
        return menu.findElement(By.cssSelector("[data-hook='post-actions__subscriptions']")).getText();
    }

    public void sortPosts(SortCriterion criterion) {
        sortingSelect.findElement(By.cssSelector("button[aria-label='Sort by:']")).click();
        String dataHookValue = "";
        switch (criterion) {
            case newest:
                dataHookValue = "sortByNewest";
                break;
            case mostLikes:
                dataHookValue = "sortByLikes";
                break;
            case mostViewed:
                dataHookValue = "sortByMostViews";
                break;
            case mostComments:
                dataHookValue = "sortByMostComments";
                break;
            case recentActivity:
                dataHookValue = "sortByLastActivity";
                break;
        }
        sortingMenu.findElement(By.cssSelector("[data-hook='" + dataHookValue + "]")).click();
    }

    public boolean isSortedBy(SortCriterion criterion) throws NotImplementedException {
        switch (criterion) {
            case mostViewed:
                var array = posts.stream().map((webElement)
                        -> Integer.parseInt(webElement
                        .findElement(By.cssSelector("[data-hook='post-list-item__view-count']")).getText()))
                        .toArray(Integer[]::new);
                for (int i = 0; i < array.length - 1; i++) {
                    if (array[i] < array[i+1])
                        return false;
                }
                break;
            case recentActivity:
                throw new NotImplementedException("sorting by recent activity verification is not implemented yet");
            case mostComments:
                array = posts.stream().map((webElement)
                        -> Integer.parseInt(webElement
                        .findElements(By.cssSelector("._2o8PM _2R83K")).get(0).getText()))
                        .toArray(Integer[]::new);
                for (int i = 0; i < array.length - 1; i++) {
                    if (array[i] < array[i+1])
                        return false;
                }
                break;
            case mostLikes:
                array = posts.stream().map((webElement)
                        -> Integer.parseInt(webElement
                        .findElements(By.cssSelector("._2o8PM _2R83K")).get(1).getText()))
                        .toArray(Integer[]::new);
                for (int i = 0; i < array.length - 1; i++) {
                    if (array[i] < array[i+1])
                        return false;
                }
                break;
            case newest:
                throw new NotImplementedException("sorting by created date verification is not implemented yet");
        }
        return true;
    }
    private WebElement getPostMenu(int index) {
        var post = posts.get(index);
        var postId = post.getAttribute("id");
        post.findElement(By.id("more-button-" + postId)).click();
        return post.findElement(By.cssSelector("[data-hook='actions']"));
    }
}
