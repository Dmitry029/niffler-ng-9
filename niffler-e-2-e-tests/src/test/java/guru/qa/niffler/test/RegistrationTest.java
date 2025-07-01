package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.RegisterPage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class RegistrationTest {

    private final String username = RandomDataUtils.randomUserName();
    private final String password = String.valueOf(10000 + new Random().nextInt(90000));
    private final String ExistUserName = "testUser2";

    @BeforeEach
    void openPage(){
        Config CFG = Config.getInstance();
        Selenide.open(CFG.registerPageUrl(), RegisterPage.class);
    }

    @DisplayName("Тест на успешную регистрацию нового пользователя")
    @Test
    void shouldRegisterNewUse(){

        String successMessage ="Congratulations! You've registered!";

        new RegisterPage()
                .setUserName(username)
                .setPassword(password)
                .setPasswordSubmit(password)
                .submitRegistration()
                .checkSuccessfulRegistration(successMessage);
    }

    @DisplayName("Тест на невозможность регистрации с существующим именем пользователя")
    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        new RegisterPage()
                .setUserName(ExistUserName)
                .setPassword(password)
                .setPasswordSubmit(password)
                .submitRegistration()
                .checkUnsuccessfulRegistrationWithExistUserName("Username `testUser2` already exists");
    }

    @DisplayName("Тест на невозможность регистрации с неодинаковыми данными в поле пароль и подтверждение пароля")
    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        new RegisterPage()
                .setUserName(ExistUserName)
                .setPassword(password)
                .setPasswordSubmit(password + "1")
                .submitRegistration()
                .checkUnsuccessfulRegistrationIfPasswordAndConfirmPasswordAreNotEqual("Passwords should be equal");
    }
}
