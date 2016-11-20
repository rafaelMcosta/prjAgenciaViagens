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
 * Este é o controlador que gerencia a execução do meu programa.
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
	// Também o controlador do programa deve ter um atributo
	// para referenciar a janela principal do programa.
	//

	/**
	 * Referência para o controlador do caso de uso "Manter ModelosAeronaves"
	 */
	private CtrlManterModelosAeronaves ctrlModelosAeronaves;
	/**
	 * Referência para o controlador do caso de uso "Manter Passageiros"
	 */
	private CtrlManterPassageiros ctrlPassageiros;
	/**
	 * Referência para o controlador do caso de uso "Manter Aeroportos"
	 */
	private CtrlManterAeroportos ctrlAeroportos;
	/**
	 * Referência para o controlador do caso de uso "Manter Funcionarios"
	 */
	private CtrlManterFuncionarios ctrlFuncionarios;
	/**
	 * Referência para o controlador do caso de uso "Manter PlanosVoos"
	 */
	private CtrlManterPlanosVoos ctrlPlanosVoos;
	/**
	 * Referência para o funcionario(usuario) que fará login no sistema
	 */
	private Funcionario funcionario;

	/**
	 * Referência para a UI principal do programa
	 */
	private UIPrincipal uiPrincipal;

	/**
	 * Referência para a UI login do programa
	 */
	private UILogin uiLogin;

	//
	// MÉTODOS
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
		// Cria e apresenta a janela principal. Observe que não estamos
		// instanciando um objeto JanelaPrincipal
		// diretamente; ou seja, não estamos fazendo "this.uiPrincipal = new
		// JanelaPrincipal(this);".
		// Estamos utilizando o método estático "obterViewer" para retornar qual
		// é a implementação
		// de UIPrincipal que iremos utilizar.

		/*
		 * this.uiPrincipal = (UIPrincipal) ViewerManager.obterViewer(this,
		 * UIPrincipal.class);
		 * 
		 * // Inicializa os DAOs DAO.inicializarDAOs(); DAO.inicializarDAOs();
		 * 
		 * // Solicita a exibição da uiPrincipal this.uiPrincipal.exibir();
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
		// Método estático da classe System que encerra o programa
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
		// se a senha confere, inicio a tela principal da aplicação
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
	 * O método main corresponde ao ponto inicial de execução de um programa em
	 * Java.
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws ControleException, DadosException {
		ICtrlCasoDeUso prg = new CtrlSessaoUsuario();
		prg.iniciar();
	}
}
