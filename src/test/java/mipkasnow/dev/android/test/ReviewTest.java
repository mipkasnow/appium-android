package mipkasnow.dev.android.test;

import mipkasnow.dev.android.data.DemoUser;
import mipkasnow.dev.android.jupiter.annotation.meta.AndroidTest;
import mipkasnow.dev.android.screen.EventsScreen;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@AndroidTest
@DisplayName("Reviews")
class ReviewTest {

    @Test
    @DisplayName("Успешное добавление отзыва со звездами и фото")
    void userCanPostReviewWithPhoto() {
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
                .openEvents()
                .checkThatScreenLoaded()
                .openInbox()
                .checkThatScreenLoaded()
                .openNotification("Doors open in 1 hour")
                .checkThatScreenLoaded()
                .openReviews()
                .checkThatScreenLoaded()
                .rate(5)
                .attachPhoto()
                .checkThatPhotoAttached()
                .post()
                .checkThatReviewWithPhotoPosted();
    }
}
