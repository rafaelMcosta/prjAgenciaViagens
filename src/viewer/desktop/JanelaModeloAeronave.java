package viewer.desktop;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import viewer.UIModeloAeronave;
import model.util.DadosException;
import control.CtrlAlterarModeloAeronave;
import control.CtrlIncluirModeloAeronave;
import control.util.ControleException;
import control.util.ICtrlCasoDeUso;

public class JanelaModeloAeronave extends JFrame implements UIModeloAeronave {
	//
	// ATRIBUTOS
	//
	/**
	 * Referência para o controlador do caso de uso Incluir ModeloAeronave
	 */
	private final CtrlIncluirModeloAeronave ctrlIncluir;
	/**
	 * Referência para o controlador do caso de uso Alterar ModeloAeronave
	 */
	private final CtrlAlterarModeloAeronave ctrlAlterar;
	/**
	 * Indica se estou fazendo uma operação de inclusão ou alteração
	 */
	private boolean ehAlteração;
	/**
	 * Content Pane da Janela
	 */
	private JPanel contentPane;
	/**
	 * TextField para a descrição do ModeloAeronave
	 */
	private JTextField tfDescricao;
	/**
	 * TextField para a capacidade do ModeloAeronave
	 */
	private JTextField tfCapacidade;

	/**
	 * Construtor para inclusão
	 * 
	 * @param ct
	 * @throws DadosException
	 * @throws ControleException
	 */
	public JanelaModeloAeronave(CtrlIncluirModeloAeronave ct) throws DadosException, ControleException {
		this.ehAlteração = false;
		this.ctrlIncluir = ct;
		this.ctrlAlterar = null;
		this.criarUI();
	}

	/**
	 * Construtor para alteração
	 * 
	 * @param ct
	 * @throws DadosException
	 * @throws ControleException
	 */
	public JanelaModeloAeronave(CtrlAlterarModeloAeronave ct) throws DadosException, ControleException {
		this.ehAlteração = true;
		this.ctrlIncluir = null;
		this.ctrlAlterar = ct;
		this.criarUI();
	}

	/**
	 * Criação da UI
	 */
	public void criarUI() {
		setTitle("Modelo de Aeronave");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 450, 178);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblDescricao = new JLabel("Descrição:");
		lblDescricao.setBounds(10, 11, 70, 14);
		contentPane.add(lblDescricao);

		JLabel lblCapacidade = new JLabel("Capacidade:");
		lblCapacidade.setBounds(10, 48, 70, 14);
		contentPane.add(lblCapacidade);

		tfDescricao = new JTextField();
		tfDescricao.setBounds(86, 8, 334, 20);
		contentPane.add(tfDescricao);
		tfDescricao.setColumns(10);

		tfCapacidade = new JTextField();
		tfCapacidade.setBounds(86, 45, 86, 20);
		contentPane.add(tfCapacidade);
		tfCapacidade.setColumns(10);

		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				solicitarExecucaoDeEfetivacao();
			}
		});
		btnOk.setBounds(73, 95, 143, 23);
		contentPane.add(btnOk);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				solicitarCancelamentoDeEfetivacao();
			}
		});
		btnCancelar.setBounds(256, 95, 154, 23);
		contentPane.add(btnCancelar);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see viewer.UIModeloAeronave#solicitarExecucaoDeEfetivacao()
	 */
	@Override
	public void solicitarExecucaoDeEfetivacao() {
		try {
			// Recupero os valores digitados nos textfields
			String descricao = tfDescricao.getText();
			int capacidade = Integer.parseInt(tfCapacidade.getText());
			// Verifico qual é a operação que estou fazendo
			// e notifico ao controlador
			if (!ehAlteração)
				ctrlIncluir.incluir(descricao, capacidade);
			else
				ctrlAlterar.alterar(descricao, capacidade);
		} catch (DadosException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		} catch (ControleException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		} catch (SecurityException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see viewer.UIModeloAeronave#solicitarCancelamentoDeEfetivacao()
	 */
	@Override
	public void solicitarCancelamentoDeEfetivacao() {
		try {
			if (!ehAlteração)
				ctrlIncluir.cancelarIncluir();
			else
				ctrlAlterar.cancelarAlterar();
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
	 * @see viewer.UIModeloAeronave#fechar()
	 */
	@Override
	public void exibir() {
		this.setVisible(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see viewer.UIModeloAeronave#fechar()
	 */
	@Override
	public void fechar() {
		this.setVisible(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see viewer.UIModeloAeronave#limpar()
	 */
	@Override
	public void limpar() {
		this.tfDescricao.setText(null);
		this.tfCapacidade.setText(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see viewer.UIModeloAeronave#atualizarCampos(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void atualizarCampos(String descricao, int capacidade) {
		limpar();
		this.tfDescricao.setText(descricao);
		this.tfCapacidade.setText(String.valueOf(capacidade));
	}
}
