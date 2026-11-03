package mipkasnow.dev.android.test;

import mipkasnow.dev.android.data.DemoCard;
import mipkasnow.dev.android.data.DemoUser;
import mipkasnow.dev.android.jupiter.annotation.meta.AndroidTest;
import mipkasnow.dev.android.screen.EventsScreen;
import mipkasnow.dev.android.screen.message.ToastMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@AndroidTest
@DisplayName("Payment methods")
class PaymentMethodTest {

    @Test
    @DisplayName("Успешное добавление и удаление карты")
    void userCanAddAndRemoveCard() {
        new EventsScreen()
                .checkThatScreenLoaded()
                .openProfile()
                .checkThatScreenLoaded()
                .checkThatSignedOut()
                .openSignIn()
                .checkThatScreenLoaded()
                .loginAsDemoUser()
                .checkThatScreenLoaded()
                .checkThatLoggedInAs(DemoUser.EMAIL)
                .openSettings()
                .checkThatScreenLoaded()
                .openPaymentMethods()
                .checkThatScreenLoaded()
                .openAddCard()
                .checkThatScreenLoaded()
                .fillCard(
                        DemoCard.CARDHOLDER,
                        DemoCard.NUMBER,
                        DemoCard.EXP_MONTH,
                        DemoCard.EXP_YEAR,
                        DemoCard.CVC
                )
                .save()
                .checkToast(ToastMessage.CARD_ADDED)
                .deleteCard(DemoCard.CARDHOLDER)
                .checkThatOpened()
                .confirm()
                .checkToast(ToastMessage.CARD_REMOVED);
    }
}
