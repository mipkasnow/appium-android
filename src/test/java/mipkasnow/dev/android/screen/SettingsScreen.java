package mipkasnow.dev.android.screen;

import com.codeborne.selenide.appium.SelenideAppiumElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.appear;
import static mipkasnow.dev.android.util.AndroidElements.byExactText;
import static mipkasnow.dev.android.util.AndroidElements.byResourceId;

public final class SettingsScreen extends BaseScreen<SettingsScreen> {

    private final SelenideAppiumElement
            title = byExactText("Settings"),
            paymentMethodsRow = byResourceId("settings.paymentMethodsRow");

    @Override
    @Step("Проверить, что экран Settings загружен")
    public SettingsScreen checkThatScreenLoaded() {
        title.should(appear);
        paymentMethodsRow.should(appear);
        return this;
    }

    @Step("Открыть способы оплаты")
    public PaymentMethodsScreen openPaymentMethods() {
        paymentMethodsRow.click();
        return new PaymentMethodsScreen();
    }
}
