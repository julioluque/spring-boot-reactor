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
import reactor.core.publisher.Mono;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(SpringBootReactorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		List<String> usuarios = findUsers();

//		iterableMap(usuarios);
//		iterableFlatMap(usuarios);

		List<Usuario> usuariosList = findUserDto();
//		iterableToStringMapping(usuariosList);

		usersfluxToMonoMapping(usuariosList);
	}

	public List<String> findUsers() throws Exception {
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
		return usuariosList;
	}

	public List<Usuario> findUserDto() throws Exception {
		List<Usuario> usuariosList = new ArrayList<>();
		usuariosList.add(new Usuario("Andres", "Fulano"));
		usuariosList.add(new Usuario("Pedro", "Fulano"));
		usuariosList.add(new Usuario("Maria", "Mengano"));
		usuariosList.add(new Usuario("Diego", "Maradona"));
		usuariosList.add(new Usuario("Juan", "perez"));
		usuariosList.add(new Usuario("Raul", "Sultano"));
		usuariosList.add(new Usuario("Manuel", "Mengano"));
		usuariosList.add(new Usuario("Bruce", "Lee"));
		usuariosList.add(new Usuario("Bruce", "Willis"));
		return usuariosList;
	}

	public void usersfluxToMonoMapping(List<Usuario> userList) throws Exception {
		log.info(new Object() {}.getClass().getEnclosingMethod().getName());
		Flux.fromIterable(userList)
				.collectList()
				.subscribe(lista -> {
						lista.forEach(x -> log.info(x.toString()));
					});
	}

	public void iterableToStringMapping(List<Usuario> usuariosList) throws Exception {
		Flux.fromIterable(usuariosList).map(
				usuario -> usuario.getNombre().toUpperCase().concat(" ").concat(usuario.getApellido().toUpperCase()))
				.flatMap(nombre -> {
					if (nombre.contains("Bruce".toUpperCase())) {
						return Mono.just(nombre);
					} else {
						return Mono.empty();
					}
				}).map(nombre -> nombre.toLowerCase())
				.subscribe(u -> log.info("Subscribe: {}", u), error -> log.error(error.getMessage()));
	}

	public void iterableFlatMap(List<String> usuariosList) throws Exception {
		Flux.fromIterable(usuariosList)
				.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
				.flatMap(usuario -> {
					if (usuario.getNombre().equalsIgnoreCase("bruce")) {
						return Mono.just(usuario);
					} else {
						return Mono.empty();
					}
				}).map(usuario -> {
					usuario.setNombre(usuario.getNombre().toLowerCase());
					return usuario;
				}).subscribe(u -> log.info("Subscribe: {}", u), error -> log.error(error.getMessage()));
	}

	public void iterableMap(List<String> usuariosList) throws Exception {

		Flux<String> nombres = Flux.fromIterable(usuariosList);

		Flux<Usuario> usuariosFlux = nombres
				.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
				.filter(n -> n.getNombre().equalsIgnoreCase("Bruce")).doOnNext(usuario -> {
					if (usuario == null)
						throw new RuntimeException("Nombres no pueden ser vacios");
				}).map(usuario -> {
					usuario.setNombre(usuario.getNombre().toLowerCase());
					return usuario;
				}).doOnNext(e -> log.info(e.getNombre().concat(", ").concat(e.getApellido())));

		nombres.subscribe(e -> log.info("Subscribe nombres: " + e.toString()), error -> log.error(error.getMessage()),
				new Runnable() {
					@Override
					public void run() {
						log.info("Finalizo la ejecucion del observable");
					}
				});

		usuariosFlux.subscribe(e -> log.info("Subscribe usuarios: " + e.toString()),
				error -> log.error(error.getMessage()), new Runnable() {
					@Override
					public void run() {
						log.info("Finalizo la ejecucion del observable");
					}
				});

	}
}
