package mipkasnow.dev.android.config;

import lombok.experimental.UtilityClass;

import java.nio.file.Path;
import java.util.Optional;

@UtilityClass
public final class AndroidConfig {

    private static final String DEFAULT_APPIUM_URL = "http://127.0.0.1:4723";
    private static final Path DEFAULT_APK = Path.of("src/test/resources/apk/FrontRow.apk");

    public static String appiumUrl() {
        return System.getProperty("appium.url", DEFAULT_APPIUM_URL);
    }

    public static Optional<String> udid() {
        String value = System.getProperty("android.udid");
        if (value == null || value.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(value.trim());
    }

    public static Path apkPath() {
        String override = System.getProperty("android.apk");
        if (override != null && !override.isBlank()) {
            return Path.of(override).toAbsolutePath().normalize();
        }
        return DEFAULT_APK.toAbsolutePath().normalize();
    }

    public static String appPackage() {
        return System.getProperty("android.appPackage", "app.frontrow.qa");
    }

    public static boolean screenshotOverride() {
        return Boolean.parseBoolean(System.getProperty("screenshot.override", "false"));
    }

    public static Path screenshotBaselineDir() {
        String override = System.getProperty("screenshot.dir");
        if (override != null && !override.isBlank()) {
            return Path.of(override).toAbsolutePath().normalize();
        }
        return Path.of("src/test/resources/screenshots").toAbsolutePath().normalize();
    }
}
