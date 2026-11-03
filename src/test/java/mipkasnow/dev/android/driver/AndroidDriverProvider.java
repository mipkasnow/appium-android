package mipkasnow.dev.android.driver;

import com.codeborne.selenide.WebDriverProvider;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.remote.AutomationName;
import mipkasnow.dev.android.config.AndroidConfig;
import org.jspecify.annotations.NonNull;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

public class AndroidDriverProvider implements WebDriverProvider {

    @Override
    public @NonNull WebDriver createDriver(@NonNull Capabilities capabilities) {
        Path apk = AndroidConfig.apkPath();
        if (!Files.isRegularFile(apk)) {
            throw new IllegalStateException("APK not found: " + apk);
        }

        UiAutomator2Options options = new UiAutomator2Options();
        options.merge(capabilities);
        options.setAutomationName(AutomationName.ANDROID_UIAUTOMATOR2);
        options.setPlatformName("Android");
        options.setApp(apk.toString());
        options.setAppPackage(AndroidConfig.appPackage());
        options.setNewCommandTimeout(Duration.ofSeconds(600));
        options.autoGrantPermissions();
        AndroidConfig.udid().ifPresent(options::setUdid);

        try {
            return new AndroidDriver(URI.create(AndroidConfig.appiumUrl()).toURL(), options);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create AndroidDriver at " + AndroidConfig.appiumUrl(), e);
        }
    }
}
