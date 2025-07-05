package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import org.junit.jupiter.api.Assertions;

import java.util.List;

import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$x;

public class FriendsPage {
    private final ElementsCollection friendsList =
            $$x("//p[contains(@class, 'MuiTypography-body') and text()]");


    public static FriendsPage open() {
        Config CFG = Config.getInstance();
        return Selenide.open(CFG.friendsPageUrl(), FriendsPage.class);
    }

    public void checkExistingFriends(String friend) {
        friendsList.shouldHave(itemWithText(friend));
    }

    public void checkThatFriendsDoNotExist() {
        friendsList.should(size(0));
    }
}
