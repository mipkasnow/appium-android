package mipkasnow.dev.android.screen.component;

import com.codeborne.selenide.appium.SelenideAppiumElement;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.appium.AppiumClickOptions.tap;
import static mipkasnow.dev.android.util.AndroidElements.byAccessibilityId;
import static mipkasnow.dev.android.util.AndroidElements.byContentDescContains;
import static mipkasnow.dev.android.util.AndroidElements.byExactText;
import static mipkasnow.dev.android.util.AndroidElements.byTextContains;
import static mipkasnow.dev.android.util.AndroidElements.byXpath;

public final class GalleryPicker {

    @Step("Выбрать файл {fileName}")
    public void choose(String fileName) {
        try {
            byContentDescContains(fileName).should(appear, Duration.ofSeconds(2)).click();
        } catch (Error e) {
            byAccessibilityId("Show roots").click();
            SelenideAppiumElement downloads = byExactText("Downloads");
            downloads.should(appear).click();
            byExactText("IMAGES IN DOWNLOADS").should(appear);
            byContentDescContains(fileName).click();
        }
    }
}
