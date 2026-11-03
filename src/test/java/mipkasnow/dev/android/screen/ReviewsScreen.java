package mipkasnow.dev.android.screen;

import com.codeborne.selenide.appium.SelenideAppiumElement;
import io.qameta.allure.Step;
import mipkasnow.dev.android.screen.component.CropPhotoComponent;
import mipkasnow.dev.android.screen.component.GalleryPicker;
import mipkasnow.dev.android.util.AdbExecutor;
import org.jspecify.annotations.NullMarked;

import java.nio.file.Path;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.disappear;
import static mipkasnow.dev.android.util.AndroidElements.byAccessibilityId;
import static mipkasnow.dev.android.util.AndroidElements.byExactText;
import static mipkasnow.dev.android.util.AndroidElements.byResourceId;

@NullMarked
public final class ReviewsScreen extends BaseScreen<ReviewsScreen> {

    private static final String PHOTO_NAME = "review-photo.png";
    private static final Path PHOTO = Path.of("src/test/resources/images", PHOTO_NAME).toAbsolutePath().normalize();

    private final SelenideAppiumElement
            title = byExactText("Reviews"),
            textInput = byResourceId("reviews.textInput"),
            imagePreview = byResourceId("reviews.imagePreview"),
            postButton = byResourceId("reviews.submitButton");

    @Override
    @Step("Проверить, что экран Reviews загружен")
    public ReviewsScreen checkThatScreenLoaded() {
        title.should(appear);
        textInput.should(appear);
        postButton.should(appear);
        return this;
    }

    @Step("Поставить оценку {stars}")
    public ReviewsScreen rate(int stars) {
        byResourceId("reviews.ratingInput.star." + stars).click();
        return this;
    }

    @Step("Прикрепить фото к отзыву")
    public ReviewsScreen attachPhoto() {
        AdbExecutor.push(PHOTO, "/sdcard/Download/" + PHOTO_NAME);
        byResourceId("reviews.pickImageButton").click();
        new GalleryPicker().choose(PHOTO_NAME);
        new CropPhotoComponent().confirm();
        return this;
    }

    @Step("Проверить, что фото прикреплено")
    public ReviewsScreen checkThatPhotoAttached() {
        imagePreview.should(appear);
        return this;
    }

    @Step("Опубликовать отзыв")
    public ReviewsScreen post() {
        postButton.click();
        return this;
    }

    @Step("Проверить, что отзыв с фото опубликован")
    public ReviewsScreen checkThatReviewWithPhotoPosted() {
        imagePreview.should(disappear);
        byAccessibilityId("Photo attached to review").should(appear);
        return this;
    }
}
