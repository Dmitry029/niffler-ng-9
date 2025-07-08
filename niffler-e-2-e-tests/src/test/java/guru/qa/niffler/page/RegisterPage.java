package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {
  private final SelenideElement usernameInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement passwordSubmitInput = $("input[name='passwordSubmit']");
  private final SelenideElement submitButton = $("button[type='submit']");
  private final SelenideElement proceedLoginButton = $(".form_sign-in");
  private final SelenideElement errorContainer = $(".form__error");
  private final SelenideElement congratulationMessage = $(".form__paragraph_success");

  public RegisterPage fillRegisterPage(String login, String password, String passwordSubmit) {
    usernameInput.setValue(login);
    passwordInput.setValue(password);
    passwordSubmitInput.setValue(passwordSubmit);
    return this;
  }

  public LoginPage successSubmit() {
    submit();
    proceedLoginButton.click();
    return new LoginPage();
  }

  public RegisterPage submit() {
    submitButton.click();
    return this;
  }

  public RegisterPage checkAlertMessage(String errorMessage) {
    errorContainer.shouldHave(text(errorMessage));
    return this;
  }

  public void checkSuccessfulRegistration(String congratulation) {
    congratulationMessage.shouldHave(text(congratulation));
  }

  public void checkUnsuccessfulRegistrationWithExistUserName(String text) {
    errorContainer.shouldHave(text(text));
  }

  public void checkUnsuccessfulRegistrationIfPasswordAndConfirmPasswordAreNotEqual(String text) {
    errorContainer.shouldBe(text(text));
  }

}
