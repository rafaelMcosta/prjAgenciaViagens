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

import control.CtrlAlterarAeroporto;
import control.CtrlIncluirAeroporto;
import control.util.ControleException;
import model.util.DadosException;
import viewer.UIAeroporto;

public class JanelaAeroporto extends JFrame implements UIAeroporto {
	//
	// ATRIBUTOS
	//
	/**
	 * Referência para o controlador do caso de uso Incluir Aeroporto
	 */
	private final CtrlIncluirAeroporto ctrlIncluir;
	/**
	 * Referência para o controlador do caso de uso Alterar Aeroporto
	 */
	private final CtrlAlterarAeroporto ctrlAlterar;
	/**
	 * Indica se estou fazendo uma operação de inclusão ou alteração
	 */
	private boolean ehAlteração;
	/**
	 * Content Pane da Janela
	 */
	private JPanel contentPane;
	/**
	 * TextField para a sigla do Aeroporto
	 */
	private JTextField tfSigla;
	/**
	 * TextField para a cidade do Aeroporto
	 */
	private JTextField tfCidade;
	/**
	 * TextField para o estado do Aeroporto
	 */
	private JTextField tfEstado;
	/**
	 * TextField para o pais do Aeroporto
	 */
	private JTextField tfPais;

	/**
	 * Construtor para inclusão
	 * 
	 * @param ct
	 * @throws DadosException
	 * @throws ControleException
	 */
	public JanelaAeroporto(CtrlIncluirAeroporto ct) throws DadosException, ControleException {
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
	public JanelaAeroporto(CtrlAlterarAeroporto ct) throws DadosException, ControleException {
		this.ehAlteração = true;
		this.ctrlIncluir = null;
		this.ctrlAlterar = ct;
		this.criarUI();
	}

	/**
	 * Criação da UI
	 */
	public void criarUI() {
		setTitle("Aeroporto");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 250);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblSigla = new JLabel("Sigla:");
		lblSigla.setBounds(10, 11, 70, 14);
		contentPane.add(lblSigla);

		JLabel lblCidade = new JLabel("Cidade:");
		lblCidade.setBounds(10, 48, 70, 14);
		contentPane.add(lblCidade);

		JLabel lblEstado = new JLabel("Estado:");
		lblEstado.setBounds(10, 85, 70, 14);
		contentPane.add(lblEstado);

		JLabel lblPais = new JLabel("Pais:");
		lblPais.setBounds(10, 127, 80, 14);
		contentPane.add(lblPais);

		tfSigla = new JTextField();
		tfSigla.setBounds(95, 8, 334, 20);
		contentPane.add(tfSigla);
		tfSigla.setColumns(10);
		
		tfCidade = new JTextField();
		tfCidade.setBounds(95, 45, 334, 20);
		contentPane.add(tfCidade);
		tfCidade.setColumns(10);

		tfEstado = new JTextField();
		tfEstado.setBounds(95, 82, 334, 20);
		contentPane.add(tfEstado);
		tfEstado.setColumns(10);

		tfPais = new JTextField();
		tfPais.setBounds(95, 119, 334, 20);
		contentPane.add(tfPais);
		tfPais.setColumns(10);

		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				solicitarExecucaoDeEfetivacao();
			}
		});
		btnOk.setBounds(73, 170, 143, 23);
		contentPane.add(btnOk);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				solicitarCancelamentoDeEfetivacao();
			}
		});
		btnCancelar.setBounds(256, 170, 154, 23);
		contentPane.add(btnCancelar);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see viewer.UIAeroporto#solicitarExecucaoDeEfetivacao()
	 */
	@Override
	public void solicitarExecucaoDeEfetivacao() {
		try {
			// Recupero os valores digitados nos textfields
			String sigla = tfSigla.getText();
			String cidade = tfCidade.getText();
			String estado = tfEstado.getText();
			String pais = tfPais.getText();
			// Verifico qual é a operação que estou fazendo
			// e notifico ao controlador
			if (!ehAlteração)
				ctrlIncluir.incluir(sigla, cidade, estado, pais);
			else
				ctrlAlterar.alterar(sigla, cidade, estado, pais);
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
	 * @see viewer.UIAeroporto#solicitarCancelamentoDeEfetivacao()
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
	 * @see viewer.UIAeroporto#fechar()
	 */
	@Override
	public void exibir() {
		this.setVisible(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see viewer.UIAeroporto#fechar()
	 */
	@Override
	public void fechar() {
		this.setVisible(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see viewer.UIAeroporto#limpar()
	 */
	@Override
	public void limpar() {
		this.tfSigla.setText(null);
		this.tfCidade.setText(null);
		this.tfEstado.setText(null);
		this.tfPais.setText(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see viewer.UIAeroporto#atualizarCampos(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void atualizarCampos(String sigla, String cidade, String estado, String pais) {
		limpar();
		this.tfSigla.setText(sigla);
		this.tfCidade.setText(cidade);
		this.tfEstado.setText(cidade);
		this.tfPais.setText(estado);
	}
}
