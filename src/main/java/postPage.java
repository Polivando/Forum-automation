import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;

public class postPage {

    WebDriver driver;
    WebDriverWait wait;
    Actions builder;

    @FindBy(css = "nav[aria-label='Breadcrumbs'] li")
    ArrayList<WebElement> navigationItems;

    @FindBy(css = "[data-hook='animated-loader__container'] [data-hook='like-button']")
    WebElement likeButton;

    @FindBy(css = "[data-hook='pagination-top'] button + button")
    WebElement replyButton;

    @FindBy(css = "[data-hook='pagination-top'] button[actiondetails]")
    WebElement followButton;

    @FindBy(css = ".public-DraftEditor-content")
    WebElement commentTextField;

    @FindBy(css = "[data-hook='submit-button']")
    WebElement publishCommentButton;

    @FindBy(css = "article[data-hook='comment']")
    ArrayList<WebElement> replies;

    public postPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5),Duration.ofMillis(50));
        builder = new Actions(driver);
    }

    public String getCategoryNameFromMenu() {
        return navigationItems.get(1).getText();
    }

    public String getPostTitleFromMenu() {
        return navigationItems.get(2).getText();
    }

    public postPage addLike() {
        if (hasLikeFromCurrentUser())
            throw new IllegalStateException("cannot add more than one like from user");
        likeButton.click();

        return this;
    }

    public int getLikesCount() {
        var likesCount = likeButton.getAttribute("aria-label").split(" ")[0];
        return Integer.parseInt(likesCount);
    }

    public boolean hasLikeFromCurrentUser() {
        return !likeButton.getAttribute("aria-label").contains("unchecked");
    }

    public postPage replyToPost(String content) {
        ElementBehaviors.waitAndClick(replyButton, wait);
        ElementBehaviors.enterText(content, commentTextField, wait, builder);
        ElementBehaviors.scrollToAndClick(publishCommentButton, builder);

        return this;
    }

    public String getLastCommentText() {
        var comment = replies.get(replies.size()-1);
        return comment.findElement(By.cssSelector(".comment__content p")).getText();
    }

    public String getLastCommentDate() {
        var comment = replies.get(replies.size()-1);
        return comment.findElement(By.cssSelector(".comment-header [data-hook='time-ago']]")).getText();
    }

    public postPage followPost() {
        if (getFollowStatus() == "Following")
            throw new IllegalStateException("post already followed");
        followButton.click();

        return this;
    }

    public postPage unfollowPost() {
        if (getFollowStatus() == "Follow")
            throw new IllegalStateException("post already unfollowed");
        followButton.click();

        return this;
    }

    public postPage ensurePostFollowed() {
        if (getFollowStatus() != "Follow")
            followButton.click();

        return this;
    }

    public postPage ensurePostUnfollowed() {
        if (getFollowStatus() != "Following")
            followButton.click();

        return this;
    }

    public String getFollowStatus() {
        return followButton.getText();
    }
}
