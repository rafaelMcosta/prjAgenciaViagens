package control;

import java.util.List;

import control.util.ControleException;
import control.util.ErroDeControle;
import control.util.ICtrlCasoDeUso;
import model.Passageiro;
import model.dao.DAO;
import model.dao.IDAO;
import model.util.DadosException;
import model.util.IDadosParaTabela;
import viewer.UICadastroPassageiros;
import viewer.ViewerManager;

public class CtrlManterPassageiros implements ICtrlCasoDeUso{
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
			throw new ControleException(new ErroDeControle("Não se pode sair do estado "
					+ (anterior == null ? "NULO" : anterior) + " e ir para o estado " + novo));
		}
	}

	/**
	 * Referência para o controlador do programa.
	 */
	private final CtrlSessaoUsuario ctrlPrg;

	/**
	 * Referência para o controlador do caso de uso Incluir Passageiro.
	 */
	private CtrlIncluirPassageiro ctrlIncluirPassageiro;

	/**
	 * Referência para o controlador do caso de uso Alterar Passageiro.
	 */
	private CtrlAlterarPassageiro ctrlAlterarPassageiro;

	/**
	 * Referência para o controlador do caso de uso Excluir Passageiro.
	 */
	private CtrlExcluirPassageiro ctrlExcluirPassageiro;

	/**
	 * Referência para a janela do cadastro de Passageiro
	 */
	private UICadastroPassageiros uiCadastro;

	/**
	 * Referência para o objeto DaoPassageiro
	 */
	private IDAO<Passageiro> dao = (IDAO<Passageiro>) DAO.getDAO(Passageiro.class);

	/**
	 * Atributo que indica qual operação está em curso
	 */
	private Status status;

	//
	// MÉTODOS
	//

	/**
	 * Construtor da classe CtrlManterPassageiros
	 */
	public CtrlManterPassageiros(CtrlSessaoUsuario p) throws ControleException, DadosException {
		// Guardo a referência para o controlador do programa
		this.ctrlPrg = p;
		// Iniciando o caso de uso
		this.iniciar();
	}

	/**
	 * Inicia o caso de uso "Manter Passageiros"
	 */
	public void iniciar() throws ControleException, DadosException {
		// Recupero os objetos Passageiro do DAO
		this.dao = (IDAO<Passageiro>) DAO.getDAO(Passageiro.class);
		List<IDadosParaTabela> passageiros = dao.getListaObjs();
		// Crio e abro a janela de cadastro
		this.uiCadastro = (UICadastroPassageiros) ViewerManager.obterViewer(this, UICadastroPassageiros.class);
		// Solicito à interface que carregue os objetos
		this.uiCadastro.exibirObjetos(passageiros);
		// Solicito à interface que carregue os objetos
		this.uiCadastro.exibir();
		// Informo que o controlador de caso de uso está disponível
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
		// Informo que o controlador está encerrado
		this.setStatus(Status.ENCERRADO);
		// Notifico ao controlador do programa o término deste caso de uso
		this.ctrlPrg.terminarCasoDeUsoManterPassageiros();
	}

	/** 
	 * 
	 */
	public void iniciarCasoDeUsoIncluirPassageiro() throws DadosException, ControleException {
		// Indico que o controlador de caso de uso está incluindo
		this.setStatus(Status.INCLUINDO);
		// Abro a janela de passageiro
		this.ctrlIncluirPassageiro = new CtrlIncluirPassageiro(this);
	}

	/**
	 *  
	 */
	public void terminarCasoDeUsoIncluirPassageiro() throws DadosException, ControleException {
		if (this.ctrlIncluirPassageiro != null)
			this.ctrlIncluirPassageiro.terminar();
		this.ctrlIncluirPassageiro = null;
		// Indico que o controlador de caso de uso está disponível
		this.setStatus(Status.DISPONIVEL);
		// Recupero os objetos ModeloAeronave do DAO
		List<IDadosParaTabela> passageiros = dao.getListaObjs();
		// Solicito a atualização da interface após as ações de inclusão
		this.uiCadastro.exibirObjetos(passageiros);
		this.uiCadastro.exibir();
	}

	/** 
	 * 
	 */
	public void iniciarCasoDeUsoAlterarPassageiro(IDadosParaTabela selecionado)
			throws DadosException, ControleException {
		// Indico que o controlador de caso de uso está incluindo
		this.setStatus(Status.ALTERANDO);
		// Promovo o casting
		Passageiro p = (Passageiro) selecionado;
		// Abro a janela de Passageiro
		this.ctrlAlterarPassageiro = new CtrlAlterarPassageiro(this, p);
	}

	/**
	 *  
	 */
	public void terminarCasoDeUsoAlterarPassageiro() throws DadosException, ControleException {
		if (this.ctrlAlterarPassageiro != null)
			this.ctrlAlterarPassageiro.terminar();
		this.ctrlAlterarPassageiro = null;
		// Indico que o controlador de caso de uso está disponível
		this.setStatus(Status.DISPONIVEL);
		// Recupero os objetos Passageiro do DAO
		List<IDadosParaTabela> passageiros = dao.getListaObjs();
		// Solicito a atualização da interface após as ações de inclusão
		this.uiCadastro.exibirObjetos(passageiros);
		this.uiCadastro.exibir();
	}

	/** 
	 * 
	 */
	public void iniciarCasoDeUsoExcluirPassageiro(IDadosParaTabela selecionado)
			throws DadosException, ControleException {
		// Indico que o controlador de caso de uso está incluindo
		this.setStatus(Status.EXCLUINDO);
		// Promovo o casting
		Passageiro p = (Passageiro) selecionado;
		// Abro a janela de Passageiro
		this.ctrlExcluirPassageiro = new CtrlExcluirPassageiro(this, p);
	}

	/**
	 *  
	 */
	public void terminarCasoDeUsoExcluirPassageiro() throws DadosException, ControleException {
		if (this.ctrlExcluirPassageiro != null)
			this.ctrlExcluirPassageiro.terminar();
		this.ctrlExcluirPassageiro = null;
		// Indico que o controlador de caso de uso está disponível
		this.setStatus(Status.DISPONIVEL);
		// Recupero os objetos Passageiro do DAO
		List<IDadosParaTabela> passageiros = dao.getListaObjs();
		// Solicito a atualização da interface após as ações de inclusão
		this.uiCadastro.exibirObjetos(passageiros);
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
