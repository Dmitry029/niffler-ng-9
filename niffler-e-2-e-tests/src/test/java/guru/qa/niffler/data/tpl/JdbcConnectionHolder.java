package guru.qa.niffler.data.tpl;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/*
Это класс содержащий соединения с БД - URL БД - соединение. При многопоточном выполнении
каждый поток будет иметь свое соединение. У нас 3 потока см DataSources dsBean.setPoolSize(3);
 */
public class JdbcConnectionHolder implements AutoCloseable {

    private final DataSource dataSource;
    private final Map<Long, Connection> threadConnections = new ConcurrentHashMap<>(); // Long это id потока

    public JdbcConnectionHolder(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection connection() {
        return threadConnections.computeIfAbsent(
                Thread.currentThread().threadId(),
                key -> {
                    try {
                        return dataSource.getConnection();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    @Override
    public void close() {
        // закрываем поток remove не только достает поток как get, но и удаляет его из Map
        Optional.ofNullable(threadConnections.remove(Thread.currentThread().threadId()))
                .ifPresent(connection -> {
                    try {
                        if (!connection.isClosed()) {
                            connection.close();
                        }
                    } catch (SQLException e) {
                        // NOP
                    }
                });
    }

    // проверяем, что после окончания работы в данном холдере все соединения закрыты и, если нет закрываем их
    public void closeAllConnections() {
        threadConnections.values().forEach(connection -> {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                // NOP
            }
        });
    }
}
