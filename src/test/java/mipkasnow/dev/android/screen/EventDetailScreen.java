package mipkasnow.dev.android.screen;

import com.codeborne.selenide.appium.SelenideAppiumElement;
import io.qameta.allure.Step;
import mipkasnow.dev.android.util.AndroidActions;
import org.jspecify.annotations.NullMarked;

import static com.codeborne.selenide.Condition.appear;
import static mipkasnow.dev.android.util.AndroidElements.byResourceId;

@NullMarked
public final class EventDetailScreen extends BaseScreen<EventDetailScreen> {

    private final SelenideAppiumElement
            title = byResourceId("eventDetail.title"),
            reviewsButton = byResourceId("eventDetail.reviewsButton");

    @Override
    @Step("Проверить, что экран события загружен")
    public EventDetailScreen checkThatScreenLoaded() {
        title.should(appear);
        return this;
    }

    @Step("Открыть отзывы")
    public ReviewsScreen openReviews() {
        AndroidActions.swipeUpQuick();
        reviewsButton.click();
        return new ReviewsScreen();
    }
}
