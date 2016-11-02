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

public class CtrlAlterarFuncionario implements ICtrlCasoDeUso{

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
	private final CtrlManterFuncionarios ctrlManterFuncionarios;

	/**
	 * Refer�ncia para o objeto a ser atualizado
	 */
	private Funcionario atual;

	/**
	 * Refer�ncia para a janela Passageiro que permitir� a inclus�o e altera��o
	 * do Funcionario
	 */
	private UIFuncionario uiFuncionario;

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
	 * Construtor da classe CtrlAlterarFuncionario
	 */
	public CtrlAlterarFuncionario(CtrlManterFuncionarios ctrl, Funcionario f) throws ControleException, DadosException {
		// Guardo a refer�ncia para o controlador do programa
		this.ctrlManterFuncionarios = ctrl;
		// Guardo a refer�ncia para o objeto a ser alterado
		this.atual = f;
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
		this.uiFuncionario = (UIFuncionario) ViewerManager.obterViewer(this, UIFuncionario.class);
		// Solicito � interface que atualize os campos
		this.uiFuncionario.atualizarCampos(this.atual.getLogin(), this.atual.getSenha(), this.atual.getCpf());
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
		// Fecho a janela
		this.uiFuncionario.fechar();
		// Informo que o controlador est� dispon�vel
		this.setStatus(Status.ENCERRADO);
		// Notifico ao controlador do programa o t�rmino deste caso de uso
		ctrlManterFuncionarios.terminarCasoDeUsoAlterarFuncionario();
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
	public void alterar(String login, String senha, String cpf)
			throws DadosException, ControleException {
		// Se o controlador n�o tinha ativado uma altera��o, n�o fa�o nada!
		if (this.status != Status.ALTERANDO)
			throw new ControleException(new ErroDeControle("N�o � poss�vel concluir uma opera��o de altera��o"));
		// Atualizo os campos
		this.atual.setLogin(login);
		this.atual.setSenha(senha);
		this.atual.setCpf(cpf);
		// Salvo o objeto Funcionario usando o DAO
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
