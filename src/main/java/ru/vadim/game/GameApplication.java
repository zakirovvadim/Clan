package ru.vadim.game;

import org.h2.tools.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.sql.SQLException;

@SpringBootApplication
@EnableConfigurationProperties
public class GameApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameApplication.class, args);
        try {
            Server server = Server.createTcpServer().start();
            System.out.println("Server started and connection is open.");
            System.out.println("URL: jdbc:h2:" + server.getURL() + "/mem:test");
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        }
    }

}
