package model;

import java.io.Serializable;

import model.dao.IDados;
import model.util.DadosException;
import model.util.ErroDeDominio;
import model.util.IDadosParaTabela;
import model.util.RegraDeDominio;

/**
 * Classe que representa uma passagem. Atributos: numero do bilhete, valor da
 * passagem, compra que está vinculada, passageiro, poltrona escolhida para o
 * voo e voo que será realizado por essa passagem.
 * 
 * @author RAFAEL
 *
 */
public class Passagem implements IDados, Comparable<Passagem>, Serializable, IDadosParaTabela {

	// CONSTANTES
	public static final int TAMANHO_NUM_BILHETE = 13;
	public static final double VALOR_MIN = 0.0;

	private String numBilhete;
	private double valorPassagem;
	private Compra compra;
	private Passageiro passageiro;
	private Poltrona poltrona;
	private Voo voo;

	/**
	 * Construtor vazio
	 */
	public Passagem() {
		super();
	}

	/**
	 * Construtor concreto da passagem, recebe todos os atributos próprios e de
	 * relacionamento com outras classes: Compra, Passageiro,Voo, Poltrona. Pode
	 * disparar DadosException.
	 * 
	 * @param numBilhete
	 *            - recebe uma String que será o número do bilhete.
	 * @param valorPassagem
	 *            - recebe um double que será o valor da passagem.
	 * @param compra
	 *            - recebe uma Compra que estará atrelada a esta passagem.
	 * @param passageiro
	 *            - recebe um Passageiro que utilizará esta passagem.
	 * @param poltrona
	 *            - recebe uma Poltrona que a passagem está destinada.
	 * @param voo
	 *            - recebe um Voo para esta passagem.
	 * @throws DadosException
	 */
	public Passagem(String numBilhete, double valorPassagem, Compra compra, Passageiro passageiro, Poltrona poltrona,
			Voo voo) throws DadosException {
		super();
		this.setNumBilhete(numBilhete);
		this.setValorPassagem(valorPassagem);
		this.setCompra(compra);
		this.setPassageiro(passageiro);
		this.setPoltrona(poltrona);
		this.setVoo(voo);
	}

	@Override
	public String toString() {
		return "Passagem [numBilhete=" + numBilhete + ", valorPassagem=" + valorPassagem + ", compra=" + compra + "]";
	}

	@Override
	public int compareTo(Passagem p) {
		return this.numBilhete.compareTo(p.getNumBilhete());
	}

	public String getNumBilhete() {
		return numBilhete;
	}

	public void setNumBilhete(String numBilhete) throws DadosException {
		Passagem.validarNumBilhete(numBilhete);
		this.numBilhete = numBilhete;
	}

	public double getValorPassagem() {
		return valorPassagem;
	}

	public void setValorPassagem(double valorPassagem) throws DadosException {
		Passagem.validarValorPassagem(valorPassagem);
		this.valorPassagem = valorPassagem;
	}

	public Compra getCompra() {
		return compra;
	}

	/**
	 * Adicionar, alterar ou remover uma compra da passagem. Para adicionar e
	 * alterar, passe um objeto compra. Para excluir passe null. Poderá ser
	 * disparada uma DadosException.
	 * 
	 * @param nova
	 *            - um objeto do tipo Compra ou null para excluir a compra.
	 * @throws DadosException
	 */
	public void setCompra(Compra nova) throws DadosException {
		if (this.compra == nova)
			return;
		if (nova == null) {
			Compra antiga = this.compra;
			this.compra = null;
			antiga.removePassagem(this);
		} else {
			if (this.compra != null)
				this.compra.removePassagem(this);
			this.compra = nova;
			nova.addPassagem(this);
		}
	}

	public Passageiro getPassageiro() {
		return passageiro;
	}

	/**
	 * Adicionar, alterar ou remover um passageiro da passagem. Para adicionar e
	 * alterar, passe um objeto passageiro. Para excluir passe null. Poderá ser
	 * disparada uma DadosException.
	 * 
	 * @param novo
	 *            - um objeto do tipo Passageiro ou null para excluir o
	 *            passageiro da passagem.
	 * @throws DadosException
	 */
	public void setPassageiro(Passageiro novo) throws DadosException {
		if (this.passageiro == novo)
			return;
		if (novo == null) {
			Passageiro antigo = this.passageiro;
			this.passageiro = null;
			antigo.removePassagem(this);
		} else {
			if (this.passageiro != null)
				this.passageiro.removePassagem(this);
			this.passageiro = novo;
			novo.addPassagem(this);
		}
	}

