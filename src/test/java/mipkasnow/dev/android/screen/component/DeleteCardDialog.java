package mipkasnow.dev.android.screen.component;

import com.codeborne.selenide.appium.SelenideAppiumElement;
import io.qameta.allure.Step;
import mipkasnow.dev.android.config.AndroidConfig;
import mipkasnow.dev.android.screen.PaymentMethodsScreen;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.exactText;
import static mipkasnow.dev.android.util.AndroidElements.byResourceId;

public final class DeleteCardDialog {

    private final SelenideAppiumElement
            title = byResourceId(AndroidConfig.appPackage() + ":id/alert_title"),
            message = byResourceId("android:id/message"),
            confirmButton = byResourceId("android:id/button1");

    @Step("Проверить, что диалог удаления карты открыт")
    public DeleteCardDialog checkThatOpened() {
        title.shouldHave(exactText("Delete card?"));
        message.should(appear);
        confirmButton.should(appear);
        return this;
    }

    @Step("Подтвердить удаление карты")
    public PaymentMethodsScreen confirm() {
        confirmButton.click();
        return new PaymentMethodsScreen();
    }
}
