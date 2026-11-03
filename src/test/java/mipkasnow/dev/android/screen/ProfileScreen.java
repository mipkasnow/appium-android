package mipkasnow.dev.android.screen;

import com.codeborne.selenide.appium.SelenideAppiumElement;
import io.qameta.allure.Step;
import mipkasnow.dev.android.screen.component.TabBarComponent;

import static com.codeborne.selenide.Condition.appear;
import static mipkasnow.dev.android.util.AndroidElements.byExactText;
import static mipkasnow.dev.android.util.AndroidElements.byResourceId;

public final class ProfileScreen extends BaseScreen<ProfileScreen> {

    private final SelenideAppiumElement
            title = byExactText("Profile"),
            signedOutHint = byExactText("Sign in to manage your account and tickets."),
            signInButton = byResourceId("profile.signInButton"),
            avatar = byResourceId("profile.avatar"),
            signOutButton = byResourceId("profile.signOutButton"),
            editProfileButton = byResourceId("profile.editButton"),
            settingsButton = byResourceId("profile.settingsButton");

    private final TabBarComponent tabBar = new TabBarComponent();

    @Override
    @Step("Проверить, что экран Profile загружен")
    public ProfileScreen checkThatScreenLoaded() {
        title.should(appear);
        return this;
    }

    @Step("Проверить, что пользователь не авторизован")
    public ProfileScreen checkThatSignedOut() {
        signedOutHint.should(appear);
        signInButton.should(appear);
        return this;
    }

    @Step("Проверить, что выполнен вход как {email}")
    public ProfileScreen checkThatLoggedInAs(String email) {
        byExactText(email).should(appear);
        avatar.should(appear);
        signOutButton.should(appear);
        editProfileButton.should(appear);
        return this;
    }

    @Step("Открыть экран входа")
    public LoginScreen openSignIn() {
        signInButton.click();
        return new LoginScreen();
    }

    @Step("Открыть редактирование профиля")
    public EditProfileScreen openEditProfile() {
        editProfileButton.click();
        return new EditProfileScreen();
    }

    @Step("Проверить отображаемое имя: {displayName}")
    public ProfileScreen checkThatDisplayName(String displayName) {
        byExactText(displayName).should(appear);
        return this;
    }

    @Step("Проверить био: {bio}")
    public ProfileScreen checkThatBio(String bio) {
        byExactText(bio).should(appear);
        return this;
    }

    @Step("Открыть настройки")
    public SettingsScreen openSettings() {
        settingsButton.click();
        return new SettingsScreen();
    }

    @Step("Выйти из аккаунта")
    public ProfileScreen signOut() {
        signOutButton.click();
        return this;
    }

    @Step("Открыть Events через навбар")
    public EventsScreen openEvents() {
        return tabBar.openEvents();
    }
}
