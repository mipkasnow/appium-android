package mipkasnow.dev.android.jupiter.extension;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.appium.java_client.android.AndroidStartScreenRecordingOptions;
import io.qameta.allure.selenide.AllureSelenide;
import mipkasnow.dev.android.allure.Attach;
import mipkasnow.dev.android.driver.AndroidDriverProvider;
import mipkasnow.dev.android.util.AdbExecutor;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.LifecycleMethodExecutionExceptionHandler;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.open;
import static mipkasnow.dev.android.util.Drivers.android;

public class AndroidExtension implements
        BeforeAllCallback,
        BeforeEachCallback,
        AfterEachCallback,
        TestExecutionExceptionHandler,
        LifecycleMethodExecutionExceptionHandler {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(AndroidExtension.class);
    private static final String CONFIGURED = "configured";

    @Override
    public void beforeAll(ExtensionContext context) {
        ExtensionContext root = context.getRoot();
        if (Boolean.TRUE.equals(root.getStore(NAMESPACE).get(CONFIGURED))) {
            return;
        }

        Configuration.browser = AndroidDriverProvider.class.getName();
        Configuration.browserSize = null;
        Configuration.timeout = 15_000;
        Configuration.pollingInterval = 200;

        root.getStore(NAMESPACE).put(CONFIGURED, Boolean.TRUE);
    }

    @Override
    public void beforeEach(@NonNull ExtensionContext context) {
        if (!SelenideLogger.hasListener("AllureSelenide")) {
            SelenideLogger.addListener(
                    "AllureSelenide",
                    new AllureSelenide()
                            .screenshots(false)
                            .savePageSource(false)
                            .includeSelenideSteps(false)
            );
        }
        AdbExecutor.clearAppData();
        open();
        startVideoRecording();
    }

    @Override
    public void afterEach(@NonNull ExtensionContext context) {
        if (WebDriverRunner.hasWebDriverStarted()) {
            Selenide.closeWebDriver();
        }
    }

    @Override
    public void handleTestExecutionException(@NonNull ExtensionContext context, Throwable throwable) throws Throwable {
        attachArtifactsOnFail();
        throw throwable;
    }

    @Override
    public void handleBeforeEachMethodExecutionException(@NonNull ExtensionContext context, Throwable throwable) throws Throwable {
        attachArtifactsOnFail();
        throw throwable;
    }

    @Override
    public void handleAfterEachMethodExecutionException(@NonNull ExtensionContext context, Throwable throwable) throws Throwable {
        attachArtifactsOnFail();
        throw throwable;
    }

    private static void startVideoRecording() {
        if (!WebDriverRunner.hasWebDriverStarted()) {
            return;
        }
        android().startRecordingScreen(new AndroidStartScreenRecordingOptions()
                .withVideoSize("1280x720")
                .withTimeLimit(Duration.ofSeconds(200)));
    }

    private static void attachArtifactsOnFail() {
        if (!WebDriverRunner.hasWebDriverStarted()) {
            return;
        }
        Attach.screenshotAs("Screenshot of failed step");
        Attach.videoAs("Video of failed test");
        Attach.attachAsText("Page Source", android().getPageSource());
    }
}
