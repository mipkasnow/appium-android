package mipkasnow.dev.android.screen;

import com.codeborne.selenide.appium.SelenideAppiumElement;
import io.qameta.allure.Step;
import mipkasnow.dev.android.util.AndroidActions;

import static com.codeborne.selenide.Condition.appear;
import static mipkasnow.dev.android.util.AndroidElements.byExactText;
import static mipkasnow.dev.android.util.AndroidElements.byResourceId;

public final class EditProfileScreen extends BaseScreen<EditProfileScreen> {

    private final SelenideAppiumElement
            title = byExactText("Edit profile"),
            displayNameInput = byResourceId("editProfile.displayNameInput"),
            bioInput = byResourceId("editProfile.bioInput"),
            saveButton = byResourceId("editProfile.saveButton");

    @Override
    @Step("Проверить, что экран Edit profile загружен")
    public EditProfileScreen checkThatScreenLoaded() {
        title.should(appear);
        displayNameInput.should(appear);
        bioInput.should(appear);
        saveButton.should(appear);
        return this;
    }

    @Step("Заполнить профиль: имя {displayName}, био {bio}")
    public EditProfileScreen fillProfile(String displayName, String bio) {
        displayNameInput.setValue(displayName);
        bioInput.setValue(bio);
        return this;
    }

    @Step("Сохранить профиль")
    public ProfileScreen save() {
        AndroidActions.hideKeyboard();
        saveButton.click();
        return new ProfileScreen();
    }
}
