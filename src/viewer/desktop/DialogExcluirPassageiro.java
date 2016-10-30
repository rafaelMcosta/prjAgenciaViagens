package viewer.desktop;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import control.CtrlExcluirPassageiro;
import control.util.ControleException;
import model.util.DadosException;
import viewer.UIExcluirPassageiro;

public class DialogExcluirPassageiro implements UIExcluirPassageiro {
	//
	// ATRIBUTOS
	//
	/**
	 * Referência para o controlador do caso de uso Excluir Passageiro
	 */
	private final CtrlExcluirPassageiro ctrl;
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
	public DialogExcluirPassageiro(CtrlExcluirPassageiro ct) throws DadosException, ControleException {
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
	 * @see viewer.UIPassageiro#solicitarExecucaoDeEfetivacao()
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
	 * @see viewer.UIPassageiro#solicitarCancelamentoDeEfetivacao()
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
	 * @see viewer.UIPassageiro#fechar()
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
	 * @see viewer.UIPassageiro#fechar()
	 */
	@Override
	public void fechar() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see viewer.UIPassageiro#limpar()
	 */
	@Override
	public void limpar() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see viewer.UIPassageiro#atualizarCampos(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void atualizarCampos(String nome, String dtNascimento, String cpf, String passaporte) {
		this.mensagem = "Deseja excluir o Passageiro " + nome + "?";
	}
}
