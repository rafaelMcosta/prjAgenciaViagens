package viewer.desktop;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import control.CtrlExcluirAeroporto;
import control.util.ControleException;
import model.util.DadosException;
import viewer.UIExcluirAeroporto;

public class DialogExcluirAeroporto implements UIExcluirAeroporto {
	//
	// ATRIBUTOS
	//
	/**
	 * Referência para o controlador do caso de uso Excluir Aeroporto
	 */
	private final CtrlExcluirAeroporto ctrl;
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
	public DialogExcluirAeroporto(CtrlExcluirAeroporto ct) throws DadosException, ControleException {
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
	 * @see viewer.UIAeroporto#solicitarExecucaoDeEfetivacao()
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
	 * @see viewer.UIAeroporto#solicitarCancelamentoDeEfetivacao()
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
	 * @see viewer.UIAeroporto#fechar()
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
	 * @see viewer.UIAeroporto#fechar()
	 */
	@Override
	public void fechar() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see viewer.UIAeroporto#limpar()
	 */
	@Override
	public void limpar() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see viewer.UIAeroporto#atualizarCampos(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void atualizarCampos(String sigla, String cidade, String estado, String pais) {
		this.mensagem = "Deseja excluir o Aeroporto " + sigla + "?";
	}
}
