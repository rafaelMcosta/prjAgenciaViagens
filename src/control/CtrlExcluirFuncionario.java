package control;

import control.util.ControleException;
import control.util.ErroDeControle;
import control.util.ICtrlCasoDeUso;
import model.Compra;
import model.Funcionario;
import model.dao.DAO;
import model.dao.IDAO;
import model.util.DadosException;
import viewer.UIExcluirFuncionario;
import viewer.ViewerManager;

public class CtrlExcluirFuncionario implements ICtrlCasoDeUso{
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
		private final CtrlManterFuncionarios ctrlManterFuncionarios;

		/**
		 * Refer�ncia para o objeto a ser atualizado
		 */
		private Funcionario atual;

		/**
		 * Refer�ncia para a janela Passageiro que permitir� a exclus�o do
		 * Funcionario
		 */
		private UIExcluirFuncionario uiExcluirFuncionario;

		/**
		 * Refer�ncia para o objeto DaoFuncionario
		 */
		private IDAO<Funcionario> dao = (IDAO<Funcionario>) DAO.getDAO(Funcionario.class);

		/**
		 * Atributo que indica qual opera��o est� em curso
		 */
		private Status status;

		//
		// M�TODOS
		//

		/**
		 * Construtor da classe CtrlExcluirFuncionario
		 */
		public CtrlExcluirFuncionario(CtrlManterFuncionarios ctrl, Funcionario f) throws ControleException, DadosException {
			// Guardo a refer�ncia para o controlador do programa
			this.ctrlManterFuncionarios = ctrl;
			// Guardo a refer�ncia para o objeto a ser alterado
			this.atual = f;
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
			this.uiExcluirFuncionario = (UIExcluirFuncionario) ViewerManager.obterViewer(this, UIExcluirFuncionario.class);
			// Solicito � interface que atualize os campos
			this.uiExcluirFuncionario.atualizarCampos(this.atual.getLogin(), this.atual.getSenha(), this.atual.getCpf());
			// Solicito � interface que carregue os objetos
			this.uiExcluirFuncionario.exibir();
		}

		/** 
		 * 
		 */
		public void terminar() throws DadosException, ControleException {
			if (this.status == Status.ENCERRADO)
				return;
			// N�o h� Funcionario em manipula��o
			this.atual = null;
			// Solicito o fechamento da janela
			this.uiExcluirFuncionario.fechar();
			// Informo que o controlador est� dispon�vel
			this.setStatus(Status.ENCERRADO);
			// Notifico ao controlador do programa o t�rmino deste caso de uso
			ctrlManterFuncionarios.terminarCasoDeUsoExcluirFuncionario();
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
			// Desaloco todas as compras do Funcionario
			for (Compra c : this.atual.getCompras())
				this.atual.removeCompra(c);
			// Salvo o objeto Funcionario usando o DAO
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
