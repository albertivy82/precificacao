package org.openjfx.precificacao.controller;

import com.sun.javafx.binding.StringFormatter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import org.openjfx.precificacao.App;
import org.openjfx.precificacao.models.Projeto;
import org.openjfx.precificacao.service.ClienteService;
import org.openjfx.precificacao.service.CustosFixosService;
import org.openjfx.precificacao.service.CustosService;
import org.openjfx.precificacao.service.ProjetoService;
import org.openjfx.precificacao.shared.ProjetoSingleton;

import java.sql.SQLException;

public class PrecificacaoController {

	Projeto projeto = ProjetoSingleton.getInstance().getProjeto();

	private ClienteService clienteService;
	private CustosFixosService custosFixosRaiz;
	private double descontoCalculado = 0;
	private ProjetoService projetoService;
	private CustosService custosVariaveisService;


	@FXML
	private Label nomeProjetoLabel;

	@FXML
	private Label clienteLabel;

	@FXML
	private Label StatusLabel;

	@FXML
	private Label custosFixos;

	@FXML
	private Label valorFinanceiroLabel;

	@FXML
	private Label valorPercentualLabel;

	@FXML
	private Slider sliderCustosFixos;

	@FXML
	private Label totalProjetoLabel;

	@FXML
	private Label totalCustosVariaveisLabel;

	@FXML
	private Label totalDeLancamentosDeCF;

	@FXML
	private Label situacaoDeCustos;


	@FXML
	private void btnDetalhamentoProjeto(ActionEvent e) {
		App.mudarTela("DetalhamentoProjeto");
	}



	@FXML
	void initialize() throws SQLException {
		this.projetoService = new ProjetoService();
		this.custosVariaveisService = new CustosService();
		labelvalorDoProjeto();
		lalabelTotalCustosVariaveis();
		this.custosFixosRaiz = new CustosFixosService();
		identificacaoProjeto();
		slider();
		saldoAtualDeCusto();
		siuacaoDeCustos();
	}

	private void identificacaoProjeto(){

		nomeProjetoLabel.setText("Nome do Projeto: " + projeto.getNomeProjeto());
		this.clienteService = new ClienteService();
		clienteLabel.setText("Cliente: " + this.clienteService.nomeCliente(projeto.getIdCliente()));
		StatusLabel.setText("Status: " + projeto.getStatus());
		custosFixos.setText("Custos Fixos Atual: " + String.format("R$ %.2f", custosFixosRaiz.totalCustosFixos()));
	}

	private void slider(){
		sliderCustosFixos.valueProperty().addListener((observable, oldValue, newValue) -> {
			double totalCustosFixos = custosFixosRaiz.totalCustosFixos();
			double percentual = newValue.doubleValue() / 100; // Converte o valor do slider para percentual.
			double valorLancado =totalCustosFixos * percentual;
			this.descontoCalculado = valorLancado;

			valorFinanceiroLabel.setText(String.format("Valor: R$ %.2f", valorLancado));
			// Atualiza o valor do Label ou outro componente que exibe o valor percentual.
			valorPercentualLabel.setText(String.format("Proporção: %.1f%%", percentual*100));

		});
	}

	private void labelvalorDoProjeto(){
		totalProjetoLabel.setText(String.format(" R$ %.2f", this.projetoService.totalDoProjeto(projeto.getId())));
	}
	//Retirar este exception daqui e ajeitar a aclasse de erviço
	private void lalabelTotalCustosVariaveis() throws SQLException {
		totalCustosVariaveisLabel.setText(String.format("R$ %.2f", this.custosVariaveisService.totalProjeto(projeto.getId())));
	}

	private void saldoAtualDeCusto(){
		double totalDeCustosLancados = this.custosFixosRaiz.TotalDeLancamentos();
		totalDeLancamentosDeCF.setText(String.format("Total custos distribuídos: R$ %.2f", totalDeCustosLancados));
	}

	private void siuacaoDeCustos(){
		double situacao = this.custosFixosRaiz.totalCustosFixos() - this.custosFixosRaiz.TotalDeLancamentos();
		if(situacao>0) {
			situacaoDeCustos.setText(String.format("Déficit de R$ %.2f", situacao));
		}else{
			situacaoDeCustos.setText("Custo compensado");
		}
	}




}





