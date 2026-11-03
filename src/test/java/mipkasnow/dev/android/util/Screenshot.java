package mipkasnow.dev.android.util;

import mipkasnow.dev.android.config.AndroidConfig;

import java.nio.file.Path;

public enum Screenshot {

    PROFILE_LOGGED_IN("profile-logged-in.png");

    private final String fileName;

    Screenshot(String fileName) {
        this.fileName = fileName;
    }

    public Path path() {
        return AndroidConfig.screenshotBaselineDir().resolve(fileName);
    }
}
