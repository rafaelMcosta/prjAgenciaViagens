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
 * Classe que representa um passageiro.
 * 
 * @author Rafael
 *
 */
public class Passageiro implements IDados, Comparable<Passageiro>, Serializable, IDadosParaTabela {

	// CONSTANTES
	public static final int TAMANHO_CPF = 11;
	public static final int TAMANHO_PASSAPORTE = 9;

	private String nome;
	private LocalDate dtNascimento;
	private String cpf;
	private String passaporte;
	private Set<Passagem> passagens;

	/**
	 * Construtor vazio
	 */
	public Passageiro() {
		super();
	}

	/**
	 * Construtor do passageiro que recebe um nome, uma data de nascimento, um
	 * cpf e um passaporte. Pode disparar DadosException.
	 * 
	 * @param nome
	 *            - recebe uma String que será o nome.
	 * @param dtNascimento
	 *            - recebe um LocalDate (da lib joda-time) que será a data de
	 *            nascimento.
	 * @param cpf
	 *            - recebe uma String que será o cpf.
	 * @param passaporte-
	 *            recebe uma String que será o passaporte.
	 * @throws DadosException
	 */
	public Passageiro(String nome, LocalDate dtNascimento, String cpf, String passaporte) throws DadosException {
		this.setNome(nome);
		this.setDtNascimento(dtNascimento);
		this.setCpf(cpf);
		this.setPassaporte(passaporte);
		this.passagens = new TreeSet<Passagem>();
	}


	@Override
	public String toString() {
		return "Passageiro [nome=" + nome + ", dtNascimento=" + dtNascimento + ", cpf=" + cpf + ", passaporte="
				+ passaporte + "]";
	}

	@Override
	public int compareTo(Passageiro passageiro) {
		return this.cpf.compareTo(passageiro.getCpf());
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) throws DadosException {
		Passageiro.validarNome(nome);
		this.nome = nome;
	}

	public LocalDate getDtNascimento() {
		return dtNascimento;
	}

	public void setDtNascimento(LocalDate dtNascimento) throws DadosException {
		Passageiro.validarDtNascimento(dtNascimento);
		this.dtNascimento = dtNascimento;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) throws DadosException {
		Passageiro.validarCpf(cpf);
		this.cpf = cpf;
	}

	public String getPassaporte() {
		return passaporte;
	}

	public void setPassaporte(String passaporte) throws DadosException {
		Passageiro.validarPassaporte(passaporte);
		this.passaporte = passaporte;
	}

	public Set<Passagem> getPassagens() {
		return passagens;
	}

	/**
	 * Adicionar passagens ao passageiro. Será avaliado a passagem de null que
	 * disparará a DadosException.
	 * 
	 * @param nova
	 *            - recebe um objeto do tipo Passagem.
	 * @throws DadosException
	 */
	public void addPassagem(Passagem nova) throws DadosException {
		Passageiro.validarPassagem(nova);
		if (!this.passagens.contains(nova)) {
			this.passagens.add(nova);
			nova.setPassageiro(this);
		}
	}

	/**
	 * Remover passagens do passageiro. Será avaliado a passagem de null que
	 * disparará a DadosException.
	 * 
	 * @param antiga
	 *            - recebe um objeto do tipo Passagem.
	 * @throws DadosException
	 */
	public void removePassagem(Passagem antiga) throws DadosException {
		Passageiro.validarPassagem(antiga);
		if (this.passagens.contains(antiga)) {
			this.passagens.remove(antiga);
			antiga.setPassageiro(null);
		}
	}

	@RegraDeDominio
	private static void validarNome(String nome) throws DadosException {
		if (nome == null || nome.length() == 0)
			throw new DadosException(new ErroDeDominio(1, Passageiro.class, "O nome não pode ser nulo!"));
	}

