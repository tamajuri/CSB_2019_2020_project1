
package sec.project.inSecureDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sec.project.config.DataSourceConfig;
import sec.project.domain.Signup;

@Service
public class InSecureDto {
    
    @Autowired
    private DataSourceConfig config;
    
    public void init() throws SQLException {
        Connection c = config.getDataSource().getConnection();
        c.prepareStatement("create table Signups (name varchar(80), address varchar(80));").executeUpdate();
    }
    
    public void lisaaSignup(Signup signup) throws SQLException {
        String sql = "INSERT INTO Signups" +
        " (name, address) VALUES" +
        " ('" + signup.getName() + "', '" + signup.getAddress() + "');";
        Connection c = config.getDataSource().getConnection();
        PreparedStatement preparedStatement = c.prepareStatement(sql);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        c.close();
    }
    
    public List<Signup> allSignups() throws SQLException {
        // UNSAFE !!! DON'T DO THIS !!!
        String sql = "select "
                + "name, address "
                + "from Signups;";
        Connection c = config.getDataSource().getConnection();
        ResultSet rs = c.createStatement().executeQuery(sql);
        List<Signup> signups = new ArrayList<>();
        while (rs.next()) {
            signups.add(new Signup(rs.getString("name"), rs.getString("address")));
        }
        rs.close();
        c.close();
        return signups;
        // ...
    }
    
    public List<Signup> unsafeHaeSignup(String name) throws SQLException {
        // UNSAFE !!! DON'T DO THIS !!!
        String sql = "select "
                + "name, address "
                + "from Signups where name = '"
                + name
                + "'";
        Connection c = config.getDataSource().getConnection();
        ResultSet rs = c.createStatement().executeQuery(sql);
        List<Signup> signups = new ArrayList<>();
        while (rs.next()) {
            signups.add(new Signup(rs.getString("name"), rs.getString("address")));
        }
        rs.close();
        c.close();
        return signups;
        // ...
    }

}
