package model;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import org.joda.time.LocalDate;

import model.dao.IDados;
import model.util.DadosException;
import model.util.ErroDeDominio;
import model.util.IDadosParaTabela;
import model.util.RegraDeDominio;

/**
 * Classe que representa uma compra. Atributos: ordem, data, valorTotal, usuario
 * (que efetua a compra) e uma Collection de passagens.
 * 
 * @author RAFAEL
 *
 */
public class Compra implements IDados, Comparable<Compra>, Serializable, IDadosParaTabela {

	// CONSTANTES
	private static final int TAMANHO_ORDEM = 0;
	private static final double VALORTOTAL_MIN = 0.0;

	private String ordem;
	private LocalDate data;
	private double valorTotal;
	private Usuario usuario;
	private Set<Passagem> passagens;

	/**
	 * Construtor vazio
	 */
	public Compra() {
		super();
	}

	/**
	 * Construtor da compra que recebe uma ordem, a data da compra, o usuario
	 * que está realizando e a passagem escolhida. Pode disparar DadosException.
	 * 
	 * @param ordem
	 *            - recebe uma String que será a ordem de compra.
	 * @param data
	 *            - recebe uma LocalDate (da lib joda-time) que será a data de
	 *            compra.
	 * @param usuario
	 *            - recebe um objeto Usuario que será o usuário da compra.
	 * @throws DadosException
	 */
	public Compra(String ordem, LocalDate data, Usuario usuario) throws DadosException {
		super();
		this.setOrdem(ordem);
		this.setData(data);
		this.setUsuario(usuario);
		this.passagens = new TreeSet<Passagem>();
	}

	@Override
	public String toString() {
		return "Compra [ordem=" + ordem + ", data=" + data + ", valorTotal=" + valorTotal + "]";
	}

	@Override
	public int compareTo(Compra compra) {
		return this.getOrdem().compareTo(compra.getOrdem());
	}

	public String getOrdem() {
		return ordem;
	}

	public void setOrdem(String ordem) throws DadosException {
		Compra.validarOrdem(ordem);
		this.ordem = ordem;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) throws DadosException {
		Compra.validarData(data);
		this.data = data;
	}

	/**
	 * Calcula o valor total da compra, conforme o somatório dos valores da
	 * lista de passagens.
	 * 
	 * @return - retorna um double que indica o valor total da compra.
	 */
	public double getValorTotal() {
		for (Passagem passagem : passagens) {
			this.valorTotal += passagem.getValorPassagem();
		}
		return valorTotal;
	}

	// O valor total será calculado no metodo getValorTotal, pegando os valores
	// das passagens e somando elas.
	/*
	 * public void setValorTotal(Double valorTotal) throws DadosException {
	 * Compra.validarValorTotal(valorTotal); this.valorTotal = valorTotal; }
	 */

	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * Adicionar, alterar ou remover um usuário da compra. Para adicionar e
	 * alterar, passe um objeto usuario. Para excluir passe null. Poderá ser
	 * disparada uma DadosException.
	 * 
	 * @param novo
	 *            - um objeto do tipo Usuario ou null para excluir o usuario.
	 * @throws DadosException
	 */
	public void setUsuario(Usuario novo) throws DadosException {
		if (this.usuario == novo)
			return;
		if (novo == null) {
			Usuario antigo = this.usuario;
			this.usuario = null;
			antigo.removeCompra(this);
		} else {
			if (this.usuario != null)
				this.usuario.removeCompra(this);
			this.usuario = novo;
			novo.addCompra(this);
		}
	}

	public Set<Passagem> getPassagens() {
		return passagens;
	}

	/**
	 * Adicionar passagens a compra. Será avaliado a passagem de null que
	 * disparará a DadosException.
	 * 
	 * @param p
	 *            - recebe um objeto do tipo Passagem.
	 * @throws DadosException
	 */
	public void addPassagem(Passagem p) throws DadosException {
		Compra.validarPassagem(p);
		if (!this.passagens.contains(p)) {
			this.passagens.add(p);
			p.setCompra(this);
		}
	}

	/**
	 * Remover passagens da compra. Será avaliado a passagem de null que
	 * disparará a DadosException.
	 * 
	 * @param p
	 *            - recebe um objeto do tipo Passagem.
	 * @throws DadosException
	 */
	public void removePassagem(Passagem p) throws DadosException {
		Compra.validarPassagem(p);
		if (this.passagens.contains(p)) {
			this.passagens.remove(p);
			p.setCompra(null);
		}
	}

	@RegraDeDominio
	private static void validarOrdem(String o) throws DadosException {
		if ((o == null) || (o.length() == TAMANHO_ORDEM))
			throw new DadosException(new ErroDeDominio(1, Compra.class, "Ordem de Compra inválida."));
		for (int i = 0; i < o.length(); i++) {
			if (!Character.isDigit(o.charAt(i))) {
				throw new DadosException(new ErroDeDominio(2, Compra.class, "Ordem de Compra só aceita digitos."));
			}
		}
	}

	@RegraDeDominio
	private static void validarData(LocalDate d) throws DadosException {
		LocalDate dataAtual = LocalDate.now();

		if (d == null)
			throw new DadosException(new ErroDeDominio(3, Compra.class, "A data de compra não pode ser nula."));
		if (d.isBefore(dataAtual))
			throw new DadosException(
					new ErroDeDominio(4, Compra.class, "A data de compra não pode ser menor que a data atual."));
		if (d.isAfter(dataAtual))
			throw new DadosException(
					new ErroDeDominio(5, Compra.class, "A data de compra não pode ser maior que a data atual."));

	}

	@RegraDeDominio
	private static void validarValorTotal(Double valor) throws DadosException {
		if (valor == null)
			throw new DadosException(new ErroDeDominio(6, Compra.class, "O valor da compra não pode ser nulo."));
		if (valor <= VALORTOTAL_MIN)
			throw new DadosException(new ErroDeDominio(7, Compra.class, "O valor da compra não pode ser negativo."));
	}

	@RegraDeDominio
	private static void validarPassagem(Passagem passagem) throws DadosException {
		if (passagem == null)
			throw new DadosException(new ErroDeDominio(8, Compra.class, "Passagem nula inválida."));
	}

	/**
	 * Retorna a chave do objeto Compra.
	 */
	@Override
	public Object getChave() {
		return this.ordem;
	}

	/**
	 * Retorna os nomes dos atributos deste objeto para serem usados em uma
	 * tabela.
	 */
	@Override
	public String[] getCamposDeTabela() {
		return new String[] { "Ordem", "Data", "Valor Total", "Usuario", "#Passagens" };
	}

	/**
	 * Retorna um array do tipo Object contendo os valores dos atributos da
	 * classe.
	 */
	@Override
	public Object[] getDadosParaTabela() {
		String loginUsuario = this.usuario.getLogin() == null ? "Sem usuário" : this.usuario.getLogin();
		return new Object[] { this.ordem, this.data, this.getValorTotal(), loginUsuario, this.passagens.size() };
	}
}
