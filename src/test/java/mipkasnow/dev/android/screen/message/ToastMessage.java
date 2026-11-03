package mipkasnow.dev.android.screen.message;

import com.codeborne.selenide.appium.SelenideAppiumElement;

import static mipkasnow.dev.android.util.AndroidElements.byExactText;

public enum ToastMessage {

    CARD_ADDED("Card added"),
    CARD_REMOVED("Card removed"),
    PROFILE_UPDATED("Profile updated");

    private final String text;

    ToastMessage(String text) {
        this.text = text;
    }

    public String text() {
        return text;
    }

    public SelenideAppiumElement element() {
        return byExactText(text);
    }

    @Override
    public String toString() {
        return text;
    }
}
