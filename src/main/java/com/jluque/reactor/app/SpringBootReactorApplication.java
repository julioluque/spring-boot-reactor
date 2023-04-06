package com.jluque.reactor.app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jluque.reactor.app.service.ReactorService;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {


	private ReactorService servcie = new ReactorService();

	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

//		servcie.iterableMap();

//		servcie.iterableFlatMap();
//
//		servcie.iterableToStringMapping();

//		servcie.usersfluxToMonoMapping();

//		servcie.postCommentsFlatMap();

		servcie.postCommentsZipWith();

	}

}
