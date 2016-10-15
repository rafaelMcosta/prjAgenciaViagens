package model;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import model.dao.IDados;
import model.util.DadosException;
import model.util.ErroDeDominio;
import model.util.IDadosParaTabela;
import model.util.RegraDeDominio;

/**
 * Classe que representa um voo.
 * 
 * @author Rafael
 *
 */
public class Voo implements IDados, Comparable<Voo>, Serializable, IDadosParaTabela {
	private LocalDate data;
	private Aeronave aeronave;
	private Set<Passagem> passagens;
	private PlanoVoo planoVoo;

	/**
	 * Construtor vazio
	 */
	public Voo() {
		super();
	}

	/**
	 * Construtor do voo que recebe uma data.
	 * 
	 * @param data
	 *            - recebe uma LocalDate (da lib joda-time) que será a data do
	 *            voo.
	 * @param aeronave
	 *            - recebe um objeto Aeronave que representará a aeronave que
	 *            realizará o voo.
	 * @param planoVoo
	 *            - recebe um objeto PlanoVoo que indicará qual o plano de voo.
	 * @throws DadosException
	 */
	public Voo(LocalDate data, Aeronave aeronave, PlanoVoo planoVoo) throws DadosException {
		super();
		this.setData(data);
		this.setAeronave(aeronave);
		this.setPlanoVoo(planoVoo);
		this.passagens = new TreeSet<Passagem>();
	}


	@Override
	public String toString() {
		return "Voo [data=" + data + "]";
	}

	@Override
	public int compareTo(Voo voo) {
		if (this.data.isBefore(voo.getData()))
			return -1;
		else if (this.data.isAfter(voo.getData()))
			return 1;
		else
			return 0;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) throws DadosException {
		Voo.validarData(data);
		this.data = data;
	}

	public Aeronave getAeronave() {
		return aeronave;
	}

	/**
	 * Adicionar, alterar ou remover uma aeronave do voo. Para adicionar e
	 * alterar, passe um objeto Aeronave. Para excluir passe null. Poderá ser
	 * disparada uma DadosException.
	 * 
	 * @param nova
	 *            - um objeto do tipo Aeronave ou null para excluir a aeronave.
	 * @throws DadosException
	 */
	public void setAeronave(Aeronave nova) throws DadosException {
		if (this.aeronave == nova)
			return;
		if (nova == null) {
			Aeronave antiga = this.aeronave;
			this.aeronave = null;
			antiga.removeVoo(this);
		} else {
			if (this.aeronave != null)
				this.aeronave.removeVoo(this);
			this.aeronave = nova;
			nova.addVoo(this);
		}
	}

	public Set<Passagem> getPassagens() {
		return passagens;
	}

	/**
	 * Adiciona uma passagem a lista de passagens do voo. Recebe um objeto
	 * Passagem. Será avaliado a passagem de null que disparará a
	 * DadosException.
	 * 
	 * @param nova
	 *            - um objeto Passagem para adicionar a lista de passagens.
	 * @throws DadosException
	 */
	public void addPassagem(Passagem nova) throws DadosException {
		Voo.validarPassagem(nova);
		if (!this.passagens.contains(nova)) {
			this.passagens.add(nova);
			nova.setVoo(this);
		}
	}

	/**
	 * Remove uma passagem da lista de passagens do voo. Recebe um objeto
	 * Passagem. Será avaliado a passagem de null que disparará a
	 * DadosException.
	 * 
	 * @param antiga
	 *            - um objeto Passagem para remover da lista de passagens.
	 * @throws DadosException
	 */
	public void removePassagem(Passagem antiga) throws DadosException {
		Voo.validarPassagem(antiga);
		if (this.passagens.contains(antiga)) {
			this.passagens.remove(antiga);
			antiga.setVoo(null);
		}
	}

	public PlanoVoo getPlanoVoo() {
		return planoVoo;
	}

	/**
	 * Adicionar, alterar ou remover um plano de voo do voo. Para adicionar e
	 * alterar, passe um objeto PlanoVoo. Para excluir passe null. Poderá ser
	 * disparada uma DadosException.
	 * 
	 * @param novo
	 *            - um objeto do tipo PlanoVoo ou null para excluir o plano de
	 *            voo deste voo.
	 * @throws DadosException 
	 */
	public void setPlanoVoo(PlanoVoo novo) throws DadosException {
		if (this.planoVoo == novo)
			return;
		if (novo == null) {
			PlanoVoo antigo = this.planoVoo;
			this.planoVoo = null;
			antigo.removeVoo(this);
		} else {
			if (this.planoVoo != null)
				this.planoVoo.removeVoo(this);
			this.planoVoo = novo;
			novo.addVoo(this);
		}
	}

	@RegraDeDominio
	private static void validarData(LocalDate d) throws DadosException {
		LocalDate dataAtual = LocalDate.now();
		if (d == null)
			throw new DadosException(new ErroDeDominio(1, Voo.class, "A data do voo não pode ser nula."));
		if (d.isBefore(dataAtual))
			throw new DadosException(
					new ErroDeDominio(2, Voo.class, "A data do voo não pode ser menor que a data atual."));

	}

	@RegraDeDominio
	private static void validarPassagem(Passagem passagem) throws DadosException {
		if (passagem == null)
			throw new DadosException(new ErroDeDominio(3, Voo.class, "Passagem nula inválida."));
	}

	/**
	 * Retorna a chave do objeto voo. Concatenado o número do plano de voo que
	 * ele está relacionado mais a data do voo.
	 */
	@Override
	public Object getChave() {
		return this.planoVoo.getNumero() + this.data.toString();
	}

	/**
	 * Retorna os nomes dos atributos deste objeto para serem usados em uma
	 * tabela.
	 */
	@Override
	public String[] getCamposDeTabela() {
		return new String[] { "Data", "Aeronave", "#Passagens", "Plano de Voo" };
	}

	/**
	 * Retorna um array do tipo Object contendo os valores dos atributos da
	 * classe.
	 */
	@Override
	public Object[] getDadosParaTabela() {
		return new Object[] { this.data, this.aeronave, this.passagens.size(), this.planoVoo };
	}

}
