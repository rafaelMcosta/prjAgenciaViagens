package control;

import control.util.ControleException;
import control.util.ErroDeControle;
import control.util.ICtrlCasoDeUso;
//import model.Departamento;
//import model.Empregado.Status;
import model.ModeloAeronave;
import model.dao.DAO;
import model.dao.IDAO;
import model.util.DadosException;
import model.util.ErroDeDominio;
import viewer.UICadastroModelosAeronaves;
import viewer.UIModeloAeronave;
import viewer.ViewerManager;
//import viewer.desktop.DialogExcluirModeloAeronave;
import viewer.desktop.JanelaCadastroModelosAeronaves;
import viewer.desktop.JanelaModeloAeronave;

public class CtrlAlterarModeloAeronave implements ICtrlCasoDeUso {
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
	private final CtrlManterModelosAeronaves ctrlManterModelosAeronaves;

	/**
	 * Refer�ncia para o objeto a ser atualizado
	 */
	private ModeloAeronave atual;

	/**
	 * Refer�ncia para a janela ModeloAeronave que permitir� a inclus�o e
	 * altera��o do ModeloAeronave
	 */
	private UIModeloAeronave uiModeloAeronave;

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
	 * Construtor da classe CtrlAlterarModeloAeronave
	 */
	public CtrlAlterarModeloAeronave(CtrlManterModelosAeronaves ctrl, ModeloAeronave m)
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
		// Informo que o controlador de caso de uso est� dispon�vel
		this.setStatus(Status.ALTERANDO);
		// Crio e abro a janela de altera��o
		this.uiModeloAeronave = (UIModeloAeronave) ViewerManager.obterViewer(this, UIModeloAeronave.class);
		// Solicito � interface que atualize os campos
		this.uiModeloAeronave.atualizarCampos(this.atual.getDescricao(), this.atual.getCapacidade());
		// Solicito � interface que carregue os objetos
		this.uiModeloAeronave.exibir();
	}

	/** 
	 * 
	 */
	public void terminar() throws DadosException, ControleException {
		if (this.status == Status.ENCERRADO)
			return;
		// N�o h� ModeloAeronave em manipula��o
		this.atual = null;
		// Fecho a janela
		this.uiModeloAeronave.fechar();
		// Informo que o controlador est� dispon�vel
		this.setStatus(Status.ENCERRADO);
		// Notifico ao controlador do programa o t�rmino deste caso de uso
		ctrlManterModelosAeronaves.terminarCasoDeUsoAlterarModeloAeronave();
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
	public void alterar(String descricao, int capacidade) throws DadosException, ControleException {
		// Se o controlador n�o tinha ativado uma altera��o, n�o fa�o nada!
		if (this.status != Status.ALTERANDO)
			throw new ControleException(new ErroDeControle("N�o � poss�vel concluir uma opera��o de altera��o"));
		// Atualizo os campos
		this.atual.setDescricao(descricao);
		this.atual.setCapacidade(capacidade);
		// Salvo o objeto ModeloAeronave usando o DAO
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
