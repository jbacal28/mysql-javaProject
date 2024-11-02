package projects.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import projects.entity.Project;
import projects.exception.DbException;
import provided.util.DaoBase;

public class ProjectDao extends DaoBase {
  private static final String CATEGORY_TABLE = "category";
  private static final String MATERIAL_TABLE = "material";
  private static final String PROJECT_TABLE = "project";
  private static final String PROJECT_CATEGORY_TABLE = "project_category";
  private static final String STEP_TABLE = "step";

  public Project insertProject(Project project) {
  
    String sql = ""
        + "INSERT INTO " + PROJECT_TABLE + " "
        + "(project_name, estimated_hours, actual_hours, difficulty, notes) "
        + "VALUES "
        + "(?, ?, ?, ?, ?)";
    
    try(Connection connection = DbConnection.getConnection()) {
      startTransaction(connection);
   
    try(PreparedStatement statement = connection.prepareStatement(sql)) {
      setParameter (statement, 1, project.getProjectName(), String.class);
      setParameter (statement, 2, project.getEstimatedHours(), BigDecimal.class);
      setParameter (statement, 3, project.getActualHours(), BigDecimal.class);
      setParameter (statement, 4, project.getDifficulty(), Integer.class);
      setParameter (statement, 5, project.getNotes(), String.class);
      
      statement.executeUpdate();
      
      Integer projectId = getLastInsertId(connection, PROJECT_TABLE);
      commitTransaction(connection);
      
      project.setProjectId(projectId);
      return project;
    }
    catch(Exception e) {
      rollbackTransaction(connection);
      throw new DbException(e);
    }
  }
  catch(SQLException e) {
    throw new DbException (e);
    
    }
  }
}
