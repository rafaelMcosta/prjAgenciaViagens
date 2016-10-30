package control;

import org.joda.time.LocalDate;

import control.util.ControleException;
import control.util.ErroDeControle;
import control.util.ICtrlCasoDeUso;
import model.Aeroporto;
import model.dao.DAO;
import model.dao.IDAO;
import model.util.DadosException;
import viewer.UIAeroporto;
import viewer.ViewerManager;

public class CtrlIncluirAeroporto implements ICtrlCasoDeUso{
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
	 * Referência para o controlador do caso de uso Manter Aeroportos.
	 */
	private final CtrlManterAeroportos ctrlManterAeroportos;

	/**
	 * Referência para a UI Aeroporto que permitirá a inclusão e alteração do
	 * Aeroporto
	 */
	private UIAeroporto uiAeroporto;

	/**
	 * Referência para o objeto Aeroporto sendo manipulado
	 */
	private Aeroporto atual;

	/**
	 * Referência para o objeto DaoAeroporto
	 */
	private IDAO<Aeroporto> dao = (IDAO<Aeroporto>) DAO.getDAO(Aeroporto.class);

	/**
	 * Atributo que indica qual operação está em curso
	 */
	private Status status;

	//
	// MÉTODOS
	//

	/**
	 * Construtor da classe CtrlIncluirAeroporto
	 */
	public CtrlIncluirAeroporto(CtrlManterAeroportos ctrl) throws DadosException, ControleException {
		// Guardo a referência para o controlador do programa
		this.ctrlManterAeroportos = ctrl;
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
		this.uiAeroporto = (UIAeroporto) ViewerManager.obterViewer(this, UIAeroporto.class);
		// Solicito à interface que carregue os objetos
		this.uiAeroporto.exibir();
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
		this.uiAeroporto.fechar();
		// Informo que o controlador está disponível
		this.setStatus(Status.ENCERRADO);
		// Notifico ao controlador do programa o término deste caso de uso
		ctrlManterAeroportos.terminarCasoDeUsoIncluirAeroporto();
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
	public void incluir(String sigla, String cidade, String estado, String pais)
			throws DadosException, ControleException {
		// Se o controlador não tinha ativado uma inclusao, não faço nada!
		if (this.status != Status.INCLUINDO)
			throw new ControleException(new ErroDeControle("Não é possível concluir uma operação de inclusão"));
		// Crio um novo objeto Aeroporto
		this.atual = new Aeroporto(sigla, cidade, estado, pais);
		// Salvo o objeto Aeroporto usando o DAO
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
