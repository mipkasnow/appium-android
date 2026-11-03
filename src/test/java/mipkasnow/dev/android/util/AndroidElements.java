package mipkasnow.dev.android.util;

import com.codeborne.selenide.appium.SelenideAppiumCollection;
import com.codeborne.selenide.appium.SelenideAppiumElement;
import io.appium.java_client.AppiumBy;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.By;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.SelenideAppium.$$;
import static com.codeborne.selenide.appium.SelenideAppium.$x;
import static org.openqa.selenium.By.xpath;

@UtilityClass
public class AndroidElements {

    public static SelenideAppiumElement byAccessibilityId(String accessibilityId) {
        return $(AppiumBy.accessibilityId(accessibilityId));
    }

    public static SelenideAppiumElement byId(String id) {
        return $(By.id(id));
    }

    public static SelenideAppiumElement byResourceId(String resourceId) {
        return $x("//*[@resource-id='" + resourceId + "']");
    }

    public static SelenideAppiumElement byExactText(String text) {
        return $x("//*[@text='" + text + "']");
    }

    public static SelenideAppiumElement byTextContains(String text) {
        return $x("//*[contains(@text,\"" + text + "\")]");
    }

    public static SelenideAppiumElement byContentDescContains(String contentDesc) {
        return $x("//*[contains(@content-desc,\"" + contentDesc + "\")]");
    }

    public static SelenideAppiumElement byXpath(String xpathExpression) {
        return $x(xpathExpression);
    }

    public static SelenideAppiumCollection collectionByResourceId(String resourceId) {
        return $$(xpath("//*[@resource-id='" + resourceId + "']"));
    }

    public static SelenideAppiumCollection collectionByAccessibilityId(String accessibilityId) {
        return $$(AppiumBy.accessibilityId(accessibilityId));
    }
}