	public Poltrona getPoltrona() {
		return poltrona;
	}

	/**
	 * Adicionar, alterar ou remover uma poltrona da passagem. Para adicionar e
	 * alterar, passe um objeto poltrona. Para excluir passe null. Poderá ser
	 * disparada uma DadosException.
	 * 
	 * @param nova
	 *            - um objeto do tipo Poltrona ou null para excluir a poltrona
	 *            da passagem.
	 * @throws DadosException
	 */
	public void setPoltrona(Poltrona nova) throws DadosException {
		if (this.poltrona == nova)
			return;
		if (nova == null) {
			Poltrona antiga = this.poltrona;
			this.poltrona = null;
			antiga.removePassagem(this);
		} else {
			if (this.poltrona != null)
				this.poltrona.removePassagem(this);
			this.poltrona = nova;
			nova.addPassagem(this);
		}
	}

	public Voo getVoo() {
		return voo;
	}

	/**
	 * Adicionar, alterar ou remover um voo da passagem. Para adicionar e
	 * alterar, passe um objeto Voo. Para excluir passe null. Poderá ser
	 * disparada uma DadosException.
	 * 
	 * @param novo
	 *            - um objeto do tipo Voo ou null para excluir o voo da
	 *            passagem.
	 * @throws DadosException 
	 */
	public void setVoo(Voo novo) throws DadosException {
		if (this.voo == novo)
			return;
		if (novo == null) {
			Voo antigo = this.voo;
			this.voo = null;
			antigo.removePassagem(this);
		} else {
			if (this.voo != null)
				this.voo.removePassagem(this);
			this.voo = novo;
			novo.addPassagem(this);
		}
	}

	@RegraDeDominio
	private static void validarNumBilhete(String numBilhete) throws DadosException {
		if ((numBilhete == null) || (numBilhete.length() == 0))
			throw new DadosException(new ErroDeDominio(1, Passagem.class, "Numero do Bilhete ausente."));
		if (numBilhete.length() != TAMANHO_NUM_BILHETE)
			throw new DadosException(
					new ErroDeDominio(2, Passagem.class, "Numero do Bilhete inválido, deve possuir 13 digitos."));
		for (int i = 0; i < numBilhete.length(); i++) {
			if (!Character.isDigit(numBilhete.charAt(i))) {
				throw new DadosException(new ErroDeDominio(3, Passagem.class,
						"Numero do Bilhete na posição " + (i + 1) + " não é um digito."));
			}
		}
		boolean rep = false;
		for (int i = 1; i < numBilhete.length(); i++) {
			if (numBilhete.charAt(i - 1) == numBilhete.charAt(i))
				rep = true;
			else {
				rep = false;
				break;
			}
		}
		if (rep)
			throw new DadosException(new ErroDeDominio(4, Passageiro.class, "Numero do Bilhete é inválido"));
	}

	private static void validarValorPassagem(double valorPassagem) throws DadosException {
		if (valorPassagem < VALOR_MIN)
			throw new DadosException(
					new ErroDeDominio(5, Passagem.class, "O valor da passagem não pode ser negativo."));
	}

	/**
	 * Retorna a chave do objeto usuário.
	 */
	@Override
	public Object getChave() {
		return this.numBilhete;
	}

	/**
	 * Retorna os nomes dos atributos deste objeto para serem usados em uma
	 * tabela.
	 */
	@Override
	public String[] getCamposDeTabela() {
		return new String[] { "Numero do Bilhete", "Compra", "Passageiro", "Poltrona", "Voo" };
	}

	/**
	 * Retorna um array do tipo Object contendo os valores dos atributos da
	 * classe.
	 */
	@Override
	public Object[] getDadosParaTabela() {
		return new Object[] { this.numBilhete, this.compra, this.passageiro, this.poltrona, this.voo };
	}

}
