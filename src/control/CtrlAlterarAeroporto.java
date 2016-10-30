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
			throw new ControleException(new ErroDeControle("Não se pode sair do estado "
					+ (anterior == null ? "NULO" : anterior) + " e ir para o estado " + novo));
		}
	}

	/**
	 * Referência para o controlador do caso de uso manter.
	 */
	private final CtrlManterAeroportos ctrlManterAeroportos;

	/**
	 * Referência para o objeto a ser atualizado
	 */
	private Aeroporto atual;

	/**
	 * Referência para a janela Aeroporto que permitirá a inclusão e alteração
	 * do Aeroporto
	 */
	private UIAeroporto uiAeroporto;

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
	 * Construtor da classe CtrlAlterarAeroporto
	 */
	public CtrlAlterarAeroporto(CtrlManterAeroportos ctrl, Aeroporto a) throws ControleException, DadosException {
		// Guardo a referência para o controlador do programa
		this.ctrlManterAeroportos = ctrl;
		// Guardo a referência para o objeto a ser alterado
		this.atual = a;
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
		this.uiAeroporto = (UIAeroporto) ViewerManager.obterViewer(this, UIAeroporto.class);
		// Solicito à interface que atualize os campos
		this.uiAeroporto.atualizarCampos(this.atual.getSigla(), this.atual.getCidade(), this.atual.getEstado(),
				this.atual.getPais());
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
		// Fecho a janela
		this.uiAeroporto.fechar();
		// Informo que o controlador está disponível
		this.setStatus(Status.ENCERRADO);
		// Notifico ao controlador do programa o término deste caso de uso
		ctrlManterAeroportos.terminarCasoDeUsoAlterarAeroporto();
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
	public void alterar(String sigla, String cidade, String estado, String pais)
			throws DadosException, ControleException {
		// Se o controlador não tinha ativado uma alteração, não faço nada!
		if (this.status != Status.ALTERANDO)
			throw new ControleException(new ErroDeControle("Não é possível concluir uma operação de alteração"));
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
