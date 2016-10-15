package control;

import model.ModeloAeronave;
import model.dao.DAO;
import model.dao.IDAO;
import model.util.DadosException;
import viewer.UIModeloAeronave;
import viewer.ViewerManager;
import viewer.desktop.JanelaModeloAeronave;
import control.util.ControleException;
import control.util.ErroDeControle;
import control.util.ICtrlCasoDeUso;

public class CtrlIncluirModeloAeronave implements ICtrlCasoDeUso {
	//
	// ATRIBUTOS
	//
	public enum Status {
		INCLUINDO, ENCERRADO;

		public static void validarTransicaoStatus(Status anterior, Status novo) throws ControleException {
			if(anterior == null && novo == INCLUINDO || 
			   anterior == INCLUINDO && novo == ENCERRADO)
				return;
			throw new ControleException(new ErroDeControle("N�o se pode sair do estado " + (anterior==null?"NULO":anterior) + " e ir para o estado " + novo));
		}
	}
	
	/**
	 * Refer�ncia para o controlador do caso de uso Manter ModelosAeronaves.
	 */
	private final CtrlManterModelosAeronaves ctrlManterModelosAeronaves;

	/**
	 * Refer�ncia para a UI ModeloAeronave que permitir� a inclus�o e
	 * altera��o do ModeloAeronave
	 */
	private UIModeloAeronave uiModeloAeronave;

	/**
	 * Refer�ncia para o objeto ModeloAeronave sendo manipulado
	 */
	private ModeloAeronave atual;

	/**
	 * Refer�ncia para o objeto DaoModeloAeronave
	 */
	private IDAO<ModeloAeronave> dao = (IDAO<ModeloAeronave>)DAO.getDAO(ModeloAeronave.class);

	/**
	 * Atributo que indica qual opera��o est� em curso
	 */
	private Status status;

	//
	// M�TODOS
	//

	/**
	 * Construtor da classe CtrlIncluirModeloAeronave
	 */
	public CtrlIncluirModeloAeronave(CtrlManterModelosAeronaves ctrl) throws DadosException, ControleException {
		// Guardo a refer�ncia para o controlador do programa
		this.ctrlManterModelosAeronaves = ctrl;
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
		this.uiModeloAeronave = (UIModeloAeronave)ViewerManager.obterViewer(this, UIModeloAeronave.class);
		// Solicito � interface que carregue os objetos
		this.uiModeloAeronave.exibir();
	}

	/** 
	 * 
	 */
	public void terminar() throws DadosException, ControleException {
		if(this.status == Status.ENCERRADO)
			return;
		// N�o h� ModeloAeronave em manipula��o
		this.atual = null;
		// Fecho a UI
		this.uiModeloAeronave.fechar();
		// Informo que o controlador est� dispon�vel
		this.setStatus(Status.ENCERRADO);
		// Notifico ao controlador do programa o t�rmino deste caso de uso
		ctrlManterModelosAeronaves.terminarCasoDeUsoIncluirModeloAeronave();
	}

	/** 
	 * 
	 */
	public void cancelarIncluir() throws DadosException, ControleException {
		// Se o controlador n�o tinha ativado uma inclusao, n�o fa�o nada!
		if(this.status != Status.INCLUINDO)
			throw new ControleException(new ErroDeControle("N�o � poss�vel cancelar uma opera��o de inclus�o"));
		// Termino o caso de uso
		this.terminar();
	}

	/** 
	 * 
	 */
	public void incluir(String descricao, int capacidade) throws DadosException, ControleException {
		// Se o controlador n�o tinha ativado uma inclusao, n�o fa�o nada!
		if(this.status != Status.INCLUINDO)
			throw new ControleException(new ErroDeControle("N�o � poss�vel concluir uma opera��o de inclus�o"));
		// Crio um novo objeto ModeloAeronave
		this.atual = new ModeloAeronave(descricao, capacidade);
		// Salvo o objeto ModeloAeronave usando o DAO
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
		Status.validarTransicaoStatus(this.status,novo);
		this.status = novo;
	}
}
