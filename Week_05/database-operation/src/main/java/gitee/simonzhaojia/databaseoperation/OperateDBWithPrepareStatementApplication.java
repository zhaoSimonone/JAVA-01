package gitee.simonzhaojia.databaseoperation;

import gitee.simonzhaojia.databaseoperation.jdbc.OperateDBWithPrepareStatement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
@Slf4j
public class OperateDBWithPrepareStatementApplication implements ApplicationRunner {
  @Bean
  public OperateDBWithPrepareStatement operateDBWithPrepareStatement() throws SQLException, ClassNotFoundException {
    return new OperateDBWithPrepareStatement("jdbc:mysql://localhost:3306/nba", "com.mysql.cj.jdbc.Driver", "root", "123456");
  }

  @Autowired
  OperateDBWithPrepareStatement operateDBWithPrepareStatement;


  @Override
  public void run(ApplicationArguments args) throws Exception {
    log.info("connection toString {}", operateDBWithPrepareStatement.getConnection().toString());
    //查询
    ResultSet resultSet = operateDBWithPrepareStatement.queryAll();
    while (resultSet.next()) {
      log.info("team_id: {} ; team_name: {}", resultSet.getInt("team_id"), resultSet.getString("team_name"));
    }
    resultSet = operateDBWithPrepareStatement.queryById(1001);
    while (resultSet.next()) {
      log.info("team_id: {} ; team_name: {}", resultSet.getInt("team_id"), resultSet.getString("team_name"));
    }

//   // 插入
//    operateDBWithPrepareStatement.insert(1004, "芝加哥公牛");
//    //更新
//    operateDBWithPrepareStatement.updateById("洛杉矶湖人", 1004);
//    //删除
//    operateDBWithPrepareStatement.deleteById(1004);

    //开启事务
    operateDBWithPrepareStatement.startTransaction();
    try {
      //插入
      operateDBWithPrepareStatement.insert(1004, "芝加哥公牛");
      operateDBWithPrepareStatement.insert(1004, "华盛顿奇才");
    } catch (SQLException | ArithmeticException e) {
//      operateDBWithPrepareStatement.rollbackTransaction();
      operateDBWithPrepareStatement.close();
    }
  }
}
