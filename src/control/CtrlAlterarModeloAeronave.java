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
			throw new ControleException(new ErroDeControle("Não se pode sair do estado "
					+ (anterior == null ? "NULO" : anterior) + " e ir para o estado " + novo));
		}
	}

	/**
	 * Referência para o controlador do caso de uso manter.
	 */
	private final CtrlManterModelosAeronaves ctrlManterModelosAeronaves;

	/**
	 * Referência para o objeto a ser atualizado
	 */
	private ModeloAeronave atual;

	/**
	 * Referência para a janela ModeloAeronave que permitirá a inclusão e
	 * alteração do ModeloAeronave
	 */
	private UIModeloAeronave uiModeloAeronave;

	/**
	 * Referência para o objeto DaoModeloAeronave
	 */
	private IDAO<ModeloAeronave> dao = (IDAO<ModeloAeronave>) DAO.getDAO(ModeloAeronave.class);

	/**
	 * Atributo que indica qual operação está em curso
	 */
	private Status status;

	//
	// MÉTODOS
	//

	/**
	 * Construtor da classe CtrlAlterarModeloAeronave
	 */
	public CtrlAlterarModeloAeronave(CtrlManterModelosAeronaves ctrl, ModeloAeronave m)
			throws ControleException, DadosException {
		// Guardo a referência para o controlador do programa
		this.ctrlManterModelosAeronaves = ctrl;
		// Guardo a referência para o objeto a ser alterado
		this.atual = m;
		// Iniciando o caso de uso
		this.iniciar();
	}

	/** 
	 * 
	 */
	public void iniciar() throws ControleException, DadosException {
		// Informo que o controlador de caso de uso está disponível
		this.setStatus(Status.ALTERANDO);
		// Crio e abro a janela de alteração
		this.uiModeloAeronave = (UIModeloAeronave) ViewerManager.obterViewer(this, UIModeloAeronave.class);
		// Solicito à interface que atualize os campos
		this.uiModeloAeronave.atualizarCampos(this.atual.getDescricao(), this.atual.getCapacidade());
		// Solicito à interface que carregue os objetos
		this.uiModeloAeronave.exibir();
	}

	/** 
	 * 
	 */
	public void terminar() throws DadosException, ControleException {
		if (this.status == Status.ENCERRADO)
			return;
		// Não há ModeloAeronave em manipulação
		this.atual = null;
		// Fecho a janela
		this.uiModeloAeronave.fechar();
		// Informo que o controlador está disponível
		this.setStatus(Status.ENCERRADO);
		// Notifico ao controlador do programa o término deste caso de uso
		ctrlManterModelosAeronaves.terminarCasoDeUsoAlterarModeloAeronave();
	}

	/** 
	 * 
	 */
	public void cancelarAlterar() throws DadosException, ControleException {
		// Se o controlador não tinha ativado uma alteração, não faço nada!
		if (this.status != Status.ALTERANDO)
			throw new ControleException(new ErroDeControle("Não é possível cancelar uma operação de alteração"));
		// Termino o caso de uso
		this.terminar();
	}

	/** 
	 * 
	 */
	public void alterar(String descricao, int capacidade) throws DadosException, ControleException {
		// Se o controlador não tinha ativado uma alteração, não faço nada!
		if (this.status != Status.ALTERANDO)
			throw new ControleException(new ErroDeControle("Não é possível concluir uma operação de alteração"));
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
