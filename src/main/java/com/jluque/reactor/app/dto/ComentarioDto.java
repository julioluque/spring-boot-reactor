package com.jluque.reactor.app.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class ComentarioDto {

	private List<String> comentarios;

	public ComentarioDto() {
		this.comentarios = new ArrayList<>();
	}

	public void agregarComentario(String comentario) {
		this.comentarios.add(comentario);
	}

}
