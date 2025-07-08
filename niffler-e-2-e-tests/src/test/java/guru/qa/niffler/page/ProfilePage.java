package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;

import static com.codeborne.selenide.Condition.attributeMatching;
import static com.codeborne.selenide.Condition.disabled;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProfilePage {
  private final SelenideElement avatar = $("#image__input").parent().$("img");
  private final SelenideElement userName = $("#username");
  private final SelenideElement nameInput = $("#name");
  private final SelenideElement photoInput = $("input[type='file']");
  private final SelenideElement submitButton = $("button[type='submit']");
  private final SelenideElement categoryInput = $("input[name='category']");
  private final SelenideElement archivedSwitcher = $(".MuiSwitch-input");
  private final ElementsCollection bubbles = $$(".MuiChip-filled.MuiChip-colorPrimary");
  private final ElementsCollection bubblesArchived = $$(".MuiChip-filled.MuiChip-colorDefault");
  private final ElementsCollection categoryList = $$("[class*='MuiChip-labelMedium']");
  private final SelenideElement showArchivedCheckbox = $("input[type='checkbox']");

  public ProfilePage setName(String name) {
    nameInput.clear();
    nameInput.setValue(name);
    return this;
  }

  public ProfilePage uploadPhotoFromClasspath(String path) {
    photoInput.uploadFromClasspath(path);
    return this;
  }

  public ProfilePage addCategory(String category) {
    categoryInput.setValue(category).pressEnter();
    return this;
  }

  public ProfilePage checkCategoryExists(String category) {
    bubbles.find(text(category)).shouldBe(visible);
    return this;
  }

  public ProfilePage checkArchivedCategoryExists(String category) {
    archivedSwitcher.click();
    bubblesArchived.find(text(category)).shouldBe(visible);
    return this;
  }

  public ProfilePage checkUsername(String username) {
    this.userName.should(value(username));
    return this;
  }

  public ProfilePage checkName(String name) {
    nameInput.shouldHave(value(name));
    return this;
  }

  public ProfilePage checkPhotoExist() {
    avatar.should(attributeMatching("src", "data:image.*"));
    return this;
  }

  public ProfilePage checkThatCategoryInputDisabled() {
    categoryInput.should(disabled);
    return this;
  }

  public ProfilePage submitProfile() {
    submitButton.click();
    return this;
  }

  public static ProfilePage open(){
    Config CFG = Config.getInstance();
    return Selenide.open(CFG.profilePageUrl(), ProfilePage.class);
  }

  public void checkCategoryInCategoryList(String categoryName) {
    categoryList.findBy(text(categoryName)).should(visible);
  }

  public ProfilePage showArchivedCategories() {
    showArchivedCheckbox.click();
    return this;
  }
}
