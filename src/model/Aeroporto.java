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
 * Classe que representa um aeroporto.
 * 
 * @author Rafael
 *
 */
public class Aeroporto implements IDados, Comparable<Aeroporto>, Serializable, IDadosParaTabela {

	// CONSTANTES
	public static final int TAMANHO_MIN_SIGLA = 0;
	public static final int TAMANHO_SIGLA = 3;
	public static final int TAMANHO_MIN_CIDADE = 0;
	public static final int TAMANHO_CIDADE = 3;
	public static final int TAMANHO_MIN_ESTADO = 0;
	public static final int TAMANHO_ESTADO = 2;
	public static final int TAMANHO_MIN_PAIS = 0;
	public static final int TAMANHO_PAIS = 3;

	private String sigla;
	private String cidade;
	private String estado;
	private String pais;
	private Set<PlanoVoo> partidas;
	private Set<PlanoVoo> chegadas;

	/**
	 * Construtor vazio.
	 */
	public Aeroporto() {
		super();
	}

	/**
	 * Construtor do aeroporto que recebe uma sigla, cidade, estado e pais. Pode
	 * disparar DadosException.
	 * 
	 * @param sigla
	 *            - recebe uma String que será a sigla do aeroporto.
	 * @param cidade
	 *            - recebe uma String que será a cidade do aeroporto.
	 * @param estado
	 *            - recebe uma String que será o estado do aeroporto.
	 * @param pais
	 *            - recebe uma String que será o país do aeroporto.
	 * @throws DadosException
	 */
	public Aeroporto(String sigla, String cidade, String estado, String pais) throws DadosException {
		super();
		this.setSigla(sigla);
		this.setCidade(cidade);
		this.setEstado(estado);
		this.setPais(pais);
		this.partidas = new TreeSet<PlanoVoo>();
		this.chegadas = new TreeSet<PlanoVoo>();
	}

	@Override
	public String toString() {
		return "Aeroporto [sigla=" + sigla + ", cidade=" + cidade + ", estado=" + estado + ", pais=" + pais + "]";
	}

	@Override
	public int compareTo(Aeroporto aeroporto) {
		return this.sigla.compareTo(aeroporto.getSigla());
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) throws DadosException {
		Aeroporto.validarSigla(sigla);
		this.sigla = sigla;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) throws DadosException {
		Aeroporto.validarCidade(cidade);
		this.cidade = cidade;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) throws DadosException {
		Aeroporto.validarEstado(estado);
		this.estado = estado;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) throws DadosException {
		Aeroporto.validarPais(pais);
		this.pais = pais;
	}

	public Set<PlanoVoo> getPartidas() {
		return partidas;
	}

	/**
	 * Adicionar planos de voo considerados partidas deste aeroporto. Será
	 * avaliado a passagem de null que disparará a DadosException.
	 * 
	 * @param novo
	 *            - recebe um objeto do tipo PlanoVoo.
	 * @throws DadosException
	 */
	public void addPartida(PlanoVoo novo) throws DadosException {
		Aeroporto.validarPlanoVoo(novo);
		if (!this.partidas.contains(novo)) {
			this.partidas.add(novo);
			novo.setOrigem(this);
		}
	}

	/**
	 * Remover plano de voo considerados partidas deste aeroporto. Será avaliado
	 * a passagem de null que disparará a DadosException.
	 * 
	 * @param antigo
	 *            - recebe um objeto do tipo PlanoVoo.
	 * @throws DadosException
	 */
	public void removePartida(PlanoVoo antigo) throws DadosException {
		Aeroporto.validarPlanoVoo(antigo);
		if (this.partidas.contains(antigo)) {
			this.partidas.remove(antigo);
			antigo.setOrigem(null);
		}
	}

	public Set<PlanoVoo> getChegadas() {
		return chegadas;
	}

	/**
	 * Adicionar planos de voo considerados chegadas a este aeroporto. Será
	 * avaliado a passagem de null que disparará a DadosException.
	 * 
	 * @param novo
	 *            - recebe um objeto do tipo PlanoVoo.
	 * @throws DadosException
	 */
	public void addChegada(PlanoVoo novo) throws DadosException {
		Aeroporto.validarPlanoVoo(novo);
		if (!this.chegadas.contains(novo)) {
			this.chegadas.add(novo);
			novo.setDestino(this);
		}
	}

