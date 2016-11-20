package control;

import model.Funcionario;
import model.dao.DAO;
import model.dao.IDAO;
import model.util.DadosException;
import viewer.UILogin;
import viewer.UIPrincipal;
import viewer.ViewerManager;
import viewer.desktop.JanelaPrincipal;

import java.util.List;

import control.util.ControleException;
import control.util.ICtrlCasoDeUso;

/**
 * Este � o controlador que gerencia a execu��o do meu programa.
 * 
 * @author Alessandro Cerqueira
 */
public class CtrlSessaoUsuario implements ICtrlCasoDeUso {
	//
	// ATRIBUTOS
	// ---------
	// O controlador do programa deve ter um atributo para
	// cada controlador de caso de uso do programa.
	//
	// Tamb�m o controlador do programa deve ter um atributo
	// para referenciar a janela principal do programa.
	//

	/**
	 * Refer�ncia para o controlador do caso de uso "Manter ModelosAeronaves"
	 */
	private CtrlManterModelosAeronaves ctrlModelosAeronaves;
	/**
	 * Refer�ncia para o controlador do caso de uso "Manter Passageiros"
	 */
	private CtrlManterPassageiros ctrlPassageiros;
	/**
	 * Refer�ncia para o controlador do caso de uso "Manter Aeroportos"
	 */
	private CtrlManterAeroportos ctrlAeroportos;
	/**
	 * Refer�ncia para o controlador do caso de uso "Manter Funcionarios"
	 */
	private CtrlManterFuncionarios ctrlFuncionarios;
	/**
	 * Refer�ncia para o controlador do caso de uso "Manter PlanosVoos"
	 */
	private CtrlManterPlanosVoos ctrlPlanosVoos;
	/**
	 * Refer�ncia para o funcionario(usuario) que far� login no sistema
	 */
	private Funcionario funcionario;

	/**
	 * Refer�ncia para a UI principal do programa
	 */
	private UIPrincipal uiPrincipal;

	/**
	 * Refer�ncia para a UI login do programa
	 */
	private UILogin uiLogin;

	//
	// M�TODOS
	//
	/**
	 * Construtor do CtrlPrograma
	 */
	public CtrlSessaoUsuario() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see controle.ICtrlCasoDeUso#iniciar()
	 */
	@Override
	public void iniciar() {

		this.uiLogin = (UILogin) ViewerManager.obterViewer(this, UILogin.class);
		this.uiLogin.exibir();

		DAO.inicializarDAOs();
		// Cria e apresenta a janela principal. Observe que n�o estamos
		// instanciando um objeto JanelaPrincipal
		// diretamente; ou seja, n�o estamos fazendo "this.uiPrincipal = new
		// JanelaPrincipal(this);".
		// Estamos utilizando o m�todo est�tico "obterViewer" para retornar qual
		// � a implementa��o
		// de UIPrincipal que iremos utilizar.

		/*
		 * this.uiPrincipal = (UIPrincipal) ViewerManager.obterViewer(this,
		 * UIPrincipal.class);
		 * 
		 * // Inicializa os DAOs DAO.inicializarDAOs(); DAO.inicializarDAOs();
		 * 
		 * // Solicita a exibi��o da uiPrincipal this.uiPrincipal.exibir();
		 */

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see controle.ICtrlCasoDeUso#terminar()
	 */
	@Override
	public void terminar() {
		// Encerra os DAOs
		DAO.fecharDAOs();
		// M�todo est�tico da classe System que encerra o programa
		System.exit(0);
	}

	/**
	 * Recupero o funcionario que esta logado no sistema
	 * 
	 * @return
	 */
	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void validarLogin(String login, String senha) throws DadosException {
		// Recupero o DAO de funcionarios para validar o login
		IDAO funcionarios = DAO.getDAO(Funcionario.class);
		// recupero o unico funcionario pelo login
		this.funcionario = (Funcionario) funcionarios.recuperarPelaChave(login);
		// se a senha confere, inicio a tela principal da aplica��o
		if (funcionario.getSenha().equals(senha)) {
			this.uiLogin.fechar();
			this.uiPrincipal = (UIPrincipal) ViewerManager.obterViewer(this, UIPrincipal.class);
			this.uiPrincipal.exibir();
		}
	}

	/**
	 * 
	 */
	public void iniciarCasoDeUsoManterModelosAeronaves() throws ControleException, DadosException {
		this.ctrlModelosAeronaves = new CtrlManterModelosAeronaves(this);
	}

	/**
	 * 
	 */
	public void terminarCasoDeUsoManterModelosAeronaves() throws ControleException {
		if (this.ctrlModelosAeronaves != null)
			this.ctrlModelosAeronaves.terminar();
		this.ctrlModelosAeronaves = null;
	}

	/** 
	 * 
	 */
	public void iniciarCasoDeUsoManterPassageiros() throws ControleException, DadosException {
		// Instanciando os controladores de caso de uso do sistema
		this.ctrlPassageiros = new CtrlManterPassageiros(this);
	}

	/**
	 *  
	 */
	public void terminarCasoDeUsoManterPassageiros() throws ControleException {
		if (this.ctrlPassageiros != null)
			this.ctrlPassageiros.terminar();
		this.ctrlPassageiros = null;
	}

	/** 
	 * 
	 */
	public void iniciarCasoDeUsoManterAeroportos() throws ControleException, DadosException {
		// Instanciando os controladores de caso de uso do sistema
		this.ctrlAeroportos = new CtrlManterAeroportos(this);
	}

	/**
	 *  
	 */
	public void terminarCasoDeUsoManterAeroportos() throws ControleException {
		if (this.ctrlAeroportos != null)
			this.ctrlAeroportos.terminar();
		this.ctrlAeroportos = null;
	}

	/**
	 * 
	 */
	public void iniciarCasoDeUsoManterFuncionarios() throws ControleException, DadosException {
		this.ctrlFuncionarios = new CtrlManterFuncionarios(this);
	}

	/**
	 * 
	 */
	public void terminarCasoDeUsoManterFuncionarios() throws ControleException {
		if (this.ctrlFuncionarios != null)
			this.ctrlFuncionarios.terminar();
		this.ctrlFuncionarios = null;
	}

	/**
	 * 
	 */
	public void iniciarCasoDeUsoManterPlanosVoos() throws ControleException, DadosException {
		this.ctrlPlanosVoos = new CtrlManterPlanosVoos(this);
	}

	/**
	 * 
	 */
	public void terminarCasoDeUsoManterPlanosVoos() throws ControleException {
		if (this.ctrlPlanosVoos != null)
			this.ctrlPlanosVoos.terminar();
		this.ctrlPlanosVoos = null;
	}

	/**
	 * O m�todo main corresponde ao ponto inicial de execu��o de um programa em
	 * Java.
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws ControleException, DadosException {
		ICtrlCasoDeUso prg = new CtrlSessaoUsuario();
		prg.iniciar();
	}
}
