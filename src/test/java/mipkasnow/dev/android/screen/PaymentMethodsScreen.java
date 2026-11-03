package mipkasnow.dev.android.screen;

import com.codeborne.selenide.appium.SelenideAppiumElement;
import io.qameta.allure.Step;
import mipkasnow.dev.android.screen.component.DeleteCardDialog;

import static com.codeborne.selenide.Condition.appear;
import static mipkasnow.dev.android.util.AndroidElements.byExactText;
import static mipkasnow.dev.android.util.AndroidElements.byResourceId;
import static mipkasnow.dev.android.util.AndroidElements.byXpath;

public final class PaymentMethodsScreen extends BaseScreen<PaymentMethodsScreen> {

    private final SelenideAppiumElement
            title = byExactText("Payment methods"),
            addCardButton = byResourceId("paymentMethods.addButton");

    @Override
    @Step("Проверить, что экран Payment methods загружен")
    public PaymentMethodsScreen checkThatScreenLoaded() {
        title.should(appear);
        addCardButton.should(appear);
        return this;
    }

    @Step("Открыть добавление карты")
    public AddCardScreen openAddCard() {
        addCardButton.click();
        return new AddCardScreen();
    }

    @Step("Удалить карту {cardholder}")
    public DeleteCardDialog deleteCard(String cardholder) {
        deleteButton(cardholder).click();
        return new DeleteCardDialog();
    }

    private SelenideAppiumElement deleteButton(String cardholder) {
        return byXpath("//*[contains(@text,'" + cardholder + "')]"
                + "/following-sibling::*[contains(@resource-id,'paymentMethods.delete.')]");
    }
}
