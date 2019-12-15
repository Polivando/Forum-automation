import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class searchPostsPage {
    WebDriver driver;
    WebDriverWait wait;
    Actions builder;

    @FindBy(tagName = "form")
    WebElement searchForm;

    @FindBy(css = "input[data-hook='search-input']")
    WebElement searchField;

    @FindBy(css = "[data-hook='animated-loader__container']")
    WebElement searchResults;

    public searchPostsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5),Duration.ofMillis(50));
        builder = new Actions(driver);
    }

    public searchPostsPage searchPosts(String keyword) {
        ElementBehaviors.enterTextAndSubmitForm(keyword, searchField, searchForm, wait, builder);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-hook='animated-loader__container']")));
        return this;
    }

    public int getResultsCount() {
        return searchResults.findElements(By.cssSelector(".post-list-item")).size();
    }

    public String getEmptyResultsMessage() {
        var headerText = searchResults.findElement(By.cssSelector("[data-hook='empty-states__header']")).getText();
        var headerContent = searchResults.findElement(By.cssSelector("[data-hook='empty-states__content']")).getText();

        return headerText + " " + headerContent;
    }

}
