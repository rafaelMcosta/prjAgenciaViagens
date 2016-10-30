package control;

import org.joda.time.LocalDate;

import control.util.ControleException;
import control.util.ErroDeControle;
import control.util.ICtrlCasoDeUso;
import model.Passageiro;
import model.dao.DAO;
import model.dao.IDAO;
import model.util.DadosException;
import viewer.UIPassageiro;
import viewer.ViewerManager;

public class CtrlIncluirPassageiro implements ICtrlCasoDeUso {
	//
	// ATRIBUTOS
	//
	public enum Status {
		INCLUINDO, ENCERRADO;

		public static void validarTransicaoStatus(Status anterior, Status novo) throws ControleException {
			if (anterior == null && novo == INCLUINDO || anterior == INCLUINDO && novo == ENCERRADO)
				return;
			throw new ControleException(new ErroDeControle("Não se pode sair do estado "
					+ (anterior == null ? "NULO" : anterior) + " e ir para o estado " + novo));
		}
	}

	/**
	 * Referência para o controlador do caso de uso Manter Passageiros.
	 */
	private final CtrlManterPassageiros ctrlManterPassageiros;

	/**
	 * Referência para a UI Passageiro que permitirá a inclusão e alteração do
	 * ModeloAeronave
	 */
	private UIPassageiro uiPassageiro;

	/**
	 * Referência para o objeto Passageiro sendo manipulado
	 */
	private Passageiro atual;

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
	 * Construtor da classe CtrlIncluirPassageiro
	 */
	public CtrlIncluirPassageiro(CtrlManterPassageiros ctrl) throws DadosException, ControleException {
		// Guardo a referência para o controlador do programa
		this.ctrlManterPassageiros = ctrl;
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
		this.uiPassageiro = (UIPassageiro) ViewerManager.obterViewer(this, UIPassageiro.class);
		// Solicito à interface que carregue os objetos
		this.uiPassageiro.exibir();
	}

	/** 
	 * 
	 */
	public void terminar() throws DadosException, ControleException {
		if (this.status == Status.ENCERRADO)
			return;
		// Não há Passageiro em manipulação
		this.atual = null;
		// Fecho a UI
		this.uiPassageiro.fechar();
		// Informo que o controlador está disponível
		this.setStatus(Status.ENCERRADO);
		// Notifico ao controlador do programa o término deste caso de uso
		ctrlManterPassageiros.terminarCasoDeUsoIncluirPassageiro();
	}

	/** 
	 * 
	 */
	public void cancelarIncluir() throws DadosException, ControleException {
		// Se o controlador não tinha ativado uma inclusao, não faço nada!
		if (this.status != Status.INCLUINDO)
			throw new ControleException(new ErroDeControle("Não é possível cancelar uma operação de inclusão"));
		// Termino o caso de uso
		this.terminar();
	}

	/** 
	 * 
	 */
	public void incluir(String nome, String dtNascimento, String cpf, String passaporte)
			throws DadosException, ControleException {
		// Se o controlador não tinha ativado uma inclusao, não faço nada!
		if (this.status != Status.INCLUINDO)
			throw new ControleException(new ErroDeControle("Não é possível concluir uma operação de inclusão"));
		// Crio um novo objeto Passageiro
		// extraindo o dia, mes e ano da string data e retirando as barras
		String[] nascimento = dtNascimento.split("/");
		int dia = Integer.parseInt(nascimento[0]);
		int mes = Integer.parseInt(nascimento[1]);
		int ano = Integer.parseInt(nascimento[2]);
		this.atual = new Passageiro(nome, new LocalDate(ano, mes, dia), cpf, passaporte);
		// Salvo o objeto Passageiro usando o DAO
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
		Status.validarTransicaoStatus(this.status, novo);
		this.status = novo;
	}
}
