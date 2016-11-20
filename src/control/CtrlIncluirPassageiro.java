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

public class CtrlIncluirPassageiro implements ICtrlCasoDeUso {
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
	 * Refer�ncia para o controlador do caso de uso Manter Passageiros.
	 */
	private final CtrlManterPassageiros ctrlManterPassageiros;

	/**
	 * Refer�ncia para a UI Passageiro que permitir� a inclus�o e altera��o do
	 * Passageiro
	 */
	private UIPassageiro uiPassageiro;

	/**
	 * Refer�ncia para o objeto Passageiro sendo manipulado
	 */
	private Passageiro atual;

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
	 * Construtor da classe CtrlIncluirPassageiro
	 */
	public CtrlIncluirPassageiro(CtrlManterPassageiros ctrl) throws DadosException, ControleException {
		// Guardo a refer�ncia para o controlador do programa
		this.ctrlManterPassageiros = ctrl;
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
		this.uiPassageiro = (UIPassageiro) ViewerManager.obterViewer(this, UIPassageiro.class);
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
		// Fecho a UI
		this.uiPassageiro.fechar();
		// Informo que o controlador est� dispon�vel
		this.setStatus(Status.ENCERRADO);
		// Notifico ao controlador do programa o t�rmino deste caso de uso
		ctrlManterPassageiros.terminarCasoDeUsoIncluirPassageiro();
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
	public void incluir(String nome, LocalDate dtNascimento, String cpf, String passaporte)
			throws DadosException, ControleException {
		// Se o controlador n�o tinha ativado uma inclusao, n�o fa�o nada!
		if (this.status != Status.INCLUINDO)
			throw new ControleException(new ErroDeControle("N�o � poss�vel concluir uma opera��o de inclus�o"));
		// Crio um novo objeto Passageiro
		this.atual = new Passageiro(nome, dtNascimento, cpf, passaporte);
		// Salvo o objeto Passageiro usando o DAO
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
