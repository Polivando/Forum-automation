import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ElementBehaviors {

    public static void enterText(String text, WebElement element, WebDriverWait wait, Actions builder) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        builder.click(element).sendKeys(element, text).build().perform();
    }

    public static void enterTextAndSubmitForm(String text, WebElement element, WebElement form, WebDriverWait wait, Actions builder) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        builder.click(element).sendKeys(element, text).sendKeys(form, Keys.ENTER).build().perform();
    }

    public static void waitAndClick(WebElement element, WebDriverWait wait) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }

    public static void scrollToAndClick(WebElement element, Actions builder)  {
        builder.moveToElement(element).click(element).build().perform();
    }
}
