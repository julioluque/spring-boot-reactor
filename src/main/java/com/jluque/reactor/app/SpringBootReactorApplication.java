package com.jluque.reactor.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

@SuppressWarnings("unused")
@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(SpringBootReactorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

//		caseJust();
//		caseList();
//		caseFlatMap();
//		caseToString();
//		caseCollectList();
		
		String interop = "C:\\Users\\Julio\\Documents\\RLNK\\interop.txt";
		String pagos = "C:\\Users\\Julio\\Documents\\RLNK\\pagos.txt";
		String merge = "C:\\Users\\Julio\\Documents\\RLNK\\merge.txt";
//		interop = leerExtract(interop);
//		pagos = leerExtract(pagos);

		try {
			BufferedReader brPagos = new BufferedReader(new FileReader(pagos));
			BufferedReader brInterop = new BufferedReader(new FileReader(interop));
			BufferedWriter bwMerge = new BufferedWriter(new FileWriter(merge));
			
			String pagosText  = extractRead(brPagos);
			String interopText = extractRead(brInterop);

			mergeWrite(bwMerge, pagosText, interopText);
			bwMerge.flush();
			
		} catch (IOException e) {
			System.out.println("Error E/S: " + e);
		}

	}

	public static String extractRead(BufferedReader br) throws IOException {
//		String linea = br.readLine();
//		while (linea != null) {
//			System.out.println(linea);
//			linea = br.readLine();
//		}
		String texto = "";

		String temp = "";
		String bfRead;
		while ((bfRead = br.readLine()) != null) {
			temp = temp + bfRead + "\n";
		}

		texto = temp;
		return texto;

	}

	public static void mergeWrite(BufferedWriter bwMerge, String brPagos, String brInterop)
			throws IOException {
		bwMerge.write(brPagos);
		bwMerge.newLine();
		bwMerge.write(brInterop);
//		bwMerge.write("Esto es una prueba usando Buffered");
//		bwMerge.newLine();
//		bwMerge.write("Seguimos usando Buffered");
	}

	private String leerExtract(String direccion) {
		String texto = "";
		try {
			BufferedReader bf = new BufferedReader(new FileReader(direccion));
			String temp = "";
			String bfRead;
			while ((bfRead = bf.readLine()) != null) {
				temp = temp + bfRead + "\n";
			}

			texto = temp;

		} catch (IOException e) {
			System.out.println("No se encontro archivo");
			e.printStackTrace();
		}
		System.out.println(texto);
		return texto;
	}
	
	

	private void caseJust() {
		Flux<String> nombres = Flux.just("Julio Luque", "Alfredo Luque", "Pepe Argento", "Delfina Luque", "Nino Ticona",
				"Bruce Lee", "Bruce Willis", "Juan Mengando", "Juan Sultano");

		Flux<Usuario> usuarios = nombres
				.map(x -> new Usuario(x.split(" ")[0].toUpperCase(), x.split(" ")[1].toUpperCase()))
				.filter(x -> x.getApellido().equalsIgnoreCase("Luque")).doOnNext(x -> {
					if (x == null)
						throw new RuntimeException("Los nombres no pueden ser vacios.");
					System.out.println(x.getNombre().concat(" ").concat(x.getApellido()));
				}).map(x -> {
					String nombre = x.getNombre().toLowerCase();
					x.setNombre(nombre);
					return x;
				});

		usuarios.subscribe(e -> log.info(e.toString()), error -> log.error(error.getMessage()), new Runnable() {
			@Override
			public void run() {
				log.info("Finalizo la ejecucion del observable con exito!");
			}
		});
	}

	private void caseList() {

		List<String> usuarioList = new ArrayList<>();
		usuarioList.add("Julio Luque");
		usuarioList.add("Alfredo Luque");
		usuarioList.add("Pepe Argento");
		usuarioList.add("Delfina Luque");
		usuarioList.add("Nino Ticona");
		usuarioList.add("Bruce Lee");
		usuarioList.add("Bruce Willis");
		usuarioList.add("Juan Mengando");
		usuarioList.add("Juan Sultano");

		Flux<String> nombres = Flux.fromIterable(usuarioList);

		Flux<Usuario> usuarios = nombres
				.map(x -> new Usuario(x.split(" ")[0].toUpperCase(), x.split(" ")[1].toUpperCase()))
				.filter(x -> x.getApellido().equalsIgnoreCase("Luque")).doOnNext(x -> {
					if (x == null)
						throw new RuntimeException("Los nombres no pueden ser vacios.");
					System.out.println(x.getNombre().concat(" ").concat(x.getApellido()));
				}).map(x -> {
					String nombre = x.getNombre().toLowerCase();
					x.setNombre(nombre);
					return x;
				});

		usuarios.subscribe(e -> log.info(e.toString()), error -> log.error(error.getMessage()), new Runnable() {
			@Override
			public void run() {
				log.info("Finalizo la ejecucion del observable con exito!");
			}
		});

	}

	private void caseFlatMap() {

		List<String> usuarioList = new ArrayList<>();
		usuarioList.add("Julio Luque");
		usuarioList.add("Alfredo Luque");
		usuarioList.add("Pepe Argento");
		usuarioList.add("Delfina Luque");
		usuarioList.add("Nino Ticona");
		usuarioList.add("Bruce Lee");
		usuarioList.add("Bruce Willis");
		usuarioList.add("Juan Mengando");
		usuarioList.add("Juan Sultano");

		Flux.fromIterable(usuarioList)
				.map(x -> new Usuario(x.split(" ")[0].toUpperCase(), x.split(" ")[1].toUpperCase()))
				.flatMap(usuario -> {
					if (usuario.getApellido().equalsIgnoreCase("Luque"))
						return Mono.just(usuario);
					else
						return Mono.empty();
				}).map(x -> {
					String nombre = x.getNombre().toLowerCase();
					x.setNombre(nombre);
					return x;
				}).subscribe(u -> log.info(u.toString()));

	}

	private void caseToString() {

		List<Usuario> usuarioList = new ArrayList<>();
		usuarioList.add(new Usuario("Julio", "Luque"));
		usuarioList.add(new Usuario("Alfredo", "Luque"));
		usuarioList.add(new Usuario("Pepe", "Argento"));
		usuarioList.add(new Usuario("Delfina", "Luque"));
		usuarioList.add(new Usuario("Nino", "Ticona"));
		usuarioList.add(new Usuario("Bruce", "Lee"));
		usuarioList.add(new Usuario("Bruce", "Willis"));
		usuarioList.add(new Usuario("Juan", " Mengando"));
		usuarioList.add(new Usuario("Juan", "Sultano"));

		Flux.fromIterable(usuarioList)
				.map(x -> x.getNombre().toUpperCase().concat(" ").concat(x.getApellido().toUpperCase())).flatMap(x -> {
					if (x.contains("Luque".toUpperCase()))
						return Mono.just(x);
					else
						return Mono.empty();
				}).map(nombre -> nombre.toLowerCase()).subscribe(u -> log.info(u.toString()));

	}

	private void caseCollectList() {

		List<Usuario> usuarioList = new ArrayList<>();
		usuarioList.add(new Usuario("Julio", "Luque"));
		usuarioList.add(new Usuario("Alfredo", "Luque"));
		usuarioList.add(new Usuario("Pepe", "Argento"));
		usuarioList.add(new Usuario("Delfina", "Luque"));
		usuarioList.add(new Usuario("Nino", "Ticona"));
		usuarioList.add(new Usuario("Bruce", "Lee"));
		usuarioList.add(new Usuario("Bruce", "Willis"));
		usuarioList.add(new Usuario("Juan", " Mengando"));
		usuarioList.add(new Usuario("Juan", "Sultano"));

		Flux.fromIterable(usuarioList).collectList().subscribe(lista -> {
			lista.forEach(u -> log.info(u.toString()));
		});

	}
}
