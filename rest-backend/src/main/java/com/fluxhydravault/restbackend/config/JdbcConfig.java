package com.fluxhydravault.restbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.fluxhydravault.restbackend")
public class JdbcConfig {
    @Bean
    public DataSource mariadbDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/tank_game");
        dataSource.setUsername("fluxhydravault");
        dataSource.setPassword("mQe9FciB82ihPN8o");

        return dataSource;
    }
}
