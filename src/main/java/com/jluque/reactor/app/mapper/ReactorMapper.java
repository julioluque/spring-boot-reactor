package com.jluque.reactor.app.mapper;

import java.util.ArrayList;
import java.util.List;

import com.jluque.reactor.app.dto.Usuario;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ReactorMapper {

	public static List<String> findUsers() throws Exception {
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

	public static List<Usuario> findUserListDto() throws Exception {
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
}
