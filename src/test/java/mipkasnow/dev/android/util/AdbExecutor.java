package mipkasnow.dev.android.util;

import dadb.AdbShellResponse;
import dadb.Dadb;
import dadb.adbserver.AdbServer;
import io.qameta.allure.Step;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import mipkasnow.dev.android.config.AndroidConfig;

import java.nio.file.Files;
import java.nio.file.Path;

@UtilityClass
public class AdbExecutor {

    private static final String ADB_SERVER_HOST = "localhost";
    private static final int ADB_SERVER_PORT = 5037;

    @Step("Сбросить данные приложения под тестом")
    public static void clearAppData() {
        clearAppData(AndroidConfig.appPackage());
    }

    @SneakyThrows
    @Step("Сбросить данные приложения {packageName}")
    public static void clearAppData(String packageName) {
        try (Dadb dadb = connect()) {
            AdbShellResponse response = dadb.shell("pm clear " + packageName);
            if (response.getExitCode() != 0) {
                throw new IllegalStateException(
                        "pm clear failed for " + packageName + ": " + response.getAllOutput()
                );
            }
        }
    }

    @SneakyThrows
    @Step("Скопировать файл на устройство: {remotePath}")
    public static void push(Path localFile, String remotePath) {
        if (!Files.isRegularFile(localFile)) {
            throw new IllegalStateException("File not found: " + localFile);
        }
        try (Dadb dadb = connect()) {
            dadb.push(localFile.toFile(), remotePath, 420, localFile.toFile().lastModified());
        }
    }

    private static Dadb connect() {
        return AndroidConfig.udid()
                .map(AdbExecutor::connectToSerial)
                .orElseGet(AdbExecutor::discoverDevice);
    }

    private static Dadb discoverDevice() {
        Dadb dadb = Dadb.discover();
        if (dadb == null) {
            throw new IllegalStateException("No adb device found");
        }
        return dadb;
    }

    private static Dadb connectToSerial(String serial) {
        if (serial.startsWith("emulator-")) {
            int consolePort = Integer.parseInt(serial.substring("emulator-".length()));
            return Dadb.create(ADB_SERVER_HOST, consolePort + 1);
        }
        return AdbServer.createDadb(
                ADB_SERVER_HOST,
                ADB_SERVER_PORT,
                "host:transport:" + serial
        );
    }
}
