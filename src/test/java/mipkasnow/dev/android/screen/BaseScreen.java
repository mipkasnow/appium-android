package mipkasnow.dev.android.screen;

import io.qameta.allure.Step;
import mipkasnow.dev.android.screen.message.ToastMessage;
import org.jspecify.annotations.NullMarked;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.exactText;

@NullMarked
public abstract class BaseScreen<T extends BaseScreen<?>> {

    public abstract T checkThatScreenLoaded();

    @Step("Проверить тост: {toast}")
    @SuppressWarnings("unchecked")
    public T checkToast(ToastMessage toast) {
        toast.element().should(appear).shouldHave(exactText(toast.text()));
        return (T) this;
    }
}
