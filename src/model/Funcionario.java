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
 * Classe que representa um usuário. Atributos: login, senha e cpf e uma
 * Collection de compras.
 * 
 * @author RAFAEL
 *
 */
public class Funcionario implements IDados, Comparable<Funcionario>, Serializable, IDadosParaTabela {

	// CONSTANTES
	public static final int TAMANHO_MIN_LOGIN = 4;
	public static final int TAMANHO_MAX_LOGIN = 20;
	public static final int TAMANHO_MIN_SENHA = 8;
	public static final int TAMANHO_MAX_SENHA = 16;
	public static final int TAMANHO_CPF = 11;

	private String login;
	private String senha;
	private String cpf;
	private Set<Compra> compras;

	/**
	 * Construtor vazio
	 */
	public Funcionario() {
		super();
	}

	/**
	 * Construtor do usuário que recebe o login, senha, cpf como parametros.
	 * Pode disparar DadosException.
	 * 
	 * @param login
	 *            - recebe uma String que será o login do usuário.
	 * @param senha
	 *            - recebe uma String que será a senha do usuário.
	 * @param cpf
	 *            - recebe uma String que será o cpf do usuário.
	 * @throws DadosException
	 */
	public Funcionario(String login, String senha, String cpf) throws DadosException {
		super();
		this.setLogin(login);
		this.setSenha(senha);
		this.setCpf(cpf);
		this.compras = new TreeSet<Compra>();
	}

	@Override
	public String toString() {
		return "Funcionario [login=" + login + ", senha=" + senha + ", cpf=" + cpf + "]";
	}

	@Override
	public int compareTo(Funcionario funcionario) {
		return this.getLogin().compareTo(funcionario.getLogin());
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) throws DadosException {
		Funcionario.validarLogin(login);
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) throws DadosException {
		Funcionario.validarSenha(senha);
		this.senha = senha;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) throws DadosException {
		Funcionario.validarCpf(cpf);
		this.cpf = cpf;
	}

	public Set<Compra> getCompras() {
		return compras;
	}

	/**
	 * Adiciona uma compra a lista de compras do usuário. Recebe um objeto
	 * compra. Será avaliado a passagem de null que disparará a DadosException.
	 * 
	 * @param compra
	 *            - um objeto compra para adicionar a lista
	 * @throws DadosException
	 */
	public void addCompra(Compra compra) throws DadosException {
		Funcionario.validarCompra(compra);
		if (!this.compras.contains(compra)) {
			this.compras.add(compra);
			compra.setFuncionario(this);
		}
	}

	/**
	 * Remove uma compra da lista de compras do usuário. Recebe um objeto
	 * compra. Será avaliado a passagem de null que disparará a DadosException.
	 * 
	 * @param compra
	 *            - um objeto compra para adicionar a lista
	 * @throws DadosException
	 */
	public void removeCompra(Compra compra) throws DadosException {
		Funcionario.validarCompra(compra);
		if (this.compras.contains(compra)) {
			this.compras.remove(compra);
			compra.setFuncionario(null);
		}
	}

	@RegraDeDominio
	private static void validarLogin(String l) throws DadosException {
		if ((l == null) || (l.length() < TAMANHO_MIN_LOGIN))
			throw new DadosException(
					new ErroDeDominio(1, Funcionario.class, "Login deve conter pelo menos quatro caracteres."));
		if (l.length() > TAMANHO_MAX_LOGIN)
			throw new DadosException(
					new ErroDeDominio(2, Funcionario.class, "Login não pode conter mais que 20 caracteres."));
	}

	@RegraDeDominio
	private static void validarSenha(String s) throws DadosException {
		if ((s == null) || (s.length() < TAMANHO_MIN_SENHA))
			throw new DadosException(
					new ErroDeDominio(3, Funcionario.class, "Senha deve conter pelo menos oito caracteres."));
		if (s.length() > TAMANHO_MAX_SENHA)
			throw new DadosException(
					new ErroDeDominio(4, Funcionario.class, "Senha não pode conter mais que 16 caracteres."));
	}

	@RegraDeDominio
	private static void validarCpf(String cpf) throws DadosException {
		cpf = cpf.replace("-", "");
		cpf = cpf.replace(".", "");
		if (cpf == null || cpf.length() == 0)
			throw new DadosException(new ErroDeDominio(5, Funcionario.class, "O CPF não pode ser nulo!"));
		if (cpf.length() != TAMANHO_CPF)
			throw new DadosException(new ErroDeDominio(6, Funcionario.class, "O CPF não está com todos os seus digitos!"));
		for (int i = 0; i < cpf.length(); i++)
			if (!Character.isDigit(cpf.charAt(i)))
				throw new DadosException(
						new ErroDeDominio(7, Funcionario.class, "O CPF na posição " + (i + 1) + " não é numérico!"));

		boolean rep = false;
		for (int i = 1; i < cpf.length(); i++) {
			if (cpf.charAt(i - 1) == cpf.charAt(i))
				rep = true;
			else {
				rep = false;
				break;
			}
		}
		if (rep)
			throw new DadosException(new ErroDeDominio(8, Funcionario.class, "O CPF é inválido!"));

		int soma = 0;
		int resto = 0;

		for (int i = 1; i <= 9; i++) {
			soma += Integer.parseInt(cpf.substring(i - 1, i)) * (11 - i);
		}
		resto = (soma * 10) % 11;
		if ((resto == 10) || (resto == 11))
			resto = 0;
		if (resto != Integer.parseInt(cpf.substring(9, 10)))
			throw new DadosException(
					new ErroDeDominio(9, Funcionario.class, "O CPF no primeiro digito verificador é inválido!"));
		soma = 0;
		for (int i = 1; i <= 10; i++) {
			soma += Integer.parseInt(cpf.substring(i - 1, i)) * (12 - i);
		}
		resto = (soma * 10) % 11;
		if ((resto == 10) || (resto == 11))
			resto = 0;
		if (resto != Integer.parseInt(cpf.substring(10, 11)))
			throw new DadosException(
					new ErroDeDominio(10, Funcionario.class, "O CPF no segundo digito verificador é inválido!"));

	}

	@RegraDeDominio
	private static void validarCompra(Compra compra) throws DadosException {
		if (compra == null)
			throw new DadosException(new ErroDeDominio(11, Funcionario.class, "Compra nula é inválida."));
	}

	/**
	 * Retorna a chave do objeto usuário.
	 */
	@Override
	public Object getChave() {
		return this.login;
	}

	/**
	 * Retorna os nomes dos atributos deste objeto para serem usados em uma
	 * tabela.
	 */
	@Override
	public String[] getCamposDeTabela() {
		return new String[] { "Login", "Senha", "Cpf", "#Compras" };
	}

	/**
	 * Retorna um array do tipo Object contendo os valores dos atributos da
	 * classe.
	 */
	@Override
	public Object[] getDadosParaTabela() {
		return new Object[] { this.login, this.senha, this.cpf, this.compras.size() };
	}

}
