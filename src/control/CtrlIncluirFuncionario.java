package control;

import org.joda.time.LocalDate;

import control.util.ControleException;
import control.util.ErroDeControle;
import control.util.ICtrlCasoDeUso;
import model.Funcionario;
import model.dao.DAO;
import model.dao.IDAO;
import model.util.DadosException;
import viewer.UIFuncionario;
import viewer.ViewerManager;

public class CtrlIncluirFuncionario implements ICtrlCasoDeUso{
	//
	// ATRIBUTOS
	//
	public enum Status {
		INCLUINDO, ENCERRADO;

		public static void validarTransicaoStatus(Status anterior, Status novo) throws ControleException {
			if (anterior == null && novo == INCLUINDO || anterior == INCLUINDO && novo == ENCERRADO)
				return;
			throw new ControleException(new ErroDeControle("N�o se pode sair do estado "
					+ (anterior == null ? "NULO" : anterior) + " e ir para o estado " + novo));
		}
	}

	/**
	 * Refer�ncia para o controlador do caso de uso Manter Funcionarios.
	 */
	private final CtrlManterFuncionarios ctrlManterFuncionarios;

	/**
	 * Refer�ncia para a UI Funcionarios que permitir� a inclus�o e altera��o do
	 * Funcionario
	 */
	private UIFuncionario uiFuncionario;

	/**
	 * Refer�ncia para o objeto Funcionario sendo manipulado
	 */
	private Funcionario atual;

	/**
	 * Refer�ncia para o objeto DaoFuncionario
	 */
	private IDAO<Funcionario> dao = (IDAO<Funcionario>) DAO.getDAO(Funcionario.class);

	/**
	 * Atributo que indica qual opera��o est� em curso
	 */
	private Status status;

	//
	// M�TODOS
	//

	/**
	 * Construtor da classe CtrlIncluirFuncionario
	 */
	public CtrlIncluirFuncionario(CtrlManterFuncionarios ctrl) throws DadosException, ControleException {
		// Guardo a refer�ncia para o controlador do programa
		this.ctrlManterFuncionarios = ctrl;
		// Iniciando o caso de uso
		this.iniciar();
	}

	/** 
	 * 
	 */
	public void iniciar() throws DadosException, ControleException {
		// Indico que o controlador de caso de uso est� incluindo
		this.setStatus(Status.INCLUINDO);
		// Crio e abro a janela de cadastro
		this.uiFuncionario = (UIFuncionario) ViewerManager.obterViewer(this, UIFuncionario.class);
		// Solicito � interface que carregue os objetos
		this.uiFuncionario.exibir();
	}

	/** 
	 * 
	 */
	public void terminar() throws DadosException, ControleException {
		if (this.status == Status.ENCERRADO)
			return;
		// N�o h� Funcionario em manipula��o
		this.atual = null;
		// Fecho a UI
		this.uiFuncionario.fechar();
		// Informo que o controlador est� dispon�vel
		this.setStatus(Status.ENCERRADO);
		// Notifico ao controlador do programa o t�rmino deste caso de uso
		ctrlManterFuncionarios.terminarCasoDeUsoIncluirFuncionario();
	}

	/** 
	 * 
	 */
	public void cancelarIncluir() throws DadosException, ControleException {
		// Se o controlador n�o tinha ativado uma inclusao, n�o fa�o nada!
		if (this.status != Status.INCLUINDO)
			throw new ControleException(new ErroDeControle("N�o � poss�vel cancelar uma opera��o de inclus�o"));
		// Termino o caso de uso
		this.terminar();
	}

	/** 
	 * 
	 */
	public void incluir(String login, String senha, String cpf)
			throws DadosException, ControleException {
		// Se o controlador n�o tinha ativado uma inclusao, n�o fa�o nada!
		if (this.status != Status.INCLUINDO)
			throw new ControleException(new ErroDeControle("N�o � poss�vel concluir uma opera��o de inclus�o"));
		// Crio um novo objeto Funcionario
		this.atual = new Funcionario(login, senha, cpf);
		// Salvo o objeto Funcionario usando o DAO
		dao.salvar(this.atual);
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
