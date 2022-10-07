package ru.vadim.game.config;

import io.netty.util.internal.StringUtil;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.connectionfactory.R2dbcTransactionManager;
import org.springframework.data.r2dbc.connectionfactory.init.CompositeDatabasePopulator;
import org.springframework.data.r2dbc.connectionfactory.init.ConnectionFactoryInitializer;
import org.springframework.data.r2dbc.connectionfactory.init.ResourceDatabasePopulator;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.transaction.ReactiveTransactionManager;
import reactor.core.publisher.Flux;

import java.util.List;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Configuration
@EnableR2dbcRepositories
public class R2DBCConfiguration {

    @Bean
    public ConnectionFactory connectionFactory(R2DBCConfigurationProperties properties) {
//        ConnectionFactoryOptions baseOptions = ConnectionFactoryOptions.parse(properties.getUrl());
//        ConnectionFactoryOptions.Builder ob = ConnectionFactoryOptions.builder().from(baseOptions);
//        if (!StringUtil.isNullOrEmpty(properties.getUser())) {
//            ob = ob.option(USER, properties.getUser());
//        }
//        if (!StringUtil.isNullOrEmpty(properties.getPassword())) {
//            ob = ob.option(PASSWORD, properties.getPassword());
//        }
        ConnectionFactoryOptions options = builder()
                .option(DRIVER, "h2")
//                .option(PROTOCOL, "mem")  // file, mem
//                .option(HOST, "localhost")
//                .option(PORT, 8082)
                .option(USER, "sa")
                .option(PASSWORD, "q")
                .option(DATABASE, "r2dbc:h2:mem:///testdb")
                .build();
        return ConnectionFactories.get(options);
        //return ConnectionFactories.get("r2dbc:h2:mem:///test?options=DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
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
                                                        "name varchar(80) not null," +
                                                        "gold integer not null)")
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
//
////    @Bean
////    public ConnectionFactory connectionFactory() {
////        return H2ConnectionFactory.inMemory("test");
////    }
////
////
//    @Bean
//    DatabaseClient databaseClient(ConnectionFactory connectionFactory) {
//        return DatabaseClient.create(connectionFactory);
//    }
//    @Bean
//    ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
//        return new R2dbcTransactionManager(connectionFactory);
//    }
}
