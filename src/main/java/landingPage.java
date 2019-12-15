import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class landingPage {
    WebDriver driver;
    WebDriverWait wait;
    Actions builder;

    @FindBy(css="[data-hook='login-button']")
    WebElement loginSignUpButton;

    @FindBy(id="signUpDialogswitchToEmailLink")
    WebElement selectEmailSignUp;

    @FindBy(id="signUpDialogemailInputinput")
    WebElement signUpEmail;

    @FindBy(id="signUpDialogpasswordInputinput")
    WebElement signUpPassword;

    @FindBy(id="signUpDialogokButton")
    WebElement signUpButton;

    @FindBy(id="signUpDialogswitchDialogLink")
    WebElement switchToLogIn;

    @FindBy(id="memberLoginDialogswitchToEmailLink")
    WebElement selectEmailLogin;

    @FindBy(id="memberLoginDialogemailInputinput")
    WebElement loginEmail;

    @FindBy(id="memberLoginDialogpasswordInputinput")
    WebElement loginPassword;

    @FindBy(id="memberLoginDialogokButton")
    WebElement loginButton;

    @FindBy(id="comp-jxuhadd7user")
    WebElement userMenuButton;

    public landingPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5),Duration.ofMillis(50));
        builder = new Actions(driver);
    }

    public landingPage signUpViaEmail(String email, String password) {
        ElementBehaviors.waitAndClick(loginSignUpButton, wait);
        ElementBehaviors.enterText(email, signUpEmail, wait, builder);
        ElementBehaviors.enterText(password, signUpPassword, wait, builder);
        ElementBehaviors.waitAndClick(signUpButton, wait);

        return this;
    }

    public landingPage loginViaEmail(String email, String password) {
        ElementBehaviors.waitAndClick(loginSignUpButton, wait);
        ElementBehaviors.waitAndClick(switchToLogIn, wait);
        ElementBehaviors.enterText(email, loginEmail, wait, builder);
        ElementBehaviors.enterText(password, loginPassword, wait, builder);
        ElementBehaviors.waitAndClick(loginButton, wait);

        return this;
    }

    public void ensureLoggedIn(String email, String password) {
        if (isUserLoggedIn())
            return;
        loginViaEmail(email, password);
    }

    public boolean isUserLoggedIn() {
        return !loginSignUpButton.isDisplayed() && userMenuButton.isDisplayed();
    }
}
