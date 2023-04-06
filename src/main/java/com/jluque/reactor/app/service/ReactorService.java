package com.jluque.reactor.app.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jluque.reactor.app.dto.Usuario;
import com.jluque.reactor.app.mapper.ReactorMapper;

import lombok.NoArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@NoArgsConstructor
public class ReactorService {

	private static final Logger log = LoggerFactory.getLogger(ReactorService.class);

	public void iterableMap() throws Exception {
		List<String> usuariosList = ReactorMapper.findUsers();

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

	public void iterableFlatMap() throws Exception {

		List<String> usuariosList = ReactorMapper.findUsers();

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

	public void usersfluxToMonoMapping() throws Exception {
		log.info(new Object() {
		}.getClass().getEnclosingMethod().getName());

		List<Usuario> usuariosList = ReactorMapper.findUserListDto();

		Flux.fromIterable(usuariosList).collectList().subscribe(lista -> {
			lista.forEach(x -> log.info(x.toString()));
		});
	}

	public void iterableToStringMapping() throws Exception {
		List<Usuario> usuariosList = ReactorMapper.findUserListDto();

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
}
