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
			throw new ControleException(new ErroDeControle("Não se pode sair do estado " + (anterior==null?"NULO":anterior) + " e ir para o estado " + novo));
		}
	}
	
	/**
	 * Referência para o controlador do caso de uso Manter ModelosAeronaves.
	 */
	private final CtrlManterModelosAeronaves ctrlManterModelosAeronaves;

	/**
	 * Referência para a UI ModeloAeronave que permitirá a inclusão e
	 * alteração do ModeloAeronave
	 */
	private UIModeloAeronave uiModeloAeronave;

	/**
	 * Referência para o objeto ModeloAeronave sendo manipulado
	 */
	private ModeloAeronave atual;

	/**
	 * Referência para o objeto DaoModeloAeronave
	 */
	private IDAO<ModeloAeronave> dao = (IDAO<ModeloAeronave>)DAO.getDAO(ModeloAeronave.class);

	/**
	 * Atributo que indica qual operação está em curso
	 */
	private Status status;

	//
	// MÉTODOS
	//

	/**
	 * Construtor da classe CtrlIncluirModeloAeronave
	 */
	public CtrlIncluirModeloAeronave(CtrlManterModelosAeronaves ctrl) throws DadosException, ControleException {
		// Guardo a referência para o controlador do programa
		this.ctrlManterModelosAeronaves = ctrl;
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
		this.uiModeloAeronave = (UIModeloAeronave)ViewerManager.obterViewer(this, UIModeloAeronave.class);
		// Solicito à interface que carregue os objetos
		this.uiModeloAeronave.exibir();
	}

	/** 
	 * 
	 */
	public void terminar() throws DadosException, ControleException {
		if(this.status == Status.ENCERRADO)
			return;
		// Não há ModeloAeronave em manipulação
		this.atual = null;
		// Fecho a UI
		this.uiModeloAeronave.fechar();
		// Informo que o controlador está disponível
		this.setStatus(Status.ENCERRADO);
		// Notifico ao controlador do programa o término deste caso de uso
		ctrlManterModelosAeronaves.terminarCasoDeUsoIncluirModeloAeronave();
	}

	/** 
	 * 
	 */
	public void cancelarIncluir() throws DadosException, ControleException {
		// Se o controlador não tinha ativado uma inclusao, não faço nada!
		if(this.status != Status.INCLUINDO)
			throw new ControleException(new ErroDeControle("Não é possível cancelar uma operação de inclusão"));
		// Termino o caso de uso
		this.terminar();
	}

	/** 
	 * 
	 */
	public void incluir(String descricao, int capacidade) throws DadosException, ControleException {
		// Se o controlador não tinha ativado uma inclusao, não faço nada!
		if(this.status != Status.INCLUINDO)
			throw new ControleException(new ErroDeControle("Não é possível concluir uma operação de inclusão"));
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
