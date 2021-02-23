package gitee.simonzhaojia.databaseoperation.jdbc;

import java.sql.*;

public class OperateDBWithPrepareStatement {

  private String url;
  private String driver;
  private String userName;
  private String userPassword;
  private Connection connection;
  private Statement statement;
  private ResultSet resultSet;


  public OperateDBWithPrepareStatement(String url, String driver, String userName, String userPassword) throws ClassNotFoundException, SQLException {
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

  public ResultSet queryAll() throws SQLException {
    PreparedStatement ps = connection.prepareStatement("SELECT * FROM team");
    return ps.executeQuery();
  }

  public ResultSet queryById(int teamId) throws SQLException {
    PreparedStatement ps = connection.prepareStatement("SELECT * FROM team WHERE team_id = ?");
    ps.setInt(1, teamId);
    return ps.executeQuery();
  }

  public int insert(int teamId, String teamName) throws SQLException {
    PreparedStatement ps = connection.prepareStatement("INSERT  INTO team VALUES (?, ?)");
    ps.setInt(1, teamId);
    ps.setString(2, teamName);
    return ps.executeUpdate();
  }

  public int updateById(String teamName, int teamId) throws SQLException {
    PreparedStatement ps = connection.prepareStatement("UPDATE team SET team_name = ? WHERE team_id = ?");
    ps.setString(1, teamName);
    ps.setInt(2, teamId);
    return ps.executeUpdate();
  }

  public int deleteById(int teamId) throws SQLException {
    PreparedStatement ps = connection.prepareStatement("DELETE FROM team WHERE team_id = ?");
    ps.setInt(1, teamId);
    return ps.executeUpdate();
  }

  public void startTransaction() throws SQLException {
    connection.setAutoCommit(false);
  }

  public void commitTransaction() throws SQLException {
    connection.commit();
    connection.setAutoCommit(true);
  }

  public void rollbackTransaction() throws SQLException {
    connection.rollback();
    connection.setAutoCommit(true);
  }


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
