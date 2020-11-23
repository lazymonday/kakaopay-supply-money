package com.kakaopay.supplymoney.config;

import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.Random;

@Configuration
public class KPayConfig {

    @Bean
    Random getRandom() {
        return new Random(OffsetDateTime.now().toInstant().toEpochMilli());
    }

    @Bean
    public Server h2TCPServer() throws SQLException {
        return Server.createTcpServer().start();
    }
}
