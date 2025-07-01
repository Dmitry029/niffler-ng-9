package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class SpendingTest {

    private static final Config CFG = Config.getInstance();

    @User(
            username = "testUser3",
            spendings = @Spending(
                    category = "Хобби",
                    description = "Для удовольствия!",
                    amount = 10000
            )
    )

    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin(SpendJson spendJson) {
        final String newDescription = ":)";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage("testUser3", "34567")
                .submit()
                .checkThatPageLoaded()
                .editSpending(spendJson.description())
                .setNewSpendingDescription(newDescription)
                .save()
                .checkThatTableContainsSpending(newDescription)
        ;
    }
}