	/**
	 * Remover plano de voo considerados chegadas a este aeroporto. Será
	 * avaliado a passagem de null que disparará a DadosException.
	 * 
	 * @param antigo
	 *            - recebe um objeto do tipo PlanoVoo.
	 * @throws DadosException
	 */
	public void removeChegada(PlanoVoo antigo) throws DadosException {
		Aeroporto.validarPlanoVoo(antigo);
		if (this.chegadas.contains(antigo)) {
			this.chegadas.remove(antigo);
			antigo.setDestino(null);
		}
	}

	@RegraDeDominio
	public static void validarSigla(String sigla) throws DadosException {
		if ((sigla == null) || (sigla.length() == TAMANHO_MIN_SIGLA))
			throw new DadosException(new ErroDeDominio(1, Aeroporto.class, "A sigla do aeroporto não pode ser nula."));
		if (sigla.length() != TAMANHO_SIGLA) {
			throw new DadosException(
					new ErroDeDominio(2, Aeroporto.class, "A sigla do aeroporto deve conter exatos 3 caracteres."));
		}
		for (int i = 0; i < sigla.length(); i++) {
			if (!Character.isLetter(sigla.charAt(i))) {
				throw new DadosException(new ErroDeDominio(3, Aeroporto.class,
						"A sigla do aeroporto na posição " + (i + 1) + " não é uma letra"));
			}
		}
	}

	@RegraDeDominio
	public static void validarCidade(String cidade) throws DadosException {
		if ((cidade == null) || (cidade.length() == TAMANHO_MIN_CIDADE)) {
			throw new DadosException(new ErroDeDominio(4, Aeroporto.class, "A cidade do aeroporto não pode ser nula."));
		}

		if (cidade.length() < TAMANHO_CIDADE)
			throw new DadosException(new ErroDeDominio(5, Aeroporto.class,
					"A cidade do aeroporto deve conter pelo menos 3 caracteres."));
		for (int i = 0; i < cidade.length(); i++) {
			if (!Character.isLetter(cidade.charAt(i))) {
				throw new DadosException(new ErroDeDominio(6, Aeroporto.class,
						"A cidade do aeroporto na posição " + (i + 1) + " não é uma letra"));
			}
		}
	}

	@RegraDeDominio
	public static void validarEstado(String estado) throws DadosException {
		if ((estado == null) || (estado.length() == TAMANHO_MIN_ESTADO)) {
			throw new DadosException(new ErroDeDominio(7, Aeroporto.class, "O estado do aeroporto não pode ser nulo."));
		}
		if (estado.length() != TAMANHO_ESTADO)
			throw new DadosException(
					new ErroDeDominio(8, Aeroporto.class, "O estado deve conter 2 caracteres (Sigla UF)."));
		for (int i = 0; i < estado.length(); i++) {
			if (!Character.isLetter(estado.charAt(i))) {
				throw new DadosException(new ErroDeDominio(9, Aeroporto.class,
						"O estado do aeroporto na posição " + (i + 1) + " não é uma letra"));
			}
		}
	}

	@RegraDeDominio
	public static void validarPais(String pais) throws DadosException {
		if ((pais == null) || (pais.length() == TAMANHO_MIN_PAIS)) {
			throw new DadosException(new ErroDeDominio(10, Aeroporto.class, "O pais do aeroporto não pode ser nulo."));
		}
		if (pais.length() < TAMANHO_PAIS)
			throw new DadosException(
					new ErroDeDominio(11, Aeroporto.class, "O pais do aeroporto deve conter pelo menos 3 caracteres."));
		for (int i = 0; i < pais.length(); i++) {
			if (!Character.isLetter(pais.charAt(i))) {
				throw new DadosException(new ErroDeDominio(12, Aeroporto.class,
						"O pais do aeroporto na posição " + (i + 1) + " não é uma letra"));
			}
		}
	}

	@RegraDeDominio
	private static void validarPlanoVoo(PlanoVoo planoVoo) throws DadosException {
		if (planoVoo == null)
			throw new DadosException(new ErroDeDominio(13, Aeroporto.class, "Plano de Voo nulo é inválido."));
	}

	/**
	 * Retorna a chave do objeto Aeroporto.
	 */
	@Override
	public Object getChave() {
		return this.sigla;
	}

	/**
	 * Retorna os nomes dos atributos deste objeto para serem usados em uma
	 * tabela.
	 */
	@Override
	public String[] getCamposDeTabela() {
		return new String[] { "Sigla", "Cidade", "Estado", "Pais", "#Partidas", "#Chegadas" };
	}

	/**
	 * Retorna um array do tipo Object contendo os valores dos atributos da
	 * classe.
	 */
	@Override
	public Object[] getDadosParaTabela() {
		return new Object[] { this.sigla, this.cidade, this.estado, this.pais, this.partidas.size(),
				this.chegadas.size() };
	}

}
