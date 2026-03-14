package com.testdev.test_dev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestDevApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestDevApplication.class, args);
		String port = System.getProperty("server.port", "8081");
		System.out.println("Aplicacion cargada correctamente en el puerto " + port);
		System.out.println("Enlace: http://localhost:" + port + "/");

	}

}
