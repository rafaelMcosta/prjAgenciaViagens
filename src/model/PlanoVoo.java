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
 * Classe que representa um plano de voo.
 * 
 * @author Rafael
 *
 */
public class PlanoVoo implements IDados, Comparable<PlanoVoo>, Serializable, IDadosParaTabela {

	// CONSTANTES
	public static final int TAMANHO_MIN_NUMERO = 0;
	public static final int TAMANHO_NUMERO = 6;

	private String numero;
	private Set<Voo> voos;
	private Aeroporto origem;
	private Aeroporto destino;

	/**
	 * Construtor vazio.
	 */
	public PlanoVoo() {
		super();
	}

	/**
	 * Construtor do plano de voo que recebe um numero.
	 * 
	 * @param numero
	 *            - recebe uma String que será o numero do plano de voo.
	 * @param origem
	 *            - recebe um objeto do tipo Aeroporto que será a origem do
	 *            plano de voo.
	 * @param destino
	 *            - recebe um objeto do tipo Aeroporto que será o destino do
	 *            plano de voo.
	 * @throws DadosException
	 */
	public PlanoVoo(String numero, Aeroporto origem, Aeroporto destino) throws DadosException {
		super();
		this.setNumero(numero);
		this.setOrigem(origem);
		this.setDestino(destino);
		this.voos = new TreeSet<Voo>();
	}

	@Override
	public String toString() {
		return "PlanoVoo [numero=" + numero + "]";
	}

	@Override
	public int compareTo(PlanoVoo planoVoo) {
		return this.numero.compareTo(planoVoo.getNumero());
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) throws DadosException {
		PlanoVoo.validarNumero(numero);
		this.numero = numero;
	}

	public Set<Voo> getVoos() {
		return voos;
	}

	/**
	 * Adicionar voos ao plano de voo. Será avaliado a passagem de null que
	 * disparará a DadosException.
	 * 
	 * @param novo
	 *            - recebe um objeto do tipo Voo.
	 * @throws DadosException
	 */
	public void addVoo(Voo novo) throws DadosException {
		PlanoVoo.validarVoo(novo);
		if (!this.voos.contains(novo)) {
			this.voos.add(novo);
			novo.setPlanoVoo(this);
		}
	}

	/**
	 * Remover voos do plano de voo. Será avaliado a passagem de null que
	 * disparará a DadosException.
	 * 
	 * @param antigo
	 *            - recebe um objeto do tipo Voo.
	 * @throws DadosException
	 */
	public void removeVoo(Voo antigo) throws DadosException {
		PlanoVoo.validarVoo(antigo);
		if (this.voos.contains(antigo)) {
			this.voos.remove(antigo);
			antigo.setPlanoVoo(null);
		}
	}

	public Aeroporto getOrigem() {
		return origem;
	}

	/**
	 * Adicionar, alterar ou remover um aeroporto do plano de voo. Para
	 * adicionar e alterar, passe um objeto Aeroporto. Para excluir passe null.
	 * Poderá ser disparada uma DadosException.
	 * 
	 * @param origem
	 *            - um objeto do tipo Aeroporto ou null para excluir.
	 * @throws DadosException
	 */
	public void setOrigem(Aeroporto origem) throws DadosException {
		if (this.origem == origem)
			return;
		if (origem == null) {
			Aeroporto antigo = this.origem;
			this.origem = null;
			antigo.removePartida(this);
		} else {
			if (this.origem != null)
				this.origem.removePartida(this);
			this.origem = origem;
			origem.addPartida(this);
		}
	}

	public Aeroporto getDestino() {
		return destino;
	}

	/**
	 * Adicionar, alterar ou remover um aeroporto do plano de voo. Para
	 * adicionar e alterar, passe um objeto Aeroporto. Para excluir passe null.
	 * Poderá ser disparada uma DadosException.
	 * 
	 * @param destino
	 *            - um objeto do tipo Aeroporto ou null para excluir.
	 * @throws DadosException
	 */
	public void setDestino(Aeroporto destino) throws DadosException {
		if (this.destino == destino)
			return;
		if (destino == null) {
			Aeroporto antigo = this.destino;
			this.destino = null;
			antigo.removeChegada(this);
		} else {
			if (this.destino != null)
				this.destino.removeChegada(this);
			this.destino = destino;
			destino.addChegada(this);
		}
	}

	@RegraDeDominio
	public static void validarNumero(String numero) throws DadosException {
		if ((numero == null) || (numero.length() == TAMANHO_MIN_NUMERO)) {
			throw new DadosException(
					new ErroDeDominio(1, PlanoVoo.class, "O número do plano de voo não pode ser nulo."));
		}
		if (numero.length() != TAMANHO_NUMERO) {
			throw new DadosException(
					new ErroDeDominio(2, PlanoVoo.class, "O número do plano de voo deve conter 6 caracteres."));
		}

		if (!Character.isLetter(numero.charAt(0)))
			throw new DadosException(new ErroDeDominio(3, PlanoVoo.class,
					"O primeiro caracter do numero do Plano de Voo deve ser letra."));
		if (!Character.isLetter(numero.charAt(1)))
			throw new DadosException(new ErroDeDominio(4, PlanoVoo.class,
					"O segundo caracter do numero do Plano de Voo deve ser letra."));

		for (int i = 2; i < numero.length(); i++) {
			if (!Character.isDigit(numero.charAt(i))) {
				throw new DadosException(new ErroDeDominio(5, PlanoVoo.class,
						"O número do plano de voo na posição " + (i + 1) + " de ser um numero."));
			}
		}

	}

	@RegraDeDominio
	private static void validarVoo(Voo voo) throws DadosException {
		if (voo == null)
			throw new DadosException(new ErroDeDominio(4, PlanoVoo.class, "Voo null inválido."));
	}

	/**
	 * Retorna a chave do objeto PlanoVoo.
	 */
	@Override
	public Object getChave() {
		return this.numero;
	}

	/**
	 * Retorna os nomes dos atributos deste objeto para serem usados em uma
	 * tabela.
	 */
	@Override
	public String[] getCamposDeTabela() {
		return new String[] { "Numero", "#Voos", "Origem", "Destino" };
	}

	/**
	 * Retorna um array do tipo Object contendo os valores dos atributos da
	 * classe.
	 */
	@Override
	public Object[] getDadosParaTabela() {
		return new Object[] { this.numero, this.voos.size(), this.origem, this.destino };
	}

}
