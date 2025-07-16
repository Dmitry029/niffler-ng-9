package guru.qa.niffler.data.tpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Connections {

    private Connections() {
    }

    private static final Map<String, JdbcConnectionHolder> holders = new ConcurrentHashMap<>();

    public static JdbcConnectionHolder holder(String jdbcUrl) {
        return holders.computeIfAbsent(
                jdbcUrl,
                key -> new JdbcConnectionHolder(DataSources.dataSource(jdbcUrl))
        );
    }

    public static JdbcConnectionHolders holders(String... jdbcUrl) {
        List<JdbcConnectionHolder> holderList = new ArrayList<>();
        for (String url : jdbcUrl) {
            holderList.add(holder(url));
        }
        return new JdbcConnectionHolders(holderList);
    }

    // проверяем, что после окончания работы во всех холдерах все соединения закрыты и, если нет закрываем их
    public static void closeAllConnections() {
        holders.values().forEach(JdbcConnectionHolder::closeAllConnections);
    }

}
