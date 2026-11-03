package mipkasnow.dev.android.test;

import mipkasnow.dev.android.data.DemoUser;
import mipkasnow.dev.android.jupiter.annotation.meta.AndroidTest;
import mipkasnow.dev.android.screen.EventsScreen;
import mipkasnow.dev.android.screen.message.ToastMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@AndroidTest
@DisplayName("Edit profile")
class EditProfileTest {

    @Test
    @DisplayName("Успешное изменение имени и био")
    void userCanUpdateDisplayNameAndBio() {
        String displayName = "Auto Test User";
        String bio = "Automation bio for Edit Profile test";

        new EventsScreen()
                .checkThatScreenLoaded()
                .openProfile()
                .checkThatScreenLoaded()
                .checkThatSignedOut()
                .openSignIn()
                .checkThatScreenLoaded()
                .loginAsDemoUser()
                .checkThatScreenLoaded()
                .checkThatLoggedInAs(DemoUser.EMAIL)
                .openEditProfile()
                .checkThatScreenLoaded()
                .fillProfile(displayName, bio)
                .save()
                .checkToast(ToastMessage.PROFILE_UPDATED)
                .checkThatDisplayName(displayName)
                .checkThatBio(bio);
    }
}
