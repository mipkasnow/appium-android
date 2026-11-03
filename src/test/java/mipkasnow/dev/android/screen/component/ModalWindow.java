package mipkasnow.dev.android.screen.component;

import com.codeborne.selenide.appium.SelenideAppiumElement;
import io.qameta.allure.Step;
import mipkasnow.dev.android.config.AndroidConfig;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.exactText;
import static mipkasnow.dev.android.util.AndroidElements.byResourceId;

public final class ModalWindow {

    private final SelenideAppiumElement
            panel = byResourceId(AndroidConfig.appPackage() + ":id/parentPanel"),
            title = byResourceId(AndroidConfig.appPackage() + ":id/alert_title"),
            message = byResourceId("android:id/message"),
            okButton = byResourceId("android:id/button1");

    @Step("Проверить модальное окно: {titleText}, {messageText}")
    public ModalWindow checkThatOpened(String titleText, String messageText) {
        panel.should(appear);
        title.shouldHave(exactText(titleText));
        message.shouldHave(exactText(messageText));
        okButton.should(appear);
        return this;
    }

    @Step("Нажать OK")
    public void clickOk() {
        okButton.click();
    }
}
