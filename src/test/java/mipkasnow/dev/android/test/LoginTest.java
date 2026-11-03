package mipkasnow.dev.android.test;

import mipkasnow.dev.android.data.DemoUser;
import mipkasnow.dev.android.jupiter.annotation.meta.AndroidTest;
import mipkasnow.dev.android.util.ScreenDiff;
import mipkasnow.dev.android.util.Screenshot;
import mipkasnow.dev.android.screen.EventsScreen;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@AndroidTest
@DisplayName("Login")
class LoginTest {

    @Test
    @DisplayName("Успешный логин демо-пользователем через Profile")
    void demoUserCanSignInViaProfile() {
        new EventsScreen()
                .checkThatScreenLoaded()
                .openProfile()
                .checkThatScreenLoaded()
                .checkThatSignedOut()
                .openSignIn()
                .checkThatScreenLoaded()
                .loginAsDemoUser()
                .checkThatScreenLoaded()
                .checkThatLoggedInAs(DemoUser.EMAIL);

        ScreenDiff.check(Screenshot.PROFILE_LOGGED_IN);
    }

    @Test
    @DisplayName("Невалидные креды показывают Sign in failed")
    void invalidCredentialsShowSignInFailed() {
        new EventsScreen()
                .checkThatScreenLoaded()
                .openProfile()
                .checkThatScreenLoaded()
                .checkThatSignedOut()
                .openSignIn()
                .checkThatScreenLoaded()
                .loginAs("bad@example.com", "wrongpass")
                .checkThatOpened("Sign in failed", "Email or password is incorrect.")
                .clickOk();
    }
}
