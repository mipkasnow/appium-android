package mipkasnow.dev.android.screen.component;

import com.codeborne.selenide.appium.SelenideAppiumElement;
import io.qameta.allure.Step;
import mipkasnow.dev.android.config.AndroidConfig;

import static mipkasnow.dev.android.util.AndroidElements.byResourceId;

public final class CropPhotoComponent {

    private final SelenideAppiumElement cropButton = byResourceId(AndroidConfig.appPackage() + ":id/crop_image_menu_crop");

    @Step("Подтвердить обрезку фото")
    public void confirm() {
        cropButton.click();
    }
}
