import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;

public class categoriesListPage {
    WebDriver driver;
    WebDriverWait wait;
    Actions builder;

    @FindBy(css="[data-hook='category-list-item']")
    ArrayList<WebElement> categories;

    public categoriesListPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5),Duration.ofMillis(50));
        builder = new Actions(driver);
    }

    public categoriesListPage followCategory(int index) {
        var followButton = getFollowButton(index);
        if (followButton.getText() == "Follow")
            followButton.click();
        else
            throw new NotFoundException("Already followed");

        return this;
    }

    public categoriesListPage ensureCategoryFollowed(int index) {
        var followButton = getFollowButton(index);
        if (followButton.getText() == "Follow")
            followButton.click();
        return this;
    }

    public categoriesListPage unfollowCategory(int index) {
        var followButton = getFollowButton(index);
        if (followButton.getText() == "Follow")
            throw new NotFoundException("Already not followed");
        else
            confirmUnfollow(followButton);

        return this;
    }

    public categoriesListPage ensureCategoryUnfollowed(int index) {
        var followButton = getFollowButton(index);
        if (followButton.getText() != "Follow")
            confirmUnfollow(followButton);

        return this;
    }

    public String getFollowButtonText(int index) {
        return getFollowButton(index).getText();
    }

    private WebElement getFollowButton(int index) {
        var button = categories.get(index).findElement(By.cssSelector("[data-hook='follow-button']"));
        wait.until(ExpectedConditions.elementToBeClickable(button));
        return button;
    }

    private void confirmUnfollow(WebElement followButton) {
        followButton.click();
        driver.findElement(By.cssSelector("[data-hook='submit-button']")).click();
    }
}
