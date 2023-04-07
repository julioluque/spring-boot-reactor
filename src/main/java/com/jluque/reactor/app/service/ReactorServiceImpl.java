package com.jluque.reactor.app.service;

import java.time.Duration;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jluque.reactor.app.dto.ComentarioDto;
import com.jluque.reactor.app.dto.PosteoDto;
import com.jluque.reactor.app.dto.UsuarioDto;
import com.jluque.reactor.app.mapper.ReactorMapper;

import lombok.NoArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@NoArgsConstructor
public class ReactorServiceImpl implements ReactorService {
	private static final Logger log = LoggerFactory.getLogger(ReactorServiceImpl.class);

	public void iterableMap() throws Exception {
		List<String> usuariosList = ReactorMapper.findUsers();

		Flux<String> nombres = Flux.fromIterable(usuariosList);

		Flux<UsuarioDto> usuarioDtoFlux = nombres
				.map(nombre -> new UsuarioDto(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
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

		usuarioDtoFlux.subscribe(e -> log.info("Subscribe usuarios: " + e.toString()),
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
				.map(nombre -> new UsuarioDto(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
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

		List<UsuarioDto> usuariosDtoList = ReactorMapper.findUserListDto();

		Flux.fromIterable(usuariosDtoList).collectList().subscribe(lista -> {
			lista.forEach(x -> log.info(x.toString()));
		});
	}

	public void iterableToStringMapping() throws Exception {
		List<UsuarioDto> usuariosDtoList = ReactorMapper.findUserListDto();

		Flux.fromIterable(usuariosDtoList).map(
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

	public void postCommentsFlatMap() {

		Mono<UsuarioDto> usuarioDtoMono = Mono.fromCallable(ReactorMapper::findUser);

		Mono<ComentarioDto> comentariosDtoMono = Mono.fromCallable(ReactorMapper::cargarComentarios);

		usuarioDtoMono.flatMap(u -> comentariosDtoMono.map(c -> new PosteoDto(u, c)))
				.subscribe(uc -> log.info(uc.toString()));
	}

	public void postCommentsZipWith() {

		Mono<UsuarioDto> usuarioDtoMono = Mono.fromCallable(ReactorMapper::findUser);

		Mono<ComentarioDto> comentarioDtoMono = Mono.fromCallable(ReactorMapper::cargarComentarios);

		Mono<PosteoDto> posteoDtoMono = usuarioDtoMono.zipWith(comentarioDtoMono).map(t -> {
			UsuarioDto u = t.getT1();
			ComentarioDto c = t.getT2();
			return new PosteoDto(u, c);
		});

		posteoDtoMono.subscribe(uc -> log.info(uc.toString()));
	}

	public void postCommentsZipWithBifunction() {

		Mono<UsuarioDto> usuarioDtoMono = Mono.fromCallable(ReactorMapper::findUser);

		Mono<ComentarioDto> comentarioDtoMono = Mono.fromCallable(ReactorMapper::cargarComentarios);

		Mono<PosteoDto> posteoDtoMono = usuarioDtoMono.zipWith(comentarioDtoMono, (u, c) -> new PosteoDto(u, c));

		posteoDtoMono.subscribe(uc -> log.info(uc.toString()));
	}

	public void range() {
		Flux.just(1, 2, 3, 4).map(x -> (x * 2))
				.zipWith(Flux.range(0, 4), (a, b) -> String.format("PrimerFlux: %d - SegundoFlux: %d", a, b))
				.subscribe(log::info);
	}

	public void interval() {
		Flux<Integer> range = Flux.range(1, 12);
		Flux<Long> delay = Flux.interval(Duration.ofSeconds(1));
		range.zipWith(delay, (r, d) -> r).doOnNext(i -> log.info(i.toString())).subscribe();
	}

	public void delayElement() {
		Flux<Integer> range = Flux.range(1, 12).delayElements(Duration.ofSeconds(1))
				.doOnNext(e -> log.info(e.toString()));
		range.blockLast();
	}

	public void infiniteInterval() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);

		Flux.interval(Duration.ofSeconds(1)).doOnTerminate(latch::countDown).flatMap(i -> {
			if (i >= 5) {
				return Flux.error(new InterruptedException("Solo hasta 5"));
			}
			return Flux.just(i);
		}).map(i -> "hola " + i).retry(2).subscribe(log::info, e -> log.error(e.getMessage()));

		latch.await();
	}

	public void infiniteIntervalFromCreate() {
		Flux.create(emitter -> {
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				private Integer contador = 0;

				@Override
				public void run() {
					emitter.next(++contador);
					if (contador == 10) {
						timer.cancel();
						emitter.complete();
					}
					if (contador == 5) {
						timer.cancel();
						emitter.error(new InterruptedException("Error en contador 5!"));
					}
				}
			}, 1000, 500);
		}).subscribe(e -> log.info(e.toString()), e -> log.info(e.getMessage()), () -> log.info("Done"));
	}

	public void backPresureSimple() {
		Flux.range(1, 10).log().subscribe(x -> log.info(x.toString()));
	}

}
