package control;

import model.Aeronave;
//import model.Departamento;
//import model.Empregado;
import model.ModeloAeronave;
import model.Poltrona;
import model.dao.DAO;
import model.dao.IDAO;
import model.util.DadosException;
import viewer.UIExcluirModeloAeronave;
import viewer.ViewerManager;
import control.util.ControleException;
import control.util.ErroDeControle;
import control.util.ICtrlCasoDeUso;

public class CtrlExcluirModeloAeronave implements ICtrlCasoDeUso {
	//
	// ATRIBUTOS
	//
	public enum Status {
		EXCLUINDO, ENCERRADO;

		public static void validarTransicaoStatus(Status anterior, Status novo) throws ControleException {
			if (anterior == null && novo == EXCLUINDO || anterior == EXCLUINDO && novo == ENCERRADO)
				return;
			throw new ControleException(new ErroDeControle("Não se pode sair do estado "
					+ (anterior == null ? "NULO" : anterior) + " e ir para o estado " + novo));
		}
	}

	/**
	 * Referência para o controlador do caso de uso
	 */
	private final CtrlManterModelosAeronaves ctrlManterModelosAeronaves;

	/**
	 * Referência para o objeto a ser atualizado
	 */
	private ModeloAeronave atual;

	/**
	 * Referência para a janela ModeloAeronave que permitirá a exclusão do
	 * ModeloAeronave
	 */
	private UIExcluirModeloAeronave uiExcluirModeloAeronave;

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
	 * Construtor da classe CtrlExcluirModeloAeronave
	 */
	public CtrlExcluirModeloAeronave(CtrlManterModelosAeronaves ctrl, ModeloAeronave m)
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
		// Informo que o controlador de caso de uso está excluindo
		this.setStatus(Status.EXCLUINDO);
		// Crio e abro a janela de exclusão
		this.uiExcluirModeloAeronave = (UIExcluirModeloAeronave) ViewerManager.obterViewer(this,
				UIExcluirModeloAeronave.class);
		// Solicito à interface que atualize os campos
		this.uiExcluirModeloAeronave.atualizarCampos(this.atual.getDescricao(), this.atual.getCapacidade());
		// Solicito à interface que carregue os objetos
		this.uiExcluirModeloAeronave.exibir();
	}

	/** 
	 * 
	 */
	public void terminar() throws DadosException, ControleException {
		if (this.status == Status.ENCERRADO)
			return;
		// Não há ModeloAeronave em manipulação
		this.atual = null;
		// Solicito o fechamento da janela
		this.uiExcluirModeloAeronave.fechar();
		// Informo que o controlador está disponível
		this.setStatus(Status.ENCERRADO);
		// Notifico ao controlador do programa o término deste caso de uso
		ctrlManterModelosAeronaves.terminarCasoDeUsoExcluirModeloAeronave();
	}

	/** 
	 * 
	 */
	public void cancelarExcluir() throws DadosException, ControleException {
		// Se o controlador não tinha ativado uma exclusão, não faço nada!
		if (this.status != Status.EXCLUINDO)
			throw new ControleException(new ErroDeControle("Não é possível cancelar uma operação de exclusão"));
		// Termino o caso de uso
		this.terminar();
	}

	/** 
	 * 
	 */
	public void excluir() throws DadosException, ControleException {
		// Se o controlador não tinha ativado uma exclusão, não faço nada!
		if (this.status != Status.EXCLUINDO)
			throw new ControleException(new ErroDeControle("Não é possível concluir uma operação de exclusão"));
		// Desaloco todas as aeronaves do ModeloAeronave
		for (Aeronave a : this.atual.getAeronaves())
			this.atual.removeAeronave(a);
		// Desaloco todas as poltronas do ModeloAeronave
		for (Poltrona p : this.atual.getPoltronas())
			this.atual.removePoltrona(p);
		// Salvo o objeto ModeloAeronave usando o DAO
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
