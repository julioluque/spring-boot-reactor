package com.jluque.reactor.app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jluque.reactor.app.service.ReactorService;
import com.jluque.reactor.app.service.ReactorServiceImpl;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {

	private ReactorService service = new ReactorServiceImpl();

	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		service.backPresureLimitRate();

	}

}
