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

public class CtrlAlterarPassageiro implements ICtrlCasoDeUso {
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
	private final CtrlManterPassageiros ctrlManterPassageiros;

	/**
	 * Referência para o objeto a ser atualizado
	 */
	private Passageiro atual;

	/**
	 * Referência para a janela Passageiro que permitirá a inclusão e alteração
	 * do Passageiro
	 */
	private UIPassageiro uiPassageiro;

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
	 * Construtor da classe CtrlAlterarPassageiro
	 */
	public CtrlAlterarPassageiro(CtrlManterPassageiros ctrl, Passageiro p) throws ControleException, DadosException {
		// Guardo a referência para o controlador do programa
		this.ctrlManterPassageiros = ctrl;
		// Guardo a referência para o objeto a ser alterado
		this.atual = p;
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
		this.uiPassageiro = (UIPassageiro) ViewerManager.obterViewer(this, UIPassageiro.class);
		// Solicito à interface que atualize os campos
		this.uiPassageiro.atualizarCampos(this.atual.getNome(), this.atual.getCpf(), this.atual.getPassaporte(),
				this.atual.getDtNascimento().toString());
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
		// Fecho a janela
		this.uiPassageiro.fechar();
		// Informo que o controlador está disponível
		this.setStatus(Status.ENCERRADO);
		// Notifico ao controlador do programa o término deste caso de uso
		ctrlManterPassageiros.terminarCasoDeUsoAlterarPassageiro();
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
	public void alterar(String nome, String dtNascimento, String cpf, String passaporte)
			throws DadosException, ControleException {
		// Se o controlador não tinha ativado uma alteração, não faço nada!
		if (this.status != Status.ALTERANDO)
			throw new ControleException(new ErroDeControle("Não é possível concluir uma operação de alteração"));
		// Atualizo os campos
		this.atual.setNome(nome);
		String[] nascimento = dtNascimento.split("-");
		int dia = Integer.parseInt(nascimento[2]);
		int mes = Integer.parseInt(nascimento[1]);
		int ano = Integer.parseInt(nascimento[0]);
		this.atual.setDtNascimento(new LocalDate(ano, mes, dia));
		this.atual.setCpf(cpf);
		this.atual.setPassaporte(passaporte);
		// Salvo o objeto Passageiro usando o DAO
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
