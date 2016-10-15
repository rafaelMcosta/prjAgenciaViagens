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
 * Classe que representa uma poltrona.
 * 
 * @author Rafael
 *
 */

/**
 * @author Rafael
 *
 */
/**
 * @author Rafael
 *
 */
public class Poltrona implements IDados, Comparable<Poltrona>, Serializable, IDadosParaTabela {

	// CONSTANTES
	public static final int TAMANHO_MIN_NUMERO = 1;
	public static final int TAMANHO_MAX_NUMERO = 3;

	private String numero;
	private Character letra;
	private tipoPoltrona tipoPoltrona;
	private tipoClasse tipoClasse;
	private Set<Passagem> passagens;
	private ModeloAeronave modeloAeronave;

	/**
	 * Enum para definir o tipo da classe (ECONOMICA, EXECUTIVA OU
	 * PRIMEIRA_CLASSE) que a poltrona irá ficar.
	 * 
	 * @author Rafael
	 *
	 */
	public enum tipoClasse {
		ECONOMICA, EXECUTIVA, PRIMEIRA_CLASSE;
	}

	/**
	 * Enum para definir a disposição (JANELA OU CORREDOR) da poltrona.
	 * 
	 * @author Rafael
	 *
	 */
	public enum tipoPoltrona {
		JANELA, CORREDOR;
	}

	/**
	 * Construtor vazio.
	 */
	public Poltrona() {
		super();
	}

	/**
	 * Construtor de poltrona que recebe um numero, letra, tipoPoltrona,
	 * tipoClasse. Pode disparar uma DadosException.
	 * 
	 * @param numero
	 *            - recebe uma String que será o número da poltrona.
	 * @param letra
	 *            - recebe um Character que será a letra da poltrona.
	 * @param tipoPoltrona
	 *            - recebe um Enum (tipoPoltrona) que será o tipo da poltrona.
	 * @param tipoClasse
	 *            - recebe um Enum (tipoClasse) que será o tipo da classe.
	 * @param modeloAeronave
	 *            - recebe um ModeloAeronave a qual esta poltrona se destina.
	 * @throws DadosException
	 */
	public Poltrona(String numero, Character letra, tipoPoltrona tipoPoltrona, tipoClasse tipoClasse,
			ModeloAeronave modeloAeronave) throws DadosException {
		super();
		this.setNumero(numero);
		this.setLetra(letra);
		this.setTipoPoltrona(tipoPoltrona);
		this.setTipoClasse(tipoClasse);
		this.setModeloAeronave(modeloAeronave);
		this.passagens = new TreeSet<Passagem>();
	}

	@Override
	public String toString() {
		return "Poltrona [numero=" + numero + ", letra=" + letra + ", tipoPoltrona=" + tipoPoltrona + ", tipoClasse="
				+ tipoClasse + "]";
	}

	@Override
	public int compareTo(Poltrona o) {
		String estaPoltrona = this.numero + this.letra;
		String novaPoltrona = o.getNumero() + o.getLetra();
		return estaPoltrona.compareTo(novaPoltrona);
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) throws DadosException {
		Poltrona.validarNumero(numero);
		this.numero = numero;
	}

	public Character getLetra() {
		return letra;
	}

	public void setLetra(Character letra) throws DadosException {
		Poltrona.validarLetra(letra);
		this.letra = letra;
	}

	public tipoPoltrona getTipoPoltrona() {
		return tipoPoltrona;
	}

	public void setTipoPoltrona(tipoPoltrona tipoPoltrona) {
		this.tipoPoltrona = tipoPoltrona;
	}

	public tipoClasse getTipoClasse() {
		return tipoClasse;
	}

	public void setTipoClasse(tipoClasse tipoClasse) {
		this.tipoClasse = tipoClasse;
	}

	public Set<Passagem> getPassagens() {
		return passagens;
	}

	/**
	 * Adicionar passagens a poltrona. Será avaliado a passagem de null que
	 * disparará a DadosException.
	 * 
	 * @param nova
	 *            - recebe um objeto do tipo Passagem.
	 * @throws DadosException
	 */
	public void addPassagem(Passagem nova) throws DadosException {
		Poltrona.validarPassagem(nova);
		if (!this.passagens.contains(nova)) {
			this.passagens.add(nova);
			nova.setPoltrona(this);
		}
	}

	/**
	 * Remover passagens da poltrona. Será avaliado a passagem de null que
	 * disparará a DadosException.
	 * 
	 * @param antiga
	 *            - recebe um objeto do tipo Passagem.
	 * @throws DadosException
	 */
	public void removePassagem(Passagem antiga) throws DadosException {
		Poltrona.validarPassagem(antiga);
		if (this.passagens.contains(antiga)) {
			this.passagens.remove(antiga);
			antiga.setPoltrona(null);
		}
	}

	public ModeloAeronave getModeloAeronave() {
		return modeloAeronave;
	}

	/**
	 * Adicionar, alterar ou remover um modelo de aeronave da poltrona. Para
	 * adicionar e alterar, passe um objeto modeloAeronave. Para excluir passe
	 * null. Poderá ser disparada uma DadosException.
	 * 
	 * @param modeloAeronave
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
			antigo.removePoltrona(this);
		} else {
			if (this.modeloAeronave != null)
				this.modeloAeronave.removePoltrona(this);
			this.modeloAeronave = novo;
			novo.addPoltrona(this);
		}
	}

	@RegraDeDominio
	private static void validarNumero(String num) throws DadosException {
		if ((num == null) || (num.length() < TAMANHO_MIN_NUMERO) || (num.length() > TAMANHO_MAX_NUMERO))
			throw new DadosException(new ErroDeDominio(1, Poltrona.class, "Numero de poltrona inválida."));
		for (int i = 0; i < num.length(); i++) {
			if (!Character.isDigit(num.charAt(i))) {
				throw new DadosException(new ErroDeDominio(2, Poltrona.class, "Numero de poltrona só aceita digitos."));
			}
		}
	}

	@RegraDeDominio
	private static void validarLetra(Character letra) throws DadosException {
		if ((letra == null) || (letra == 0))
			throw new DadosException(new ErroDeDominio(3, Poltrona.class, "Letra da poltrona inválida."));
		if (!Character.isLetter(letra)) {
			throw new DadosException(
					new ErroDeDominio(4, Poltrona.class, "Letra da poltrona só aceita caracteres de A-Z."));
		}
	}

	@RegraDeDominio
	private static void validarPassagem(Passagem passagem) throws DadosException {
		if (passagem == null)
			throw new DadosException(new ErroDeDominio(5, Poltrona.class, "Passagem nula inválida."));
	}

	/**
	 * Retorna a chave do objeto poltrona.
	 */
	@Override
	public Object getChave() {
		return this.numero + this.letra;
	}

	/**
	 * Retorna os nomes dos atributos deste objeto para serem usados em uma
	 * tabela.
	 */
	@Override
	public String[] getCamposDeTabela() {
		return new String[] { "Numero", "Letra", "Tipo Poltrona", "Classe", "#Passagens", "Modelo Aeronave" };
	}

	/**
	 * Retorna um array do tipo Object contendo os valores dos atributos da
	 * classe.
	 */
	@Override
	public Object[] getDadosParaTabela() {
		return new Object[] { this.numero, this.letra, this.tipoPoltrona, this.tipoClasse, this.passagens.size(),
				this.modeloAeronave };
	}

}
