package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import lombok.AllArgsConstructor;

import static guru.qa.niffler.data.Databases.transaction;

public class SpendDbClient {

    private static final Config CFG = Config.getInstance();

    @AllArgsConstructor
    private enum IsolationLevel {
        READ_UNCOMMITTED(1),
        READ_COMMITTED(2),
        REPEATABLE_READ(3),
        SERIALIZABLE(4);

        private final int value;
    }

    public SpendJson createSpend(SpendJson spend) {

        return transaction(connection -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = new CategoryDaoJdbc(connection)
                                .create(spendEntity.getCategory());
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(
                            new SpendDaoJdbc(connection).create(spendEntity)
                    );
                },
                CFG.spendJdbcUrl(), IsolationLevel.READ_COMMITTED.value
        );
    }

    public CategoryJson createCategory(CategoryJson categoryJson) {
        return transaction(
                connection -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);
                    return CategoryJson.fromEntity(new CategoryDaoJdbc(connection).create(categoryEntity));
                },
                CFG.spendJdbcUrl(), IsolationLevel.READ_COMMITTED.value
        );
    }

    public void deleteCategory(CategoryJson categoryJson) {
        transaction(connection -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);
                    new CategoryDaoJdbc(connection).deleteCategory(categoryEntity);
                    return null;
                },
                CFG.spendJdbcUrl(), IsolationLevel.READ_COMMITTED.value
        );
    }
}
