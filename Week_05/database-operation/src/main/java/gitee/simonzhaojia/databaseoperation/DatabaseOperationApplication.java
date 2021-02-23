package gitee.simonzhaojia.databaseoperation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

@SpringBootApplication
@Slf4j
public class DatabaseOperationApplication {


  public static void main(String[] args) throws SQLException {
    //使用JDBC的原生接口实现增删改查
//    SpringApplication.run(OperateDatabaseWithNativeJdbcDriverApplication.class, args);
    //使用PrepareStatement实现增删改查
    SpringApplication.run(OperateDBWithPrepareStatementApplication.class, args);
//    SpringApplication.run(OperateDBWithHikariCPApplication.class, args);
  }

}
