package gitee.simonzhaojia.databaseoperation;

import gitee.simonzhaojia.databaseoperation.jdbc.OperateDBWithNativeJdbcDriver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.ResultSet;
import java.sql.SQLException;

//@Configuration
@Slf4j
public class OperateDatabaseWithNativeJdbcDriverApplication implements ApplicationRunner {

  @Bean
  public OperateDBWithNativeJdbcDriver operateDBWithNativeJdbcDriver() throws SQLException, ClassNotFoundException {
    return new OperateDBWithNativeJdbcDriver("jdbc:mysql://localhost:3306/nba", "com.mysql.cj.jdbc.Driver", "root", "123456");
  }

  @Autowired
  OperateDBWithNativeJdbcDriver operateDBWithNativeJdbcDriver;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    log.info("connection toString {}", operateDBWithNativeJdbcDriver.getConnection().toString());
    //查询
    log.info("Before Insert");
    ResultSet resultSet = operateDBWithNativeJdbcDriver.query("SELECT * FROM team");
    while (resultSet.next()) {
      log.info("team_id: {} ; team_name: {}", resultSet.getInt("team_id"), resultSet.getString("team_name"));
    }
    //插入
    operateDBWithNativeJdbcDriver.insert("INSERT INTO team VALUES (1004, '布鲁克林篮网队')");
    log.info("After Insert & Before Update");
    resultSet = operateDBWithNativeJdbcDriver.query("SELECT * FROM team");
    while (resultSet.next()) {
      log.info("team_id: {} ; team_name: {}", resultSet.getInt("team_id"), resultSet.getString("team_name"));
    }
    //更新
    operateDBWithNativeJdbcDriver.update("UPDATE team SET team_name = '芝加哥公牛' WHERE team_id = 1004");
    log.info("After Update & Before Delete");
    resultSet = operateDBWithNativeJdbcDriver.query("SELECT * FROM team");
    while (resultSet.next()) {
      log.info("team_id: {} ; team_name: {}", resultSet.getInt("team_id"), resultSet.getString("team_name"));
    }
    //删除
    operateDBWithNativeJdbcDriver.delete("DELETE FROM team WHERE team_id = 1004");
    log.info("After Delete");
    resultSet = operateDBWithNativeJdbcDriver.query("SELECT * FROM team");
    while (resultSet.next()) {
      log.info("team_id: {} ; team_name: {}", resultSet.getInt("team_id"), resultSet.getString("team_name"));
    }
  }
}
