package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@WebTest
public class ProfileTest {

  private final Config CFG = Config.getInstance();

  @User(
          username = "testUser3",
          categories = @Category(archived = true)
  )

  @DisplayName("Архивная категория должна присутствовать в списке категорий")
  @Test
  void archivedCategoryShouldPresentInCategoriesList(CategoryJson categoryJson) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
            .fillLoginPage("testUser3", "34567")
            .checkThatPageLoaded()
            .navigateToProfilePage()
            .showArchivedCategories()
            .checkCategoryInCategoryList(categoryJson.name());
  }

  @User(
          username = "testUser3",
          categories = @Category(archived = false)
  )
  @DisplayName("Активная категория должна присутствовать в списке категорий")
  @Test
  void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
            .fillLoginPage("testUser3", "34567")
            .checkThatPageLoaded()
            .navigateToProfilePage()
            .checkCategoryInCategoryList(category.name());
  }
}
