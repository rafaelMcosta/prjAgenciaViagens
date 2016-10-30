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

public class CtrlAlterarAeroporto implements ICtrlCasoDeUso {
	//
	// ATRIBUTOS
	//
	public enum Status {
		ALTERANDO, ENCERRADO;

		public static void validarTransicaoStatus(Status anterior, Status novo) throws ControleException {
			if (anterior == null && novo == ALTERANDO || anterior == ALTERANDO && novo == ENCERRADO)
				return;
			throw new ControleException(new ErroDeControle("N�o se pode sair do estado "
					+ (anterior == null ? "NULO" : anterior) + " e ir para o estado " + novo));
		}
	}

	/**
	 * Refer�ncia para o controlador do caso de uso manter.
	 */
	private final CtrlManterAeroportos ctrlManterAeroportos;

	/**
	 * Refer�ncia para o objeto a ser atualizado
	 */
	private Aeroporto atual;

	/**
	 * Refer�ncia para a janela Aeroporto que permitir� a inclus�o e altera��o
	 * do Aeroporto
	 */
	private UIAeroporto uiAeroporto;

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
	 * Construtor da classe CtrlAlterarAeroporto
	 */
	public CtrlAlterarAeroporto(CtrlManterAeroportos ctrl, Aeroporto a) throws ControleException, DadosException {
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
		// Informo que o controlador de caso de uso est� dispon�vel
		this.setStatus(Status.ALTERANDO);
		// Crio e abro a janela de altera��o
		this.uiAeroporto = (UIAeroporto) ViewerManager.obterViewer(this, UIAeroporto.class);
		// Solicito � interface que atualize os campos
		this.uiAeroporto.atualizarCampos(this.atual.getSigla(), this.atual.getCidade(), this.atual.getEstado(),
				this.atual.getPais());
		// Solicito � interface que carregue os objetos
		this.uiAeroporto.exibir();
	}

	/** 
	 * 
	 */
	public void terminar() throws DadosException, ControleException {
		if (this.status == Status.ENCERRADO)
			return;
		// N�o h� Passageiro em manipula��o
		this.atual = null;
		// Fecho a janela
		this.uiAeroporto.fechar();
		// Informo que o controlador est� dispon�vel
		this.setStatus(Status.ENCERRADO);
		// Notifico ao controlador do programa o t�rmino deste caso de uso
		ctrlManterAeroportos.terminarCasoDeUsoAlterarAeroporto();
	}

	/** 
	 * 
	 */
	public void cancelarAlterar() throws DadosException, ControleException {
		// Se o controlador n�o tinha ativado uma altera��o, n�o fa�o nada!
		if (this.status != Status.ALTERANDO)
			throw new ControleException(new ErroDeControle("N�o � poss�vel cancelar uma opera��o de altera��o"));
		// Termino o caso de uso
		this.terminar();
	}

	/** 
	 * 
	 */
	public void alterar(String sigla, String cidade, String estado, String pais)
			throws DadosException, ControleException {
		// Se o controlador n�o tinha ativado uma altera��o, n�o fa�o nada!
		if (this.status != Status.ALTERANDO)
			throw new ControleException(new ErroDeControle("N�o � poss�vel concluir uma opera��o de altera��o"));
		// Atualizo os campos
		this.atual.setSigla(sigla);
		this.atual.setCidade(cidade);
		this.atual.setEstado(estado);
		this.atual.setPais(pais);
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
