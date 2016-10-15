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
 * Classe que representa uma aeronave.
 * 
 * @author Rafael
 *
 */
public class Aeronave implements IDados, Comparable<Aeronave>, Serializable, IDadosParaTabela {

	// CONSTANTES
	public static final int TAMANHO_MIN_MATRICULA = 0;

	private String matricula;
	private ModeloAeronave modeloAeronave;
	private Set<Voo> voos;

	/**
	 * Construtor vazio
	 */
	public Aeronave() {
		super();
	}

	/**
	 * Construtor da aeronave que recebe uma matricula. Pode disparar
	 * DadosException.
	 * 
	 * @param matricula
	 *            - recebe uma String que será a matricula.
	 * @param modeloAeronave
	 *            - recebe um objeto do tipo ModeloAeronave que representará o
	 *            modelo de aeronave.
	 * @throws DadosException
	 */
	public Aeronave(String matricula, ModeloAeronave modeloAeronave) throws DadosException {
		super();
		this.setMatricula(matricula);
		this.setModeloAeronave(modeloAeronave);
		this.voos = new TreeSet<Voo>();
	}

	@Override
	public String toString() {
		return "Aeronave [matricula=" + matricula + "]";
	}

	@Override
	public int compareTo(Aeronave aeronave) {
		return this.matricula.compareTo(aeronave.getMatricula());
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) throws DadosException {
		Aeronave.validarMatricula(matricula);
		this.matricula = matricula;
	}

	public ModeloAeronave getModeloAeronave() {
		return modeloAeronave;
	}

	/**
	 * Adicionar, alterar ou remover um modelo de aeronave da aeronave. Para
	 * adicionar e alterar, passe um objeto ModeloAeronave. Para excluir passe
	 * null. Poderá ser disparada uma DadosException.
	 * 
	 * @param novo
	 *            - um objeto do tipo ModeloAeronave ou null para excluir o
	 *            modelo de aeronave.
	 * @throws DadosException
	 */
	public void setModeloAeronave(ModeloAeronave novo) throws DadosException {
		if (this.modeloAeronave == novo)
			return;
		if (novo == null) {
			ModeloAeronave antigo = this.modeloAeronave;
			this.modeloAeronave = null;
			antigo.removeAeronave(this);
		} else {
			if (this.modeloAeronave != null)
				this.modeloAeronave.removeAeronave(this);
			this.modeloAeronave = novo;
			novo.addAeronave(this);
		}
	}

	public Set<Voo> getVoos() {
		return voos;
	}

	/**
	 * Adicionar voos a aeronave. Será avaliado a passagem de null que disparará
	 * a DadosException.
	 * 
	 * @param novo
	 *            - recebe um objeto do tipo Voo.
	 * @throws DadosException
	 */
	public void addVoo(Voo novo) throws DadosException {
		Aeronave.validarVoo(novo);
		if (!this.voos.contains(novo)) {
			this.voos.add(novo);
			novo.setAeronave(this);
		}
	}

	/**
	 * Remover voos da aeronave. Será avaliado a passagem de null que disparará
	 * a DadosException.
	 * 
	 * @param antigo
	 *            - - recebe um objeto do tipo Voo.
	 * @throws DadosException
	 */
	public void removeVoo(Voo antigo) throws DadosException {
		Aeronave.validarVoo(antigo);
		if (this.voos.contains(antigo)) {
			this.voos.remove(antigo);
			antigo.setAeronave(null);
		}
	}

	@RegraDeDominio
	private static void validarMatricula(String matricula) throws DadosException {
		if ((matricula == null) || (matricula.length() == TAMANHO_MIN_MATRICULA))
			throw new DadosException(new ErroDeDominio(1, Aeronave.class, "Matrícula da aeronave inválida."));
	}

	@RegraDeDominio
	private static void validarVoo(Voo voo) throws DadosException {
		if (voo == null)
			throw new DadosException(new ErroDeDominio(2, Aeronave.class, "Voo null inválido."));
	}

	/**
	 * Retorna a chave do objeto Aeronave.
	 */
	@Override
	public Object getChave() {
		return this.matricula;
	}

	/**
	 * Retorna os nomes dos atributos deste objeto para serem usados em uma
	 * tabela.
	 */
	@Override
	public String[] getCamposDeTabela() {
		return new String[] { "Matricula", "Modelo Aeronave", "#Voos" };
	}

	/**
	 * Retorna um array do tipo Object contendo os valores dos atributos da
	 * classe.
	 */
	@Override
	public Object[] getDadosParaTabela() {
		return new Object[] { this.matricula, this.modeloAeronave, this.voos.size() };
	}

}
