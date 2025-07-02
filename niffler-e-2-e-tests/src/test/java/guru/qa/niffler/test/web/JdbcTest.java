package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendDbClient;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class JdbcTest {
    @Test
    void daoTest() {
        SpendDbClient spend = new SpendDbClient();

        spend.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                "test-cat-name",
                                "duck",
                                false
                        ),
                        CurrencyValues.RUB,
                        100.0,
                        "test desc",
                        "duck"
                )
        );
        System.out.println(spend);
    }
}
