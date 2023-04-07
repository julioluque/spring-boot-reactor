package com.jluque.reactor.app.mapper;

import java.util.ArrayList;
import java.util.List;

import com.jluque.reactor.app.dto.ComentarioDto;
import com.jluque.reactor.app.dto.UsuarioDto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ReactorMapper {

	public static UsuarioDto findUser() {
		return new UsuarioDto("Andres", "Raffo");
	}

	public static List<String> findUsers() {
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

	public static List<UsuarioDto> findUserListDto() {
		List<UsuarioDto> usuariosDtoList = new ArrayList<>();
		usuariosDtoList.add(new UsuarioDto("Andres", "Fulano"));
		usuariosDtoList.add(new UsuarioDto("Pedro", "Fulano"));
		usuariosDtoList.add(new UsuarioDto("Maria", "Mengano"));
		usuariosDtoList.add(new UsuarioDto("Diego", "Maradona"));
		usuariosDtoList.add(new UsuarioDto("Juan", "perez"));
		usuariosDtoList.add(new UsuarioDto("Raul", "Sultano"));
		usuariosDtoList.add(new UsuarioDto("Manuel", "Mengano"));
		usuariosDtoList.add(new UsuarioDto("Bruce", "Lee"));
		usuariosDtoList.add(new UsuarioDto("Bruce", "Willis"));
		return usuariosDtoList;
	}

	public static ComentarioDto cargarComentarios() {
		ComentarioDto comentarioDtoList = new ComentarioDto();
		comentarioDtoList.agregarComentario("primer comentario");
		comentarioDtoList.agregarComentario("segundo comentario");
		comentarioDtoList.agregarComentario("post 3");
		comentarioDtoList.agregarComentario("posteo 4");
		comentarioDtoList.agregarComentario("publicacion 5");
		comentarioDtoList.agregarComentario("sexto comentario");
		comentarioDtoList.agregarComentario("siete!");
		return comentarioDtoList;
	}

}
