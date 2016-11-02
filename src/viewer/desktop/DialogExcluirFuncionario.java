package viewer.desktop;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import control.CtrlExcluirFuncionario;
import control.util.ControleException;
import model.util.DadosException;
import viewer.UIExcluirFuncionario;

public class DialogExcluirFuncionario implements UIExcluirFuncionario{
	//
	// ATRIBUTOS
	//
	/**
	 * Referência para o controlador do caso de uso Excluir Funcionario
	 */
	private final CtrlExcluirFuncionario ctrl;
	/**
	 * Content Pane da Janela
	 */
	private JPanel contentPane;

	private String mensagem;

	/**
	 * Construtor para exclusão
	 * 
	 * @param ct
	 * @throws DadosException
	 * @throws ControleException
	 */
	public DialogExcluirFuncionario(CtrlExcluirFuncionario ct) throws DadosException, ControleException {
		this.ctrl = ct;
	}

	/**
	 * Criação da UI
	 */
	public void criarUI() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see viewer.UIFuncionario#solicitarExecucaoDeEfetivacao()
	 */
	@Override
	public void solicitarExecucaoDeEfetivacao() {
		try {
			ctrl.excluir();
		} catch (DadosException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		} catch (ControleException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see viewer.UIFuncionario#solicitarCancelamentoDeEfetivacao()
	 */
	@Override
	public void solicitarCancelamentoDeEfetivacao() {
		try {
			ctrl.cancelarExcluir();
		} catch (DadosException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		} catch (ControleException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see viewer.UIFuncionario#fechar()
	 */
	@Override
	public void exibir() {
		if (JOptionPane.showConfirmDialog(null, this.mensagem) == JOptionPane.YES_OPTION)
			this.solicitarExecucaoDeEfetivacao();
		else
			this.solicitarCancelamentoDeEfetivacao();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see viewer.UIFuncionario#fechar()
	 */
	@Override
	public void fechar() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see viewer.UIFuncionario#limpar()
	 */
	@Override
	public void limpar() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see viewer.UIFuncionario#atualizarCampos(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void atualizarCampos(String login, String senha, String cpf) {
		this.mensagem = "Deseja excluir o Funcionario " + login + "?";
	}

}
