package model;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import model.dao.IDados;
import model.util.DadosException;
import model.util.ErroDeDominio;
import model.util.IDadosParaTabela;
import model.util.RegraDeDominio;

/**
 * Classe que representa um modelo de aeronave.
 * 
 * @author Rafael
 *
 */
public class ModeloAeronave implements IDados, Comparable<ModeloAeronave>, Serializable, IDadosParaTabela {

	// CONSTANTES
	public static final int CAPACIDADE_MIN = 15;
	public static final int CAPACIDADE_MAX = 853;

	private String descricao;
	private int capacidade;
	private Set<Poltrona> poltronas;
	private Set<Aeronave> aeronaves;

	/**
	 * Construtor vazio
	 */
	public ModeloAeronave() {
		super();
	}

	/**
	 * Construtor do modelo de aeronave que recebe uma descrição e uma
	 * capacidade. Pode disparar a DadosException.
	 * 
	 * @param descricao
	 *            - recebe uma String que será a descrição do modelo da
	 *            aeronave.
	 * @param capacidade
	 *            - recebe um int que definirá a capacidade do modelo de
	 *            aeronave.
	 * @throws DadosException
	 */
	public ModeloAeronave(String descricao, int capacidade) throws DadosException {
		super();
		this.setDescricao(descricao);
		this.setCapacidade(capacidade);
		this.poltronas = new TreeSet<Poltrona>();
		this.aeronaves = new TreeSet<Aeronave>();
	}

	@Override
	public String toString() {
		return "ModeloAeronave [descricao=" + descricao + ", capacidade=" + capacidade + "]";
	}

	@Override
	public int compareTo(ModeloAeronave novo) {
		return this.getDescricao().compareTo(novo.getDescricao());
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) throws DadosException {
		ModeloAeronave.validarDescricao(descricao);
		this.descricao = descricao;
	}

	public int getCapacidade() {
		return capacidade;
	}

	public void setCapacidade(int capacidade) throws DadosException {
		ModeloAeronave.validarCapacidade(capacidade);
		this.capacidade = capacidade;
	}

	public Set<Poltrona> getPoltronas() {
		return poltronas;
	}

	/**
	 * Adiciona uma poltrona a lista de poltronas do modelo de aeronave. Recebe
	 * um objeto poltrona. Será avaliado a passagem de null que disparará a
	 * DadosException.
	 * 
	 * @param nova
	 *            - um objeto do tipo Poltrona para adicionar a lista de
	 *            poltronas.
	 * @throws DadosException
	 */
	public void addPoltrona(Poltrona nova) throws DadosException {
		ModeloAeronave.validarPoltrona(nova);
		if (!this.poltronas.contains(nova)) {
			this.poltronas.add(nova);
			nova.setModeloAeronave(this);
		}
	}

	/**
	 * Remove uma poltrona da lista de poltronas do modelo de aeronave. Recebe
	 * um objeto Poltrona. Será avaliado a passagem de null que disparará a
	 * DadosException.
	 * 
	 * @param antiga
	 *            - um objeto do tipo Poltrona para remover da lista de
	 *            poltronas.
	 * @throws DadosException
	 */
	public void removePoltrona(Poltrona antiga) throws DadosException {
		ModeloAeronave.validarPoltrona(antiga);
		if (this.poltronas.contains(antiga)) {
			this.poltronas.remove(antiga);
			antiga.setModeloAeronave(null);
		}
	}

	public Set<Aeronave> getAeronaves() {
		return aeronaves;
	}

	/**
	 * Adiciona uma aeronave a lista de aeronaves ao modelo de aeronave. Recebe
	 * um objeto Aeronave. Será avaliado a passagem de null que disparará a
	 * DadosException.
	 * 
	 * @param nova
	 *            - um objeto do tipo Aeronave para adicionar a lista de
	 *            aeronaves.
	 * @throws DadosException
	 */
	public void addAeronave(Aeronave nova) throws DadosException {
		ModeloAeronave.validarAeronave(nova);
		if (!this.aeronaves.contains(nova)) {
			this.aeronaves.add(nova);
			nova.setModeloAeronave(this);
		}
	}

	/**
	 * Remove uma aeronave da lista de aeronaves do modelo de aeronave. Recebe
	 * um objeto Aeronave. Será avaliado a passagem de null que disparará a
	 * DadosException.
	 * 
	 * @param aeronave
	 *            - um objeto do tipo Aeronave para remover da lista de
	 *            aeronaves.
	 * @throws DadosException
	 */
	public void removeAeronave(Aeronave antiga) throws DadosException {
		ModeloAeronave.validarAeronave(antiga);
		if (this.aeronaves.contains(antiga)) {
			this.aeronaves.remove(antiga);
			antiga.setModeloAeronave(null);
		}
	}

	@RegraDeDominio
	public static void validarDescricao(String descricao) throws DadosException {
		if ((descricao == null) || (descricao.length() == 0))
			throw new DadosException(
					new ErroDeDominio(1, ModeloAeronave.class, "Descrição do modelo da aeronave inválida."));
	}

	@RegraDeDominio
	public static void validarCapacidade(int capacidade) throws DadosException {
		if (capacidade < CAPACIDADE_MIN || capacidade > CAPACIDADE_MAX) {
			throw new DadosException(
					new ErroDeDominio(2, ModeloAeronave.class, "Capacidade para o modelo da aeronave inválida."));
		}
	}

	@RegraDeDominio
	private static void validarAeronave(Aeronave aeronave) throws DadosException {
		if (aeronave == null)
			throw new DadosException(new ErroDeDominio(3, ModeloAeronave.class, "Aeronave nula é inválida."));
	}

	@RegraDeDominio
	private static void validarPoltrona(Poltrona nova) throws DadosException {
		if (nova == null)
			throw new DadosException(new ErroDeDominio(4, ModeloAeronave.class, "Poltrona nula é inválida."));
	}

	/**
	 * Retorna a chave do objeto modeloAeronave.
	 */
	@Override
	public Object getChave() {
		return this.descricao;
	}

	/**
	 * Retorna os nomes dos atributos deste objeto para serem usados em uma
	 * tabela.
	 */
	@Override
	public String[] getCamposDeTabela() {
		return new String[] { "Descricao", "Capacidade", "#Poltronas", "#Aeronaves" };
	}

	/**
	 * Retorna um array do tipo Object contendo os valores dos atributos da
	 * classe.
	 */
	@Override
	public Object[] getDadosParaTabela() {
		return new Object[] { this.descricao, this.capacidade, this.poltronas.size(), this.aeronaves.size() };
	}

}
