package com.example.classroom_db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Autowired;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

@SpringBootApplication
public class ClassroomDbApplication implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(ClassroomDbApplication.class);

	@Autowired
	private DataSource dataSource;

	public static void main(String[] args) {
		SpringApplication.run(ClassroomDbApplication.class, args);
	}

	@Override
	public void run(String... args) {
		try (Connection conn = dataSource.getConnection()) {
			DatabaseMetaData metaData = conn.getMetaData();
			logger.info("✅ Успешное подключение к PostgreSQL!");
			logger.info("URL: {}", metaData.getURL());
			logger.info("User: {}", metaData.getUserName());
			logger.info("Database: {}", conn.getCatalog());
			logger.info("PostgreSQL version: {}", metaData.getDatabaseProductVersion());
		} catch (Exception e) {
			logger.error("❌ Ошибка подключения к PostgreSQL", e);
		}
	}

}