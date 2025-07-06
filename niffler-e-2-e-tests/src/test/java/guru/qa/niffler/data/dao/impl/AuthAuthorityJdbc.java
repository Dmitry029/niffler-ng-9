package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuthAuthorityJdbc implements AuthAuthorityDao {

    private static final Config CFG = Config.getInstance();

    private final Connection connection;

    public AuthAuthorityJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createUser(AuthAuthorityEntity... authAuthorityEntity) {
        try (
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO authority (user_id, authority) " +
                                "VALUES (?, ?)",
                        Statement.RETURN_GENERATED_KEYS
                )) {
            for (AuthAuthorityEntity authority : authAuthorityEntity) {
                ps.setObject(1, authority.getUser().getId());
                ps.setString(2, authority.getAuthority().name());
                ps.addBatch();
                ps.clearParameters();
            }

            ps.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthAuthorityEntity> findAll() {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM authority")) {
            ps.execute();
            List<AuthAuthorityEntity> result = new ArrayList<>();
            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    result.add(mapperAuthAuthorityEntity(rs));
                }
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthAuthorityEntity> findAllByUserId(UUID userId) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM authority where user_id = ?")) {
            ps.setObject(1, userId);
            ps.execute();
            List<AuthAuthorityEntity> result = new ArrayList<>();
            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    result.add(mapperAuthAuthorityEntity(rs));
                }
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private AuthAuthorityEntity mapperAuthAuthorityEntity(ResultSet rs) throws SQLException {
        AuthAuthorityEntity ae = new AuthAuthorityEntity();
        ae.setId(rs.getObject("id", UUID.class));
        ae.setUser(new AuthUserEntity(rs.getObject("user_id", UUID.class)));
        ae.setAuthority(Authority.valueOf(rs.getString("authority")));
        return ae;
    }
}
