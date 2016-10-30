package control;

import org.joda.time.LocalDate;

import control.util.ControleException;
import control.util.ErroDeControle;
import control.util.ICtrlCasoDeUso;
import model.Passageiro;
import model.dao.DAO;
import model.dao.IDAO;
import model.util.DadosException;
import viewer.UIPassageiro;
import viewer.ViewerManager;

public class CtrlAlterarPassageiro implements ICtrlCasoDeUso {
	//
	// ATRIBUTOS
	//
	public enum Status {
		ALTERANDO, ENCERRADO;

		public static void validarTransicaoStatus(Status anterior, Status novo) throws ControleException {
			if (anterior == null && novo == ALTERANDO || anterior == ALTERANDO && novo == ENCERRADO)
				return;
			throw new ControleException(new ErroDeControle("N�o se pode sair do estado "
					+ (anterior == null ? "NULO" : anterior) + " e ir para o estado " + novo));
		}
	}

	/**
	 * Refer�ncia para o controlador do caso de uso manter.
	 */
	private final CtrlManterPassageiros ctrlManterPassageiros;

	/**
	 * Refer�ncia para o objeto a ser atualizado
	 */
	private Passageiro atual;

	/**
	 * Refer�ncia para a janela Passageiro que permitir� a inclus�o e altera��o
	 * do Passageiro
	 */
	private UIPassageiro uiPassageiro;

	/**
	 * Refer�ncia para o objeto DaoPassageiro
	 */
	private IDAO<Passageiro> dao = (IDAO<Passageiro>) DAO.getDAO(Passageiro.class);

	/**
	 * Atributo que indica qual opera��o est� em curso
	 */
	private Status status;

	//
	// M�TODOS
	//

	/**
	 * Construtor da classe CtrlAlterarPassageiro
	 */
	public CtrlAlterarPassageiro(CtrlManterPassageiros ctrl, Passageiro p) throws ControleException, DadosException {
		// Guardo a refer�ncia para o controlador do programa
		this.ctrlManterPassageiros = ctrl;
		// Guardo a refer�ncia para o objeto a ser alterado
		this.atual = p;
		// Iniciando o caso de uso
		this.iniciar();
	}

	/** 
	 * 
	 */
	public void iniciar() throws ControleException, DadosException {
		// Informo que o controlador de caso de uso est� dispon�vel
		this.setStatus(Status.ALTERANDO);
		// Crio e abro a janela de altera��o
		this.uiPassageiro = (UIPassageiro) ViewerManager.obterViewer(this, UIPassageiro.class);
		// Solicito � interface que atualize os campos
		this.uiPassageiro.atualizarCampos(this.atual.getNome(), this.atual.getCpf(), this.atual.getPassaporte(),
				this.atual.getDtNascimento().toString());
		// Solicito � interface que carregue os objetos
		this.uiPassageiro.exibir();
	}

	/** 
	 * 
	 */
	public void terminar() throws DadosException, ControleException {
		if (this.status == Status.ENCERRADO)
			return;
		// N�o h� Passageiro em manipula��o
		this.atual = null;
		// Fecho a janela
		this.uiPassageiro.fechar();
		// Informo que o controlador est� dispon�vel
		this.setStatus(Status.ENCERRADO);
		// Notifico ao controlador do programa o t�rmino deste caso de uso
		ctrlManterPassageiros.terminarCasoDeUsoAlterarPassageiro();
	}

	/** 
	 * 
	 */
	public void cancelarAlterar() throws DadosException, ControleException {
		// Se o controlador n�o tinha ativado uma altera��o, n�o fa�o nada!
		if (this.status != Status.ALTERANDO)
			throw new ControleException(new ErroDeControle("N�o � poss�vel cancelar uma opera��o de altera��o"));
		// Termino o caso de uso
		this.terminar();
	}

	/** 
	 * 
	 */
	public void alterar(String nome, String dtNascimento, String cpf, String passaporte)
			throws DadosException, ControleException {
		// Se o controlador n�o tinha ativado uma altera��o, n�o fa�o nada!
		if (this.status != Status.ALTERANDO)
			throw new ControleException(new ErroDeControle("N�o � poss�vel concluir uma opera��o de altera��o"));
		// Atualizo os campos
		this.atual.setNome(nome);
		String[] nascimento = dtNascimento.split("-");
		int dia = Integer.parseInt(nascimento[2]);
		int mes = Integer.parseInt(nascimento[1]);
		int ano = Integer.parseInt(nascimento[0]);
		this.atual.setDtNascimento(new LocalDate(ano, mes, dia));
		this.atual.setCpf(cpf);
		this.atual.setPassaporte(passaporte);
		// Salvo o objeto Passageiro usando o DAO
		dao.atualizar(this.atual);
		// Termino o caso de uso
		this.terminar();
	}

	/**
	 * 
	 * @return
	 */
	public Status getStatus() {
		return this.status;
	}

	/**
	 * 
	 * @param novo
	 * @throws ControleException
	 */
	public void setStatus(Status novo) throws ControleException {
		Status.validarTransicaoStatus(this.status, novo);
		this.status = novo;
	}
}
