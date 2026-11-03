package mipkasnow.dev.android.util;

import com.codeborne.selenide.appium.SelenideAppiumElement;
import io.qameta.allure.Step;
import lombok.experimental.UtilityClass;
import mipkasnow.dev.android.config.AndroidConfig;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Condition.appear;
import static mipkasnow.dev.android.util.Drivers.android;
import static org.junit.jupiter.api.Assertions.fail;

@UtilityClass
public class AndroidActions {

    @Step("Перезапустить приложение")
    public static void terminateAndActivateApp() {
        String packageName = AndroidConfig.appPackage();
        android().terminateApp(packageName);
        android().activateApp(packageName);
    }

    @Step("Свернуть и снова открыть приложение")
    public static void minimizeAndReopenApp() {
        android().runAppInBackground(Duration.ofSeconds(1));
    }

    @Step("Скрыть клавиатуру")
    public static void hideKeyboard() {
        android().hideKeyboard();
    }

    @Step("Pull to refresh")
    public static void refreshViaSwipe() {
        Dimension size = android().manage().window().getSize();
        int centerX = size.getWidth() / 2;
        int centerY = size.getHeight() / 2;
        int bottomEdge = (int) (size.getHeight() * 0.85f);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence scroll = new Sequence(finger, 0);
        scroll.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX, centerY));
        scroll.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        scroll.addAction(finger.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), centerX, bottomEdge));
        scroll.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        android().perform(List.of(scroll));
    }

    public static void swipeUpQuick() {
        swipeVertical(0.8, 0.2, 150);
    }

    public static void swipeDownQuick() {
        swipeVertical(0.2, 0.8, 150);
    }

    public static void swipeLeftQuick() {
        swipeHorizontal(0.9, 0.1, 300);
    }

    public static void swipeRightQuick() {
        swipeHorizontal(0.1, 0.9, 300);
    }

    public static void swipeUpToFindElement(SelenideAppiumElement element) {
        int swipes = 0;
        while (!element.isDisplayed()) {
            swipeUpQuick();
            swipes++;
            if (swipes == 10) {
                fail("Превышено количество свайпов");
            }
        }
        element.should(appear);
    }

    private static void swipeVertical(double startRatio, double endRatio, int durationMs) {
        Dimension size = android().manage().window().getSize();
        int startX = size.width / 2;
        int startY = (int) (size.height * startRatio);
        int endY = (int) (size.height * endRatio);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence scroll = new Sequence(finger, 0);
        scroll.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
        scroll.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        scroll.addAction(finger.createPointerMove(Duration.ofMillis(durationMs), PointerInput.Origin.viewport(), startX, endY));
        scroll.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        android().perform(List.of(scroll));
    }

    private static void swipeHorizontal(double startRatio, double endRatio, int durationMs) {
        Dimension size = android().manage().window().getSize();
        int startY = size.height / 2;
        int startX = (int) (size.width * startRatio);
        int endX = (int) (size.width * endRatio);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence scroll = new Sequence(finger, 0);
        scroll.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
        scroll.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        scroll.addAction(finger.createPointerMove(Duration.ofMillis(durationMs), PointerInput.Origin.viewport(), endX, startY));
        scroll.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        android().perform(List.of(scroll));
    }
}
