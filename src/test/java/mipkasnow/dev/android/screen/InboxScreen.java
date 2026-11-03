package mipkasnow.dev.android.screen;

import com.codeborne.selenide.appium.SelenideAppiumElement;
import io.qameta.allure.Step;
import org.jspecify.annotations.NullMarked;

import static com.codeborne.selenide.Condition.appear;
import static mipkasnow.dev.android.util.AndroidElements.byExactText;
import static mipkasnow.dev.android.util.AndroidElements.byResourceId;

@NullMarked
public final class InboxScreen extends BaseScreen<InboxScreen> {

    private final SelenideAppiumElement
            title = byExactText("Inbox"),
            notifications = byResourceId("inbox.list");

    @Override
    @Step("Проверить, что экран Inbox загружен")
    public InboxScreen checkThatScreenLoaded() {
        title.should(appear);
        notifications.should(appear);
        return this;
    }

    @Step("Открыть уведомление {title}")
    public EventDetailScreen openNotification(String title) {
        byExactText(title).click();
        return new EventDetailScreen();
    }
}
