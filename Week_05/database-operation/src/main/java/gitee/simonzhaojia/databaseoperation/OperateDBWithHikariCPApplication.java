package gitee.simonzhaojia.databaseoperation;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

//@Configuration
@Slf4j
public class OperateDBWithHikariCPApplication implements ApplicationRunner {

  @Value("${spring.datasource.url}")
  private String dataSourceUrl;

  @Value("${spring.datasource.username}")
  private String user;

  @Value("${spring.datasource.password}")
  private String password;

  @Bean
  public DataSource dataSource() {
    HikariConfig config = new HikariConfig();
    //数据源
    config.setJdbcUrl(dataSourceUrl);
    //用户名
    config.setUsername(user);
    //密码
    config.setPassword(password);
    config.addDataSourceProperty("cachePrepStmts", "true"); //是否自定义配置，为true时下面两个参数才生效
    config.addDataSourceProperty("prepStmtCacheSize", "250"); //连接池大小默认25，官方推荐250-500
    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048"); //单条语句最大长度默认256，官方推荐2048
    config.addDataSourceProperty("useServerPrepStmts", "true"); //新版本MySQL支持服务器端准备，开启能够得到显著性能提升
    config.addDataSourceProperty("useLocalSessionState", "true");
    config.addDataSourceProperty("useLocalTransactionState", "true");
    config.addDataSourceProperty("rewriteBatchedStatements", "true");
    config.addDataSourceProperty("cacheResultSetMetadata", "true");
    config.addDataSourceProperty("cacheServerConfiguration", "true");
    config.addDataSourceProperty("elideSetAutoCommits", "true");
    config.addDataSourceProperty("maintainTimeStats", "false");

    HikariDataSource hikariDataSource = new HikariDataSource(config);
    return hikariDataSource;
  }

  @Autowired
  private DataSource dataSource;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    Connection connection = dataSource.getConnection();
    log.info("connection.toString {}", connection.toString());
    PreparedStatement ps = connection.prepareStatement("SELECT * FROM team");
    ResultSet resultSet = ps.executeQuery();
    while (resultSet.next()) {
      log.info("team_id: {} ; team_name: {}", resultSet.getInt("team_id"), resultSet.getString("team_name"));
    }
    connection.close();
  }
}
