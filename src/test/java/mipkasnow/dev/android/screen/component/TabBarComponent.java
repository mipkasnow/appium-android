package mipkasnow.dev.android.screen.component;

import com.codeborne.selenide.appium.SelenideAppiumElement;
import io.qameta.allure.Step;
import mipkasnow.dev.android.screen.EventsScreen;
import mipkasnow.dev.android.screen.ProfileScreen;

import static mipkasnow.dev.android.util.AndroidElements.byAccessibilityId;

public final class TabBarComponent {

    private final SelenideAppiumElement
            eventsTab = byAccessibilityId("Events tab"),
            profileTab = byAccessibilityId("Profile tab");

    @Step("Открыть вкладку Events")
    public EventsScreen openEvents() {
        eventsTab.click();
        return new EventsScreen();
    }

    @Step("Открыть вкладку Profile")
    public ProfileScreen openProfile() {
        profileTab.click();
        return new ProfileScreen();
    }
}