	@RegraDeDominio
	private static void validarDtNascimento(LocalDate dtNascimento) throws DadosException {
		DateTime dataAtual = DateTime.now();
		long millisAtual = dataAtual.getMillis();
		DateTime dataNasc = new DateTime(dtNascimento.toDate());
		long millisNasc = dataNasc.getMillis();

		if ((millisAtual - millisNasc) <= 63072000000L) {
			throw new DadosException(
					new ErroDeDominio(2, Passageiro.class, "Passageiro com menos de 2 anos não paga passagem!"));
		}
	}

	@RegraDeDominio
	private static void validarCpf(String cpf) throws DadosException {
		cpf = cpf.replace("-", "");
		cpf = cpf.replace(".", "");
		if (cpf == null || cpf.length() == 0)
			throw new DadosException(new ErroDeDominio(3, Usuario.class, "O CPF não pode ser nulo!"));
		if (cpf.length() != TAMANHO_CPF)
			throw new DadosException(new ErroDeDominio(4, Usuario.class, "O CPF não está com todos os seus digitos!"));
		for (int i = 0; i < cpf.length(); i++)
			if (!Character.isDigit(cpf.charAt(i)))
				throw new DadosException(
						new ErroDeDominio(5, Usuario.class, "O CPF na posição " + (i + 1) + " não é numérico!"));

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
			throw new DadosException(new ErroDeDominio(6, Usuario.class, "O CPF é inválido!"));

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
					new ErroDeDominio(7, Usuario.class, "O CPF no primeiro digito verificador é inválido!"));
		soma = 0;
		for (int i = 1; i <= 10; i++) {
			soma += Integer.parseInt(cpf.substring(i - 1, i)) * (12 - i);
		}
		resto = (soma * 10) % 11;
		if ((resto == 10) || (resto == 11))
			resto = 0;
		if (resto != Integer.parseInt(cpf.substring(10, 11)))
			throw new DadosException(
					new ErroDeDominio(8, Usuario.class, "O CPF no segundo digito verificador é inválido!"));

	}

	@RegraDeDominio
	public static void validarPassaporte(String passaporte) throws DadosException {
		if (passaporte == null || passaporte.length() == 0)
			throw new DadosException(new ErroDeDominio(9, Passageiro.class, "O Passaporte não pode ser nulo!"));
		if (passaporte.length() != TAMANHO_PASSAPORTE)
			throw new DadosException(
					new ErroDeDominio(10, Passageiro.class, "O Passaporte não está com todos os seus digitos!"));
		for (int i = 0; i < passaporte.length(); i++) {
			if (!Character.isLetterOrDigit(passaporte.charAt(i)))
				throw new DadosException(new ErroDeDominio(11, Passageiro.class,
						"O Passaporte na posição " + (i + 1) + " não é alfanumerico!"));
		}
		boolean rep = false;
		for (int i = 1; i < passaporte.length(); i++) {
			if (passaporte.charAt(i - 1) == passaporte.charAt(i))
				rep = true;
			else {
				rep = false;
				break;
			}
		}
		if (rep)
			throw new DadosException(new ErroDeDominio(12, Passageiro.class, "O passaporte é inválido"));

	}

	@RegraDeDominio
	private static void validarPassagem(Passagem passagem) throws DadosException {
		if (passagem == null)
			throw new DadosException(new ErroDeDominio(13, Passageiro.class, "Passagem nula inválida."));
	}

	/**
	 * Retorna a chave do objeto passageiro.
	 */
	@Override
	public Object getChave() {
		return this.cpf;
	}

	/**
	 * Retorna os nomes dos atributos deste objeto para serem usados em uma
	 * tabela.
	 */
	@Override
	public String[] getCamposDeTabela() {
		return new String[] { "Nome", "Data de Nascimento", "Cpf", "Passaporte", "#Passagens" };
	}

	/**
	 * Retorna um array do tipo Object contendo os valores dos atributos da
	 * classe.
	 */
	@Override
	public Object[] getDadosParaTabela() {
		return new Object[] { this.nome, this.dtNascimento, this.cpf, this.passaporte, this.passagens.size() };
	}

}
