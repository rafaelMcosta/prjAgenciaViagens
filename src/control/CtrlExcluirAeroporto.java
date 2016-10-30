package control;

import control.util.ControleException;
import control.util.ErroDeControle;
import control.util.ICtrlCasoDeUso;
import model.Aeroporto;
import model.PlanoVoo;
import model.dao.DAO;
import model.dao.IDAO;
import model.util.DadosException;
import viewer.UIExcluirAeroporto;
import viewer.ViewerManager;

public class CtrlExcluirAeroporto implements ICtrlCasoDeUso {
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
	private final CtrlManterAeroportos ctrlManterAeroportos;

	/**
	 * Refer�ncia para o objeto a ser atualizado
	 */
	private Aeroporto atual;

	/**
	 * Refer�ncia para a janela Aeroporto que permitir� a exclus�o do Aeroporto
	 */
	private UIExcluirAeroporto uiExcluirAeroporto;

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
	 * Construtor da classe CtrlExcluirAeroporto
	 */
	public CtrlExcluirAeroporto(CtrlManterAeroportos ctrl, Aeroporto a) throws ControleException, DadosException {
		// Guardo a refer�ncia para o controlador do programa
		this.ctrlManterAeroportos = ctrl;
		// Guardo a refer�ncia para o objeto a ser alterado
		this.atual = a;
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
		this.uiExcluirAeroporto = (UIExcluirAeroporto) ViewerManager.obterViewer(this, UIExcluirAeroporto.class);
		// Solicito � interface que atualize os campos
		this.uiExcluirAeroporto.atualizarCampos(this.atual.getSigla(), this.atual.getCidade(), this.atual.getEstado(),
				this.atual.getPais());
		// Solicito � interface que carregue os objetos
		this.uiExcluirAeroporto.exibir();
	}

	/** 
	 * 
	 */
	public void terminar() throws DadosException, ControleException {
		if (this.status == Status.ENCERRADO)
			return;
		// N�o h� Aeroporto em manipula��o
		this.atual = null;
		// Solicito o fechamento da janela
		this.uiExcluirAeroporto.fechar();
		// Informo que o controlador est� dispon�vel
		this.setStatus(Status.ENCERRADO);
		// Notifico ao controlador do programa o t�rmino deste caso de uso
		ctrlManterAeroportos.terminarCasoDeUsoExcluirAeroporto();
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
		// Desaloco todas as partidas do Aeroporto
		for (PlanoVoo p : this.atual.getPartidas())
			this.atual.removePartida(p);
		// Desaloco todas as chegadas do Aeroporto
		for (PlanoVoo c : this.atual.getChegadas())
			this.atual.removeChegada(c);
		// Salvo o objeto Aeroporto usando o DAO
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
