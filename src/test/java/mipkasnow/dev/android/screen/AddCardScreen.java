package mipkasnow.dev.android.screen;

import com.codeborne.selenide.appium.SelenideAppiumElement;
import io.qameta.allure.Step;
import mipkasnow.dev.android.util.AndroidActions;

import static com.codeborne.selenide.Condition.appear;
import static mipkasnow.dev.android.util.AndroidElements.byExactText;
import static mipkasnow.dev.android.util.AndroidElements.byResourceId;

public final class AddCardScreen extends BaseScreen<AddCardScreen> {

    private final SelenideAppiumElement
            title = byExactText("Add card"),
            cardholderInput = byResourceId("addPaymentMethod.cardholderInput"),
            numberInput = byResourceId("addPaymentMethod.numberInput"),
            expMonthInput = byResourceId("addPaymentMethod.expMonthInput"),
            expYearInput = byResourceId("addPaymentMethod.expYearInput"),
            cvcInput = byResourceId("addPaymentMethod.cvcInput"),
            saveButton = byResourceId("addPaymentMethod.submitButton");

    @Override
    @Step("Проверить, что экран Add card загружен")
    public AddCardScreen checkThatScreenLoaded() {
        title.should(appear);
        cardholderInput.should(appear);
        numberInput.should(appear);
        saveButton.should(appear);
        return this;
    }

    @Step("Заполнить карту: держатель {cardholder}, номер {number}, месяц {expMonth}, год {expYear}, cvc {cvc}")
    public AddCardScreen fillCard(String cardholder, String number, String expMonth, String expYear, String cvc) {
        cardholderInput.sendKeys(cardholder);
        numberInput.sendKeys(number);
        expMonthInput.sendKeys(expMonth);
        expYearInput.sendKeys(expYear);
        cvcInput.sendKeys(cvc);
        return this;
    }

    @Step("Сохранить карту")
    public PaymentMethodsScreen save() {
        AndroidActions.hideKeyboard();
        saveButton.click();
        return new PaymentMethodsScreen();
    }
}
