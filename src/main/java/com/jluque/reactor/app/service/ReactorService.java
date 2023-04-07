package com.jluque.reactor.app.service;

import java.time.Duration;
import java.util.List;
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
public class ReactorService {

	private static final Logger log = LoggerFactory.getLogger(ReactorService.class);

	/**
	 * Example Map. carga lista de nombres los separa por nombre y apellido, filtra
	 * y cambia mayusculas/minusculas.
	 * 
	 * Subscribe y muestra resutaldos
	 * 
	 * @throws Exception
	 */
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

	/**
	 * example FlatMap. replcia del item iterableMap, pero usando flatMap, aplano
	 * las listas y las filtro en el mismo flujo.
	 * 
	 * Al final subscribe y muestra resultados.
	 * 
	 * @throws Exception
	 */
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

	/**
	 * Cambia objetos de Flux a Mono usando collectList
	 * 
	 * Subscribe y muestra resultadosO
	 * 
	 * @throws Exception
	 */
	public void usersfluxToMonoMapping() throws Exception {

		List<UsuarioDto> usuariosDtoList = ReactorMapper.findUserListDto();

		Flux.fromIterable(usuariosDtoList).collectList().subscribe(lista -> {
			lista.forEach(x -> log.info(x.toString()));
		});
	}

	/**
	 * Convierte un obejto Dto en una lista de String.
	 * 
	 * Subcribe y muestra resultados.
	 */
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

	/**
	 * Lanzamos posteos de comentarios uniendo Usuarios y Comentarios usando
	 * flatMap.
	 * 
	 * Subscribe e imprime resultados
	 */
	public void postCommentsFlatMap() {

		Mono<UsuarioDto> usuarioDtoMono = Mono.fromCallable(ReactorMapper::findUser);

		Mono<ComentarioDto> comentariosDtoMono = Mono.fromCallable(ReactorMapper::cargarComentarios);

		usuarioDtoMono.flatMap(u -> comentariosDtoMono.map(c -> new PosteoDto(u, c)))
				.subscribe(uc -> log.info(uc.toString()));
	}

	/**
	 * Misma situacion que postCommentsFlatMap pero usando zipWith Single parameter
	 * para relacionar Usuarios y Comentarios en un dto que se llama posteDto
	 * 
	 * Subscribe e imprime resultados
	 */
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

	/**
	 * Misma situacion que postCommentsFlatMap pero usando zipWith usando bifunciton
	 * con dos parametros para relacionar Usuarios y Comentarios en un dto que se
	 * llama posteDto
	 * 
	 * Subscribe e imprime resultados
	 */
	public void postCommentsZipWithBifunction() {

		Mono<UsuarioDto> usuarioDtoMono = Mono.fromCallable(ReactorMapper::findUser);

		Mono<ComentarioDto> comentarioDtoMono = Mono.fromCallable(ReactorMapper::cargarComentarios);

		Mono<PosteoDto> posteoDtoMono = usuarioDtoMono.zipWith(comentarioDtoMono, (u, c) -> new PosteoDto(u, c));

		posteoDtoMono.subscribe(uc -> log.info(uc.toString()));
	}

	/**
	 * Usa rango para decidir cantidad a imprimir. Combina dos flux usando zipWith.
	 * 
	 * @Flux1: lista de enteros y procesados.
	 * @Flux2: usa un rango de dicha lista.
	 * 
	 * @Subscribe: lista general
	 */
	public void range() {
		Flux.just(1, 2, 3, 4).map(x -> (x * 2))
				.zipWith(Flux.range(0, 4), (a, b) -> String.format("PrimerFlux: %d - SegundoFlux: %d", a, b))
				.subscribe(log::info);
	}

	/**
	 * Usa un intervalo de Flux para frenar segun el rango para decidir cantidad a
	 * imprimir. Combina dos flux usando zipWith.
	 * 
	 * @Flux1: un rango de conteo.
	 * @Flux2: es un delay para cada elemento del rango.
	 * 
	 * @Subscribe: lista general
	 */
	public void interval() {
		Flux<Integer> range = Flux.range(1, 12);
		Flux<Long> delay = Flux.interval(Duration.ofSeconds(1));
		range.zipWith(delay, (r, d) -> r).doOnNext(i -> log.info(i.toString())).subscribe();
	}

	/**
	 * Misma situacion que @interval() Usa un intervalo con delayElements de Flux
	 * para frenar segun el rango para decidir cantidad a imprimir.
	 * 
	 * @Flux: un rango de conteo. Agrega delay
	 * 
	 * @Subscribe: NoBoqueante, hilos corriendo en background. Interacalamos
	 *             con @BlockLast.
	 * @BlockLast: Bloqueante. No recomendado ya que genera cuellos de botella.
	 *             hilos corriendo en rpimer plano. Intercala con @Subscribe
	 */
	public void delayElement() {
		Flux<Integer> range = Flux.range(1, 12).delayElements(Duration.ofSeconds(1))
				.doOnNext(e -> log.info(e.toString()));
		range.blockLast();
	}

	public void infiniteInterval() throws InterruptedException {
		System.out.println("infiniteInterval();");
		CountDownLatch latch = new CountDownLatch(1);

		Flux.interval(Duration.ofSeconds(1)).doOnTerminate(latch::countDown).flatMap(i -> {
			if (i >= 5) {
				return Flux.error(new InterruptedException("Solo hasta 5"));
			}
			return Flux.just(i);
		}).map(i -> "hola " + i).retry(2).subscribe(log::info, e -> log.error(e.getMessage()));

		latch.await();
	}

}
