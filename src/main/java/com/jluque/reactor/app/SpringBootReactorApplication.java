package com.jluque.reactor.app;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jluque.reactor.app.dto.Comentarios;
import com.jluque.reactor.app.dto.Usuario;
import com.jluque.reactor.app.dto.UsuarioComentarios;
import com.jluque.reactor.app.service.ReactorService;

import reactor.core.publisher.Mono;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(SpringBootReactorApplication.class);

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
		postComments();

	}

	public Comentarios cargarComentarios() {
		Comentarios comentarios = new Comentarios();
		comentarios.agregarComentario("primer comentario");
		comentarios.agregarComentario("segundo comentario");
		comentarios.agregarComentario("post 3");
		comentarios.agregarComentario("posteo 4");
		comentarios.agregarComentario("publicacion 5");
		comentarios.agregarComentario("sexto comentario");
		comentarios.agregarComentario("siete!");

		return comentarios;
	}

	public void postComments() {

		Mono<Usuario> usuarioMono = Mono.fromCallable(() -> new Usuario("Julio", "Luque"));

		Mono<Comentarios> comentariosMono = Mono.fromCallable(() -> cargarComentarios());

		usuarioMono.flatMap(u -> comentariosMono.map(c -> new UsuarioComentarios(u, c)))
				.subscribe(uc -> log.info(uc.toString()));
	}

}
