package org.openjfx.precificacao.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Cliente {
	
	private int id;
	private String codCliente;
	private String nome;
	private String cpf;
	private String telefone;
	private String email;
	private String endereco;
	private String bairro;
	private String estado;
	private String cidade;
	private String cep;
	private String uuid;
	private String perfil;

	
	@Override
	public String toString() {
	return ("Cliente nº " + codCliente + "-" + nome).toUpperCase();
	}

}
