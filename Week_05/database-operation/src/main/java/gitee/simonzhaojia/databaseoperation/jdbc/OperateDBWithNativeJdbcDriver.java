package gitee.simonzhaojia.databaseoperation.jdbc;

import java.sql.*;

public class OperateDBWithNativeJdbcDriver {

  private String url;
  private String driver;
  private String userName;
  private String userPassword;
  private Connection connection;
  private Statement statement;
  private ResultSet resultSet;

  public OperateDBWithNativeJdbcDriver(String url, String driver, String userName, String userPassword) throws ClassNotFoundException, SQLException {
    this.url = url;
    this.driver = driver;
    this.userName = userName;
    this.userPassword = userPassword;
    // 1、加载数据库驱动
    Class.forName(this.driver);
    // 2、获取数据库连接
    this.connection = DriverManager.getConnection(this.url, this.userName, this.userPassword);
    // 3、获取数据库操作对象
    this.statement = this.connection.createStatement();
  }

  public Connection getConnection() {
    return this.connection;
  }

  /**
   * 查询
   * @param sql
   * @return
   * @throws SQLException
   */
  public ResultSet query(String sql) throws SQLException {
    resultSet = statement.executeQuery(sql);
    return resultSet;
  }

  /**
   * 插入
   * @param sql
   * @return
   * @throws SQLException
   */
  public int insert(String sql) throws SQLException {
    return statement.executeUpdate(sql);
  }

  /**
   * 更新
   * @param sql
   * @return
   * @throws SQLException
   */
  public int update(String sql) throws SQLException {
    return statement.executeUpdate(sql);
  }

  /**
   * 删除
   * @param sql
   * @return
   * @throws SQLException
   */
  public int delete(String sql) throws SQLException {
    return statement.executeUpdate(sql);
  }


  /**
   * 关闭连接
   */
  public void close() {
    // 关闭对象，回收数据库资源
    //关闭结果集对象
    if (resultSet != null) {
      try {
        resultSet.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    // 关闭数据库操作对象
    if (statement != null) {
      try {
        statement.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    // 关闭数据库连接对象
    if (connection != null) {
      try {
        if (!connection.isClosed()) {
          connection.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }


}
