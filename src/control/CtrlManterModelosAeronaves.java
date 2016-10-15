package control;

import java.util.List;

import control.util.ControleException;
import control.util.ErroDeControle;
import control.util.ICtrlCasoDeUso;
import model.ModeloAeronave;
import model.dao.DAO;
import model.dao.IDAO;
import model.util.DadosException;
import model.util.IDadosParaTabela;
import viewer.UICadastroModelosAeronaves;
import viewer.ViewerManager;

public class CtrlManterModelosAeronaves implements ICtrlCasoDeUso {
	//
	// ATRIBUTOS
	//
	public enum Status {
		DISPONIVEL, INCLUINDO, EXCLUINDO, ALTERANDO, ENCERRADO;

		public static void validarTransicaoStatus(Status anterior, Status novo) throws ControleException {
			if (anterior == null && novo == DISPONIVEL || anterior == DISPONIVEL && novo == INCLUINDO
					|| anterior == DISPONIVEL && novo == EXCLUINDO || anterior == DISPONIVEL && novo == ALTERANDO
					|| anterior == DISPONIVEL && novo == ENCERRADO || anterior == INCLUINDO && novo == DISPONIVEL
					|| anterior == EXCLUINDO && novo == DISPONIVEL || anterior == ALTERANDO && novo == DISPONIVEL)
				return;
			throw new ControleException(new ErroDeControle("N�o se pode sair do estado "
					+ (anterior == null ? "NULO" : anterior) + " e ir para o estado " + novo));
		}
	}

	/**
	 * Refer�ncia para o controlador do programa.
	 */
	private final CtrlSessaoUsuario ctrlPrg;

	/**
	 * Refer�ncia para o controlador do caso de uso Incluir ModeloAeronave.
	 */
	private CtrlIncluirModeloAeronave ctrlIncluirModeloAeronave;

	/**
	 * Refer�ncia para o controlador do caso de uso Alterar ModeloAeronave.
	 */
	private CtrlAlterarModeloAeronave ctrlAlterarModeloAeronave;

	/**
	 * Refer�ncia para o controlador do caso de uso Alterar ModeloAeronave.
	 */
	private CtrlExcluirModeloAeronave ctrlExcluirModeloAeronave;

	/**
	 * Refer�ncia para a janela do cadastro de ModeloAeronave
	 */
	private UICadastroModelosAeronaves uiCadastro;

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
	 * Construtor da classe CtrlManterModeloAeronave
	 */
	public CtrlManterModelosAeronaves(CtrlSessaoUsuario p) throws ControleException, DadosException {
		// Guardo a refer�ncia para o controlador do programa
		this.ctrlPrg = p;
		// Iniciando o caso de uso
		this.iniciar();
	}

	/**
	 * Inicia o caso de uso "Manter ModelosAeronaves"
	 */
	public void iniciar() throws ControleException, DadosException {
		// Recupero os objetos ModeloAeronave do DAO
		this.dao = (IDAO<ModeloAeronave>) DAO.getDAO(ModeloAeronave.class);
		List<IDadosParaTabela> modelos = dao.getListaObjs();
		// Crio e abro a janela de cadastro
		this.uiCadastro = (UICadastroModelosAeronaves) ViewerManager.obterViewer(this, UICadastroModelosAeronaves.class);
		// Solicito � interface que carregue os objetos
		this.uiCadastro.exibirObjetos(modelos);
		// Solicito � interface que carregue os objetos
		this.uiCadastro.exibir();
		// Informo que o controlador de caso de uso est� dispon�vel
		this.setStatus(Status.DISPONIVEL);
	}

	/** 
	 * 
	 */
	public void terminar() throws ControleException {
		if (this.status == Status.ENCERRADO)
			return;
		// Fecho a janela
		this.uiCadastro.fechar();
		// Informo que o controlador est� encerrado
		this.setStatus(Status.ENCERRADO);
		// Notifico ao controlador do programa o t�rmino deste caso de uso
		this.ctrlPrg.terminarCasoDeUsoManterModelosAeronaves();
	}

	/** 
	 * 
	 */
	public void iniciarCasoDeUsoIncluirModeloAeronave() throws DadosException, ControleException {
		// Indico que o controlador de caso de uso est� incluindo
		this.setStatus(Status.INCLUINDO);
		// Abro a janela de departamento
		this.ctrlIncluirModeloAeronave = new CtrlIncluirModeloAeronave(this);
	}

	/**
	 *  
	 */
	public void terminarCasoDeUsoIncluirModeloAeronave() throws DadosException, ControleException {
		if (this.ctrlIncluirModeloAeronave != null)
			this.ctrlIncluirModeloAeronave.terminar();
		this.ctrlIncluirModeloAeronave = null;
		// Indico que o controlador de caso de uso est� dispon�vel
		this.setStatus(Status.DISPONIVEL);
		// Recupero os objetos ModeloAeronave do DAO
		List<IDadosParaTabela> modelos = dao.getListaObjs();
		// Solicito a atualiza��o da interface ap�s as a��es de inclus�o
		this.uiCadastro.exibirObjetos(modelos);
		this.uiCadastro.exibir();
	}

	/** 
	 * 
	 */
	public void iniciarCasoDeUsoAlterarModeloAeronave(IDadosParaTabela selecionado)
			throws DadosException, ControleException {
		// Indico que o controlador de caso de uso est� incluindo
		this.setStatus(Status.ALTERANDO);
		// Promovo o casting
		ModeloAeronave m = (ModeloAeronave) selecionado;
		// Abro a janela de ModeloAeronave
		this.ctrlAlterarModeloAeronave = new CtrlAlterarModeloAeronave(this, m);
	}

	/**
	 *  
	 */
	public void terminarCasoDeUsoAlterarModeloAeronave() throws DadosException, ControleException {
		if (this.ctrlAlterarModeloAeronave != null)
			this.ctrlAlterarModeloAeronave.terminar();
		this.ctrlAlterarModeloAeronave = null;
		// Indico que o controlador de caso de uso est� dispon�vel
		this.setStatus(Status.DISPONIVEL);
		// Recupero os objetos ModeloAeronave do DAO
		List<IDadosParaTabela> modelos = dao.getListaObjs();
		// Solicito a atualiza��o da interface ap�s as a��es de inclus�o
		this.uiCadastro.exibirObjetos(modelos);
		this.uiCadastro.exibir();
	}

	/** 
	 * 
	 */
	public void iniciarCasoDeUsoExcluirModeloAeronave(IDadosParaTabela selecionado)
			throws DadosException, ControleException {
		// Indico que o controlador de caso de uso est� incluindo
		this.setStatus(Status.EXCLUINDO);
		// Promovo o casting
		ModeloAeronave m = (ModeloAeronave) selecionado;
		// Abro a janela de departamento
		this.ctrlExcluirModeloAeronave = new CtrlExcluirModeloAeronave(this, m);
	}

	/**
	 *  
	 */
	public void terminarCasoDeUsoExcluirModeloAeronave() throws DadosException, ControleException {
		if (this.ctrlExcluirModeloAeronave != null)
			this.ctrlExcluirModeloAeronave.terminar();
		this.ctrlExcluirModeloAeronave = null;
		// Indico que o controlador de caso de uso est� dispon�vel
		this.setStatus(Status.DISPONIVEL);
		// Recupero os objetos Departamento do DAO
		List<IDadosParaTabela> modelos = dao.getListaObjs();
		// Solicito a atualiza��o da interface ap�s as a��es de inclus�o
		this.uiCadastro.exibirObjetos(modelos);
		this.uiCadastro.exibir();
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
