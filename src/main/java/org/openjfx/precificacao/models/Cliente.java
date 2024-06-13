package org.openjfx.precificacao.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Cliente {
	
	private int id;
	private String nome;
	private String cpf;
	private String telefone;
	private String email;
	
	@Override
	public String toString() {
	return (nome + " - " + cpf).toUpperCase();
	}

}
