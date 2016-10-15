package control;

import model.Aeronave;
//import model.Departamento;
//import model.Empregado;
import model.ModeloAeronave;
import model.Poltrona;
import model.dao.DAO;
import model.dao.IDAO;
import model.util.DadosException;
import viewer.UIExcluirModeloAeronave;
import viewer.ViewerManager;
import control.util.ControleException;
import control.util.ErroDeControle;
import control.util.ICtrlCasoDeUso;

public class CtrlExcluirModeloAeronave implements ICtrlCasoDeUso {
	//
	// ATRIBUTOS
	//
	public enum Status {
		EXCLUINDO, ENCERRADO;

		public static void validarTransicaoStatus(Status anterior, Status novo) throws ControleException {
			if (anterior == null && novo == EXCLUINDO || anterior == EXCLUINDO && novo == ENCERRADO)
				return;
			throw new ControleException(new ErroDeControle("N�o se pode sair do estado "
					+ (anterior == null ? "NULO" : anterior) + " e ir para o estado " + novo));
		}
	}

	/**
	 * Refer�ncia para o controlador do caso de uso
	 */
	private final CtrlManterModelosAeronaves ctrlManterModelosAeronaves;

	/**
	 * Refer�ncia para o objeto a ser atualizado
	 */
	private ModeloAeronave atual;

	/**
	 * Refer�ncia para a janela ModeloAeronave que permitir� a exclus�o do
	 * ModeloAeronave
	 */
	private UIExcluirModeloAeronave uiExcluirModeloAeronave;

	/**
	 * Refer�ncia para o objeto DaoModeloAeronave
	 */
	private IDAO<ModeloAeronave> dao = (IDAO<ModeloAeronave>) DAO.getDAO(ModeloAeronave.class);

	/**
	 * Atributo que indica qual opera��o est� em curso
	 */
	private Status status;

	//
	// M�TODOS
	//

	/**
	 * Construtor da classe CtrlExcluirModeloAeronave
	 */
	public CtrlExcluirModeloAeronave(CtrlManterModelosAeronaves ctrl, ModeloAeronave m)
			throws ControleException, DadosException {
		// Guardo a refer�ncia para o controlador do programa
		this.ctrlManterModelosAeronaves = ctrl;
		// Guardo a refer�ncia para o objeto a ser alterado
		this.atual = m;
		// Iniciando o caso de uso
		this.iniciar();
	}

	/** 
	 * 
	 */
	public void iniciar() throws ControleException, DadosException {
		// Informo que o controlador de caso de uso est� excluindo
		this.setStatus(Status.EXCLUINDO);
		// Crio e abro a janela de exclus�o
		this.uiExcluirModeloAeronave = (UIExcluirModeloAeronave) ViewerManager.obterViewer(this,
				UIExcluirModeloAeronave.class);
		// Solicito � interface que atualize os campos
		this.uiExcluirModeloAeronave.atualizarCampos(this.atual.getDescricao(), this.atual.getCapacidade());
		// Solicito � interface que carregue os objetos
		this.uiExcluirModeloAeronave.exibir();
	}

	/** 
	 * 
	 */
	public void terminar() throws DadosException, ControleException {
		if (this.status == Status.ENCERRADO)
			return;
		// N�o h� ModeloAeronave em manipula��o
		this.atual = null;
		// Solicito o fechamento da janela
		this.uiExcluirModeloAeronave.fechar();
		// Informo que o controlador est� dispon�vel
		this.setStatus(Status.ENCERRADO);
		// Notifico ao controlador do programa o t�rmino deste caso de uso
		ctrlManterModelosAeronaves.terminarCasoDeUsoExcluirModeloAeronave();
	}

	/** 
	 * 
	 */
	public void cancelarExcluir() throws DadosException, ControleException {
		// Se o controlador n�o tinha ativado uma exclus�o, n�o fa�o nada!
		if (this.status != Status.EXCLUINDO)
			throw new ControleException(new ErroDeControle("N�o � poss�vel cancelar uma opera��o de exclus�o"));
		// Termino o caso de uso
		this.terminar();
	}

	/** 
	 * 
	 */
	public void excluir() throws DadosException, ControleException {
		// Se o controlador n�o tinha ativado uma exclus�o, n�o fa�o nada!
		if (this.status != Status.EXCLUINDO)
			throw new ControleException(new ErroDeControle("N�o � poss�vel concluir uma opera��o de exclus�o"));
		// Desaloco todas as aeronaves do ModeloAeronave
		for (Aeronave a : this.atual.getAeronaves())
			this.atual.removeAeronave(a);
		// Desaloco todas as poltronas do ModeloAeronave
		for (Poltrona p : this.atual.getPoltronas())
			this.atual.removePoltrona(p);
		// Salvo o objeto ModeloAeronave usando o DAO
		dao.remover(this.atual);
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
