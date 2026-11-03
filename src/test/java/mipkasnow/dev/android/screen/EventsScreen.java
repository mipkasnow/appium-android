package mipkasnow.dev.android.screen;

import com.codeborne.selenide.appium.SelenideAppiumElement;
import io.qameta.allure.Step;
import mipkasnow.dev.android.screen.component.TabBarComponent;
import org.jspecify.annotations.NullMarked;

import static com.codeborne.selenide.Condition.appear;
import static mipkasnow.dev.android.util.AndroidElements.byAccessibilityId;
import static mipkasnow.dev.android.util.AndroidElements.byExactText;
import static mipkasnow.dev.android.util.AndroidElements.byResourceId;

@NullMarked
public final class EventsScreen extends BaseScreen<EventsScreen> {

    private final SelenideAppiumElement
            title = byExactText("Events"),
            searchInput = byAccessibilityId("Search events"),
            inboxButton = byResourceId("events.inboxButton");

    private final TabBarComponent tabBar = new TabBarComponent();

    @Override
    @Step("Проверить, что экран Events загружен")
    public EventsScreen checkThatScreenLoaded() {
        title.should(appear);
        searchInput.should(appear);
        return this;
    }

    @Step("Открыть профиль через навбар")
    public ProfileScreen openProfile() {
        return tabBar.openProfile();
    }

    @Step("Открыть входящие")
    public InboxScreen openInbox() {
        inboxButton.click();
        return new InboxScreen();
    }
}
