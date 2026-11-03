package mipkasnow.dev.android.allure;

import io.qameta.allure.Attachment;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.util.Base64;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static mipkasnow.dev.android.util.Drivers.android;

@UtilityClass
public class Attach {

    @Attachment(value = "{attachName}", type = "text/plain")
    public static String attachAsText(String attachName, String message) {
        return message;
    }

    @Attachment(value = "{attachName}", type = "image/png")
    public static byte[] screenshotAs(String attachName) {
        return ((TakesScreenshot) getWebDriver()).getScreenshotAs(OutputType.BYTES);
    }

    @Attachment(value = "{attachName}", type = "video/mp4", fileExtension = ".mp4")
    public static byte[] videoAs(String attachName) {
        return Base64.getDecoder().decode(android().stopRecordingScreen());
    }

    @Attachment(value = "{attachName}", type = "image/png", fileExtension = ".png")
    public static byte[] screenshotPng(String attachName, byte[] png) {
        return png;
    }

    @Attachment(value = "{attachName}", type = "application/vnd.allure.image.diff", fileExtension = ".json")
    public static String imageDiff(String attachName, String json) {
        return json;
    }
}
