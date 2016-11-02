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
				throw new ControleException(new ErroDeControle("Não se pode sair do estado "
						+ (anterior == null ? "NULO" : anterior) + " e ir para o estado " + novo));
			}
		}

		/**
		 * Referência para o controlador do caso de uso
		 */
		private final CtrlManterFuncionarios ctrlManterFuncionarios;

		/**
		 * Referência para o objeto a ser atualizado
		 */
		private Funcionario atual;

		/**
		 * Referência para a janela Passageiro que permitirá a exclusão do
		 * Funcionario
		 */
		private UIExcluirFuncionario uiExcluirFuncionario;

		/**
		 * Referência para o objeto DaoFuncionario
		 */
		private IDAO<Funcionario> dao = (IDAO<Funcionario>) DAO.getDAO(Funcionario.class);

		/**
		 * Atributo que indica qual operação está em curso
		 */
		private Status status;

		//
		// MÉTODOS
		//

		/**
		 * Construtor da classe CtrlExcluirFuncionario
		 */
		public CtrlExcluirFuncionario(CtrlManterFuncionarios ctrl, Funcionario f) throws ControleException, DadosException {
			// Guardo a referência para o controlador do programa
			this.ctrlManterFuncionarios = ctrl;
			// Guardo a referência para o objeto a ser alterado
			this.atual = f;
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
			this.uiExcluirFuncionario = (UIExcluirFuncionario) ViewerManager.obterViewer(this, UIExcluirFuncionario.class);
			// Solicito à interface que atualize os campos
			this.uiExcluirFuncionario.atualizarCampos(this.atual.getLogin(), this.atual.getSenha(), this.atual.getCpf());
			// Solicito à interface que carregue os objetos
			this.uiExcluirFuncionario.exibir();
		}

		/** 
		 * 
		 */
		public void terminar() throws DadosException, ControleException {
			if (this.status == Status.ENCERRADO)
				return;
			// Não há Funcionario em manipulação
			this.atual = null;
			// Solicito o fechamento da janela
			this.uiExcluirFuncionario.fechar();
			// Informo que o controlador está disponível
			this.setStatus(Status.ENCERRADO);
			// Notifico ao controlador do programa o término deste caso de uso
			ctrlManterFuncionarios.terminarCasoDeUsoExcluirFuncionario();
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
