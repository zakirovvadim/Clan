package ru.vadim.game.config;

//import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
//import io.r2dbc.postgresql.PostgresqlConnectionFactory;
//import io.r2dbc.postgresql.codec.EnumCodec;

import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.reactivestreams.Publisher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;
import ru.vadim.game.model.Clan;

import java.util.HashMap;
import java.util.Map;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Configuration
public class R2DBCConfiguration {

    @Bean
    public ConnectionFactory connectionFactory() {
        ConnectionFactoryOptions conFac = builder()
                .option(DRIVER, "h2")
                .option(PROTOCOL, "mem")  // file, mem
                .option(HOST, "8082")
                .option(USER, "sa")
                .option(PASSWORD, "1")
                .option(DATABASE, "r2dbc:h2:mem:///testdb")
                .build();
        ConnectionFactory connectionFactory = ConnectionFactories.get(conFac);
        return connectionFactory;
//        return new PostgresqlConnectionFactory(
//                PostgresqlConnectionConfiguration.builder()
//                        .host("localhost")
//                        .database("postgres")
//                        .username("postgres")
//                        .password("password")
//                        .build()
//        );
    }
    @Bean
    public CommandLineRunner initDatabase(ConnectionFactory cf) {
        return (args) ->
                Flux.from(cf.create())
                        .flatMap(c ->
                                Flux.from(c.createBatch()
                                                .add("drop table if exists Clan")
                                                .add("create table Clan(" +
                                                        "id IDENTITY(1,1)," +
                                                        "name varchar(80)," +
                                                        "gold integer)")
                                                .add("insert into Clan(name,gold)" +
                                                        "values(1,100)")
                                                .add("insert into Clan(name,gold)" +
                                                        "values(2,250)")
                                                .execute())
                                        .doFinally((st) -> c.close())
                        )
                        .log()
                        .blockLast();
    }

    @Bean
    public CommandLineRunner initDatabaseHist(ConnectionFactory cf) {
        return (args) ->
                Flux.from(cf.create())
                        .flatMap(c ->
                                Flux.from(c.createBatch()
                                                .add("drop table if exists History")
                                                .add("create table History(" +
                                                        "id IDENTITY(1,1)," +
                                                        "name varchar(80)," +
                                                        "mns boolean," +
                                                        "pls boolean," +
                                                        "count integer," +
                                                        "date date," +
                                                        "gold integer)")
                                                .execute())
                                        .doFinally((st) -> c.close())
                        )
                        .log()
                        .blockLast();
    }

    @Bean
    public Publisher<? extends Connection> connection(ConnectionFactory connectionFactory) {
        return connectionFactory.create();
    }

    @Bean
    DatabaseClient databaseClient(ConnectionFactory connectionFactory) {
        return DatabaseClient.builder()
                .connectionFactory(connectionFactory)
                //.bindMarkers(() -> BindMarkersFactory.named(":", "", 20).create())
                .namedParameters(true)
                .build();
    }
}
