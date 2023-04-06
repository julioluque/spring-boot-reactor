package com.jluque.reactor.app;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jluque.reactor.app.dto.Usuario;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(SpringBootReactorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		iterableMap();
	}

	
	public void iterableFlatMap() throws Exception {

		List<String> usuariosList = new ArrayList<>();
		usuariosList.add("Andres Fulano");
		usuariosList.add("Pedro Fulano");
		usuariosList.add("Maria Mengano");
		usuariosList.add("Diego Maradona");
		usuariosList.add("Juan perez");
		usuariosList.add("Raul Sultano");
		usuariosList.add("Manuel Mengano");
		usuariosList.add("Bruce Lee");
		usuariosList.add("Bruce Willis");

		Flux.fromIterable(usuariosList)
				.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
				.filter(n -> n.getNombre().equalsIgnoreCase("Bruce")).doOnNext(usuario -> {
					if (usuario == null)
						throw new RuntimeException("Nombres no pueden ser vacios");
				})
				.doOnNext(e -> log.info(e.getNombre().concat(", ").concat(e.getApellido())))
				.subscribe(
					e -> log.info("Subscribe nombres: " + e.toString()), 
					error -> log.error(error.getMessage()),
					new Runnable() {
						@Override
						public void run() {
							log.info("Finalizo la ejecucion del observable");
						}
					});

	}
	
	public void iterableMap() throws Exception {
		
		List<String> usuariosList = new ArrayList<>();
		usuariosList.add("Andres Fulano");
		usuariosList.add("Pedro Fulano");
		usuariosList.add("Maria Mengano");
		usuariosList.add("Diego Maradona");
		usuariosList.add("Juan perez");
		usuariosList.add("Raul Sultano");
		usuariosList.add("Manuel Mengano");
		usuariosList.add("Bruce Lee");
		usuariosList.add("Bruce Willis");
		
		Flux<String> nombres = Flux.fromIterable(usuariosList);
		
		Flux<Usuario> usuariosFlux = nombres
				.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
				.filter(n -> n.getNombre().equalsIgnoreCase("Bruce")).doOnNext(usuario -> {
					if (usuario == null)
						throw new RuntimeException("Nombres no pueden ser vacios");
				})
				.map(usuario -> {
					usuario.setNombre(usuario.getNombre().toLowerCase());
					return usuario;
				})
				.doOnNext(e -> log.info(e.getNombre().concat(", ").concat(e.getApellido())));
		
		nombres.subscribe(
				e -> log.info("Subscribe nombres: " + e.toString()), 
				error -> log.error(error.getMessage()),
				new Runnable() {
					@Override
					public void run() {
						log.info("Finalizo la ejecucion del observable");
					}
				});
		
		usuariosFlux.subscribe(
				e -> log.info("Subscribe usuarios: " + e.toString()),
				error -> log.error(error.getMessage()), 
				new Runnable() {
					@Override
					public void run() {
						log.info("Finalizo la ejecucion del observable");
					}
				});
		
	}
}
