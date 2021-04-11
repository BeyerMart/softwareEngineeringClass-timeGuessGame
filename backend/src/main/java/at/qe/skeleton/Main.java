package at.qe.skeleton;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import java.sql.*;
/**
 * Spring boot application. Execute maven with <code>mvn spring-boot:run</code>
 * to start this web application.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@SpringBootApplication
/*
 * Prevent spring from trying to autowire the websocket-infrastructure: Exclude
 * the at.qe.skeleton.ui.websockets package from component scan.
 *
 * NOTE: Do not add any components to this package which should be managed by
 * spring. It is reserved for the CDI-injection-mechanisms (Weld). Only add
 * CDI-managed components.
 */
public class Main extends SpringBootServletInitializer {
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/db_g6t3?useLegacyDatetimeCode=false&serverTimezone=UTC";
        String user = "root", pass = "passwd12";

        try {
            Connection myConn = DriverManager.getConnection(url, user, pass);
            Statement myStat = myConn.createStatement();
            String sql = "select * from Users";
            ResultSet rs = myStat.executeQuery(sql);

            while (rs.next()) {
                System.out.println(rs.getString("firstName"));

            }

        }
        catch (SQLException e) { e.printStackTrace(); }
        SpringApplication.run(Main.class, args);

        SpringApplication.run(Main.class, args);
    }
}
