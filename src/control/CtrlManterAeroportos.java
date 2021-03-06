package control;

import java.util.List;

import control.util.ControleException;
import control.util.ErroDeControle;
import control.util.ICtrlCasoDeUso;
import model.Aeroporto;
import model.dao.DAO;
import model.dao.IDAO;
import model.util.DadosException;
import model.util.IDadosParaTabela;
import viewer.UICadastroAeroportos;
import viewer.ViewerManager;

public class CtrlManterAeroportos implements ICtrlCasoDeUso{
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
	 * Refer�ncia para o controlador do caso de uso Incluir Aeroporto.
	 */
	private CtrlIncluirAeroporto ctrlIncluirAeroporto;

	/**
	 * Refer�ncia para o controlador do caso de uso Alterar Aeroporto.
	 */
	private CtrlAlterarAeroporto ctrlAlterarAeroporto;

	/**
	 * Refer�ncia para o controlador do caso de uso Excluir Aeroporto.
	 */
	private CtrlExcluirAeroporto ctrlExcluirAeroporto;

	/**
	 * Refer�ncia para a janela do cadastro de Aeroporto
	 */
	private UICadastroAeroportos uiCadastro;

	/**
	 * Refer�ncia para o objeto DaoAeroporto
	 */
	private IDAO<Aeroporto> dao = (IDAO<Aeroporto>) DAO.getDAO(Aeroporto.class);

	/**
	 * Atributo que indica qual opera��o est� em curso
	 */
	private Status status;

	//
	// M�TODOS
	//

	/**
	 * Construtor da classe CtrlManterAeroportos
	 */
	public CtrlManterAeroportos(CtrlSessaoUsuario p) throws ControleException, DadosException {
		// Guardo a refer�ncia para o controlador do programa
		this.ctrlPrg = p;
		// Iniciando o caso de uso
		this.iniciar();
	}

	/**
	 * Inicia o caso de uso "Manter Aeroportos"
	 */
	public void iniciar() throws ControleException, DadosException {
		// Recupero os objetos Passageiro do DAO
		this.dao = (IDAO<Aeroporto>) DAO.getDAO(Aeroporto.class);
		List<IDadosParaTabela> aeroportos = dao.getListaObjs();
		// Crio e abro a janela de cadastro
		this.uiCadastro = (UICadastroAeroportos) ViewerManager.obterViewer(this, UICadastroAeroportos.class);
		// Solicito � interface que carregue os objetos
		this.uiCadastro.exibirObjetos(aeroportos);
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
		this.ctrlPrg.terminarCasoDeUsoManterAeroportos();
	}

	/** 
	 * 
	 */
	public void iniciarCasoDeUsoIncluirAeroporto() throws DadosException, ControleException {
		// Indico que o controlador de caso de uso est� incluindo
		this.setStatus(Status.INCLUINDO);
		// Abro a janela de passageiro
		this.ctrlIncluirAeroporto = new CtrlIncluirAeroporto(this);
	}

	/**
	 *  
	 */
	public void terminarCasoDeUsoIncluirAeroporto() throws DadosException, ControleException {
		if (this.ctrlIncluirAeroporto != null)
			this.ctrlIncluirAeroporto.terminar();
		this.ctrlIncluirAeroporto = null;
		// Indico que o controlador de caso de uso est� dispon�vel
		this.setStatus(Status.DISPONIVEL);
		// Recupero os objetos ModeloAeronave do DAO
		List<IDadosParaTabela> aeroportos = dao.getListaObjs();
		// Solicito a atualiza��o da interface ap�s as a��es de inclus�o
		this.uiCadastro.exibirObjetos(aeroportos);
		this.uiCadastro.exibir();
	}

	/** 
	 * 
	 */
	public void iniciarCasoDeUsoAlterarAeroporto(IDadosParaTabela selecionado)
			throws DadosException, ControleException {
		// Indico que o controlador de caso de uso est� incluindo
		this.setStatus(Status.ALTERANDO);
		// Promovo o casting
		Aeroporto a = (Aeroporto) selecionado;
		// Abro a janela de Passageiro
		this.ctrlAlterarAeroporto = new CtrlAlterarAeroporto(this, a);
	}

	/**
	 *  
	 */
	public void terminarCasoDeUsoAlterarAeroporto() throws DadosException, ControleException {
		if (this.ctrlAlterarAeroporto != null)
			this.ctrlAlterarAeroporto.terminar();
		this.ctrlAlterarAeroporto = null;
		// Indico que o controlador de caso de uso est� dispon�vel
		this.setStatus(Status.DISPONIVEL);
		// Recupero os objetos Passageiro do DAO
		List<IDadosParaTabela> aeroportos = dao.getListaObjs();
		// Solicito a atualiza��o da interface ap�s as a��es de inclus�o
		this.uiCadastro.exibirObjetos(aeroportos);
		this.uiCadastro.exibir();
	}

	/** 
	 * 
	 */
	public void iniciarCasoDeUsoExcluirAeroporto(IDadosParaTabela selecionado)
			throws DadosException, ControleException {
		// Indico que o controlador de caso de uso est� incluindo
		this.setStatus(Status.EXCLUINDO);
		// Promovo o casting
		Aeroporto a = (Aeroporto) selecionado;
		// Abro a janela de Passageiro
		this.ctrlExcluirAeroporto = new CtrlExcluirAeroporto(this, a);
	}

	/**
	 *  
	 */
	public void terminarCasoDeUsoExcluirAeroporto() throws DadosException, ControleException {
		if (this.ctrlExcluirAeroporto != null)
			this.ctrlExcluirAeroporto.terminar();
		this.ctrlExcluirAeroporto = null;
		// Indico que o controlador de caso de uso est� dispon�vel
		this.setStatus(Status.DISPONIVEL);
		// Recupero os objetos Passageiro do DAO
		List<IDadosParaTabela> aeroportos = dao.getListaObjs();
		// Solicito a atualiza��o da interface ap�s as a��es de inclus�o
		this.uiCadastro.exibirObjetos(aeroportos);
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
