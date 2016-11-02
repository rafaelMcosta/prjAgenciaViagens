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
			throw new ControleException(new ErroDeControle("Não se pode sair do estado "
					+ (anterior == null ? "NULO" : anterior) + " e ir para o estado " + novo));
		}
	}

	/**
	 * Referência para o controlador do caso de uso Manter Funcionarios.
	 */
	private final CtrlManterFuncionarios ctrlManterFuncionarios;

	/**
	 * Referência para a UI Funcionarios que permitirá a inclusão e alteração do
	 * Funcionario
	 */
	private UIFuncionario uiFuncionario;

	/**
	 * Referência para o objeto Funcionario sendo manipulado
	 */
	private Funcionario atual;

	/**
	 * Referência para o objeto DaoFuncionario
	 */
	private IDAO<Funcionario> dao = (IDAO<Funcionario>) DAO.getDAO(Funcionario.class);

	/**
	 * Atributo que indica qual operação está em curso
	 */
	private Status status;

	//
	// MÉTODOS
	//

	/**
	 * Construtor da classe CtrlIncluirFuncionario
	 */
	public CtrlIncluirFuncionario(CtrlManterFuncionarios ctrl) throws DadosException, ControleException {
		// Guardo a referência para o controlador do programa
		this.ctrlManterFuncionarios = ctrl;
		// Iniciando o caso de uso
		this.iniciar();
	}

	/** 
	 * 
	 */
	public void iniciar() throws DadosException, ControleException {
		// Indico que o controlador de caso de uso está incluindo
		this.setStatus(Status.INCLUINDO);
		// Crio e abro a janela de cadastro
		this.uiFuncionario = (UIFuncionario) ViewerManager.obterViewer(this, UIFuncionario.class);
		// Solicito à interface que carregue os objetos
		this.uiFuncionario.exibir();
	}

	/** 
	 * 
	 */
	public void terminar() throws DadosException, ControleException {
		if (this.status == Status.ENCERRADO)
			return;
		// Não há Funcionario em manipulação
		this.atual = null;
		// Fecho a UI
		this.uiFuncionario.fechar();
		// Informo que o controlador está disponível
		this.setStatus(Status.ENCERRADO);
		// Notifico ao controlador do programa o término deste caso de uso
		ctrlManterFuncionarios.terminarCasoDeUsoIncluirFuncionario();
	}

	/** 
	 * 
	 */
	public void cancelarIncluir() throws DadosException, ControleException {
		// Se o controlador não tinha ativado uma inclusao, não faço nada!
		if (this.status != Status.INCLUINDO)
			throw new ControleException(new ErroDeControle("Não é possível cancelar uma operação de inclusão"));
		// Termino o caso de uso
		this.terminar();
	}

	/** 
	 * 
	 */
	public void incluir(String login, String senha, String cpf)
			throws DadosException, ControleException {
		// Se o controlador não tinha ativado uma inclusao, não faço nada!
		if (this.status != Status.INCLUINDO)
			throw new ControleException(new ErroDeControle("Não é possível concluir uma operação de inclusão"));
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
