package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UdUserDao;
import guru.qa.niffler.data.dao.impl.UdUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryJdbc;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.UserJson;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;

public class UsersDbClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final AuthUserRepository authUserRepository = new AuthUserRepositoryJdbc();
    private final UdUserDao udUserDao = new UdUserDaoSpringJdbc();

    // чтобы 2 запроса в User и Auth создались одновременно используем транзакцию
    TransactionTemplate txTemplate = new TransactionTemplate(
        new JdbcTransactionManager(
            DataSources.dataSource(CFG.authJdbcUrl())
        )
    );

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
        CFG.authJdbcUrl(),
        CFG.userdataJdbcUrl()
    );

    public UserJson createUserSpringJdbc(UserJson user) {

        return xaTransactionTemplate.execute(() -> {
            AuthUserEntity authUser = new AuthUserEntity();
            authUser.setUsername(user.username());
            authUser.setPassword(pe.encode("12345"));
            authUser.setEnabled(true);
            authUser.setAccountNonExpired(true);
            authUser.setAccountNonLocked(true);
            authUser.setCredentialsNonExpired(true);
            authUser.setAuthorities(
                Arrays.stream(Authority.values()).map(
                    e -> {
                        AuthorityEntity ae = new AuthorityEntity();
                        ae.setUser(authUser);
                        ae.setAuthority(e);
                        return ae;
                    }
                ).toList()
            );

            authUserRepository.create(authUser);

            return UserJson.fromEntity(
                udUserDao.create(UserEntity.fromJson(user)),
                null
            );
        });
    }

   /* public UserJson createUserWithoutSpringJdbcTransaction(UserJson user) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(user.username());
        authUser.setPassword("12345");
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);

        AuthUserEntity createdAuthUser = authUserDao.create(authUser);

        AuthorityEntity[] authorityEntities = Arrays.stream(
            Authority.values()
        ).map(
            e -> {
                AuthorityEntity ae = new AuthorityEntity();
                ae.getUser().setId(createdAuthUser.getId());
                ae.setAuthority(e);
                return ae;
            }
        ).toArray(AuthorityEntity[]::new);

        authAuthorityDao.create(authorityEntities);
        return UserJson.fromEntity(
            udUserDao.create(UserEntity.fromJson(user)), null
        );
    }

    public UserJson createUserJdbcTransaction(UserJson user) {
        return txTemplate.execute(status -> {
                AuthUserEntity authUser = new AuthUserEntity();
                authUser.setUsername(null);
                authUser.setPassword("12345");
                authUser.setEnabled(true);
                authUser.setAccountNonExpired(true);
                authUser.setAccountNonLocked(true);
                authUser.setCredentialsNonExpired(true);

                AuthUserEntity createdAuthUser = authUserDao.create(authUser);

                AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                    e -> {
                        AuthorityEntity ae = new AuthorityEntity();
                        ae.getUser().setId(createdAuthUser.getId());
                        ae.setAuthority(e);
                        return ae;
                    }
                ).toArray(AuthorityEntity[]::new);
                authAuthorityDao.create(authorityEntities);
                return UserJson.fromEntity(
                    udUserDao.create(UserEntity.fromJson(user)), null
                );
            }
        );
    }

    public UserJson createUserWithoutJdbcTransaction(UserJson user) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(user.username());
        authUser.setPassword("12345");
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);

        AuthUserEntity createdAuthUser = authUserDao.create(authUser);

        AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
            e -> {
                AuthorityEntity ae = new AuthorityEntity();
                ae.getUser().setId(createdAuthUser.getId());
                ae.setAuthority(e);
                return ae;
            }
        ).toArray(AuthorityEntity[]::new);

        authAuthorityDao.create(authorityEntities);
        return UserJson.fromEntity(
            udUserDao.create(UserEntity.fromJson(user)), null
        );
    }*/
}
