package mipkasnow.dev.android.screen;

import com.codeborne.selenide.appium.SelenideAppiumElement;
import io.qameta.allure.Step;
import mipkasnow.dev.android.data.DemoUser;
import mipkasnow.dev.android.screen.component.ModalWindow;
import mipkasnow.dev.android.util.AndroidActions;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.exactText;
import static mipkasnow.dev.android.util.AndroidElements.byResourceId;

public final class LoginScreen extends BaseScreen<LoginScreen> {

    private final SelenideAppiumElement
            emailInput = byResourceId("login.emailInput"),
            passwordInput = byResourceId("login.passwordInput"),
            submitButton = byResourceId("login.submitButton");

    @Override
    @Step("Проверить, что экран Sign in загружен")
    public LoginScreen checkThatScreenLoaded() {
        emailInput.should(appear);
        passwordInput.should(appear);
        submitButton.should(appear);
        return this;
    }

    @Step("Войти как демо-пользователь")
    public ProfileScreen loginAsDemoUser() {
        emailInput.shouldHave(exactText(DemoUser.EMAIL));
        passwordInput.shouldHave(exactText("•".repeat(DemoUser.PASSWORD.length())));
        submitButton.click();
        return new ProfileScreen();
    }

    @Step("Войти как {email}")
    public ModalWindow loginAs(String email, String password) {
        emailInput.sendKeys(email);
        passwordInput.sendKeys(password);
        AndroidActions.hideKeyboard();
        submitButton.click();
        return new ModalWindow();
    }
}
