import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class createPostPage {
    WebDriver driver;
    WebDriverWait wait;
    Actions builder;

    @FindBy(css = "[data-hook='post-form__title-input']")
    WebElement titleField;

    @FindBy(css = ".public-DraftEditor-content")
    WebElement contentField;

    @FindBy(css = "[data-hook='post-form__publish-button']")
    WebElement publishButton;

    public createPostPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5), Duration.ofMillis(50));
        builder = new Actions(driver);
    }

    public postPage createPost(String title, String text) {
        ElementBehaviors.enterText(title, titleField, wait, builder);
        ElementBehaviors.enterText(text, contentField, wait, builder);
        ElementBehaviors.scrollToAndClick(publishButton, builder);

        return new postPage(driver);
    }
}