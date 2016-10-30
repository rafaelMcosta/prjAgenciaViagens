package control;

import control.util.ControleException;
import control.util.ErroDeControle;
import control.util.ICtrlCasoDeUso;
import model.Passageiro;
import model.Passagem;
import model.dao.DAO;
import model.dao.IDAO;
import model.util.DadosException;
import viewer.UIExcluirPassageiro;
import viewer.ViewerManager;

public class CtrlExcluirPassageiro implements ICtrlCasoDeUso{
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
	private final CtrlManterPassageiros ctrlManterPassageiros;

	/**
	 * Refer�ncia para o objeto a ser atualizado
	 */
	private Passageiro atual;

	/**
	 * Refer�ncia para a janela Passageiro que permitir� a exclus�o do
	 * Passageiro
	 */
	private UIExcluirPassageiro uiExcluirPassageiro;

	/**
	 * Refer�ncia para o objeto DaoPassageiro
	 */
	private IDAO<Passageiro> dao = (IDAO<Passageiro>) DAO.getDAO(Passageiro.class);

	/**
	 * Atributo que indica qual opera��o est� em curso
	 */
	private Status status;

	//
	// M�TODOS
	//

	/**
	 * Construtor da classe CtrlExcluirPassageiro
	 */
	public CtrlExcluirPassageiro(CtrlManterPassageiros ctrl, Passageiro p) throws ControleException, DadosException {
		// Guardo a refer�ncia para o controlador do programa
		this.ctrlManterPassageiros = ctrl;
		// Guardo a refer�ncia para o objeto a ser alterado
		this.atual = p;
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
		this.uiExcluirPassageiro = (UIExcluirPassageiro) ViewerManager.obterViewer(this, UIExcluirPassageiro.class);
		// Solicito � interface que atualize os campos
		this.uiExcluirPassageiro.atualizarCampos(this.atual.getNome(), this.atual.getCpf(),
				this.atual.getDtNascimento().toString(), this.atual.getPassaporte());
		// Solicito � interface que carregue os objetos
		this.uiExcluirPassageiro.exibir();
	}

	/** 
	 * 
	 */
	public void terminar() throws DadosException, ControleException {
		if (this.status == Status.ENCERRADO)
			return;
		// N�o h� Passageiro em manipula��o
		this.atual = null;
		// Solicito o fechamento da janela
		this.uiExcluirPassageiro.fechar();
		// Informo que o controlador est� dispon�vel
		this.setStatus(Status.ENCERRADO);
		// Notifico ao controlador do programa o t�rmino deste caso de uso
		ctrlManterPassageiros.terminarCasoDeUsoExcluirPassageiro();
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
		// Desaloco todas as passagens do Passageiro
		for (Passagem p : this.atual.getPassagens())
			this.atual.removePassagem(p);
		// Salvo o objeto Passageiro usando o DAO
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
