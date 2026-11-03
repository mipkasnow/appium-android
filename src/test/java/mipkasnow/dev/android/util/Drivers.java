package mipkasnow.dev.android.util;

import com.codeborne.selenide.WebDriverRunner;
import io.appium.java_client.android.AndroidDriver;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.WebDriver;

@UtilityClass
public class Drivers {

    public static AndroidDriver android() {
        WebDriver driver = WebDriverRunner.getWebDriver();
        if (driver instanceof AndroidDriver androidDriver) {
            return androidDriver;
        }
        throw new ClassCastException("WebDriver cannot be cast to AndroidDriver: " + driver);
    }
}
