package mipkasnow.dev.android.util;

import com.codeborne.selenide.Selenide;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import mipkasnow.dev.android.allure.Attach;
import mipkasnow.dev.android.config.AndroidConfig;
import org.junit.jupiter.api.Assertions;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

import static mipkasnow.dev.android.util.Drivers.android;
import static org.openqa.selenium.OutputType.BYTES;

@UtilityClass
public class ScreenDiff {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Duration TIMEOUT = Duration.ofSeconds(10);
    private static final Duration INTERVAL = Duration.ofMillis(500);
    private static final Duration BASELINE_DELAY = Duration.ofSeconds(3);

    public static void check(Screenshot screenshot) {
        Path expectedPath = screenshot.path();
        try {
            Files.createDirectories(expectedPath.getParent());
            if (AndroidConfig.screenshotOverride() || Files.notExists(expectedPath)) {
                Selenide.sleep(BASELINE_DELAY.toMillis());
                writePng(capture(), expectedPath);
                if (AndroidConfig.screenshotOverride()) {
                    return;
                }
                Assertions.fail("Эталон скриншота создан: " + expectedPath + ". Перезапусти тест.");
            }

            BufferedImage expected = ImageIO.read(expectedPath.toFile());
            long deadline = System.nanoTime() + TIMEOUT.toNanos();
            BufferedImage actual;
            ImageDiff diff;
            do {
                actual = capture();
                diff = new ImageDiffer().makeDiff(expected, actual);
                if (!diff.hasDiff()) {
                    return;
                }
                if (System.nanoTime() >= deadline) {
                    break;
                }
                Selenide.sleep(INTERVAL.toMillis());
            } while (true);

            byte[] expectedBytes = toPngBytes(expected);
            byte[] actualBytes = toPngBytes(actual);
            byte[] diffBytes = toPngBytes(diff.getMarkedImage());
            Attach.screenshotPng("expected", expectedBytes);
            Attach.screenshotPng("actual", actualBytes);
            Attach.screenshotPng("diff", diffBytes);
            Attach.imageDiff("screen-diff", toImageDiffJson(expectedBytes, actualBytes, diffBytes));
            Assertions.fail("Скриншот отличается от эталона: " + screenshot
                    + " (diff size=" + diff.getDiffSize() + ")");
        } catch (IOException e) {
            throw new UncheckedIOException("Не удалось проверить скриншот: " + screenshot, e);
        }
    }

    private static BufferedImage capture() throws IOException {
        byte[] png = Selenide.screenshot(BYTES);
        if (png == null) {
            throw new IllegalStateException("Скриншот не получен");
        }
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(png));
        int header = barHeight("statusBar");
        int footer = barHeight("navigationBar");
        int height = image.getHeight() - header - footer;
        if (height <= 0 || header < 0 || footer < 0) {
            throw new IllegalStateException("Некорректный кроп скриншота: header=" + header
                    + ", footer=" + footer + ", image=" + image.getWidth() + "x" + image.getHeight());
        }
        return image.getSubimage(0, header, image.getWidth(), height);
    }

    private static int barHeight(String barName) {
        Map<String, Map<String, Object>> bars = android().getSystemBars();
        Map<String, Object> bar = bars.get(barName);
        if (bar == null) {
            return 0;
        }
        Object visible = bar.get("visible");
        if (visible instanceof Boolean shown && !shown) {
            return 0;
        }
        Object height = bar.get("height");
        if (height instanceof Number number) {
            return number.intValue();
        }
        return 0;
    }

    private static String toImageDiffJson(byte[] expected, byte[] actual, byte[] diff) throws IOException {
        Map<String, String> payload = new LinkedHashMap<>();
        payload.put("expected", toDataUrl(expected));
        payload.put("actual", toDataUrl(actual));
        payload.put("diff", toDataUrl(diff));
        return MAPPER.writeValueAsString(payload);
    }

    private static String toDataUrl(byte[] png) {
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(png);
    }

    private static void writePng(BufferedImage image, Path path) throws IOException {
        ImageIO.write(image, "PNG", path.toFile());
    }

    private static byte[] toPngBytes(BufferedImage image) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", out);
        return out.toByteArray();
    }
}
