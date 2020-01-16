package nl.dehaagsehogeschool.thechallenge;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertIntoSQL {

    /**
     * Connect to the vb1.db database
     *
     * @return the Connection object
     */
    private Connection connect() {
        Connection conn = null;
        String driver = "com.mysql.cj.jdbc.Driver";
        // MySQL connection string, pas zonodig het pad aan:
        String connection = "jdbc:mysql://localhost:3306/chalenge_hhs";
        String user = "microbit";
        String password = "geheim";
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(connection, user, password);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return conn;
    }


//    public void insert(String tijdstip, float temperatuur) {
            public void insert(String naam){
//        String sql = "INSERT INTO tbl1(tijdstip, temperatuur) VALUES(?,?)";
        String sql = "INSERT INTO test(naam) VALUES (?)";
        try {
            PreparedStatement preparedStatement = connect().prepareStatement(sql);
            preparedStatement.setString(1, naam);
//            preparedStatement.setString(1, tijdstip);
//            preparedStatement.setFloat(2, temperatuur);
            preparedStatement.executeUpdate();
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void update(String naam, float afstand){

             String sql = "UPDATE device SET percentage_full = ? WHERE naam = ?";

            try {
                PreparedStatement preparedStatement = connect().prepareStatement(sql);
//                preparedStatement.setString(1, naam);
    //            preparedStatement.setString(1, tijdstip);
    //            preparedStatement.setFloat(2, temperatuur);
                preparedStatement.setFloat(1, afstand);
                preparedStatement.setString(2, naam);

                preparedStatement.executeUpdate();
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
            }

    }
}
