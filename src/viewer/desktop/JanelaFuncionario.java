package viewer.desktop;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.joda.time.LocalDate;

import com.toedter.calendar.JDateChooser;

import control.CtrlAlterarFuncionario;
import control.CtrlIncluirFuncionario;
import control.util.ControleException;
import model.util.DadosException;
import viewer.UIFuncionario;

public class JanelaFuncionario extends JFrame implements UIFuncionario{
	//
	// ATRIBUTOS
	//
	/**
	 * Referência para o controlador do caso de uso Incluir Funcionario
	 */
	private final CtrlIncluirFuncionario ctrlIncluir;
	/**
	 * Referência para o controlador do caso de uso Alterar Funcionario
	 */
	private final CtrlAlterarFuncionario ctrlAlterar;
	/**
	 * Indica se estou fazendo uma operação de inclusão ou alteração
	 */
	private boolean ehAlteração;
	/**
	 * Content Pane da Janela
	 */
	private JPanel contentPane;
	/**
	 * TextField para o login do Funcionario
	 */
	private JTextField tfLogin;
	/**
	 * TextField para a senha do Funcionario
	 */
	private JTextField tfSenha;
	/**
	 * TextField para o cpf do Funcionario
	 */
	private JTextField tfCpf;

	/**
	 * Construtor para inclusão
	 * 
	 * @param ct
	 * @throws DadosException
	 * @throws ControleException
	 */
	public JanelaFuncionario(CtrlIncluirFuncionario ct) throws DadosException, ControleException {
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
	public JanelaFuncionario(CtrlAlterarFuncionario ct) throws DadosException, ControleException {
		this.ehAlteração = true;
		this.ctrlIncluir = null;
		this.ctrlAlterar = ct;
		this.criarUI();
	}

	/**
	 * Criação da UI
	 */
	public void criarUI() {
		setTitle("Funcionario");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 450, 250);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblLogin = new JLabel("Login:");
		lblLogin.setBounds(10, 11, 70, 14);
		contentPane.add(lblLogin);

		JLabel lblSenha = new JLabel("Senha:");
		lblSenha.setBounds(10, 48, 70, 14);
		contentPane.add(lblSenha);

		JLabel lblCpf = new JLabel("Cpf:");
		lblCpf.setBounds(10, 85, 70, 14);
		contentPane.add(lblCpf);

		tfLogin = new JTextField();
		tfLogin.setBounds(95, 8, 334, 20);
		contentPane.add(tfLogin);
		tfLogin.setColumns(10);

		tfSenha = new JTextField();
		tfSenha.setBounds(95, 45, 334, 20);
		contentPane.add(tfSenha);
		tfSenha.setColumns(10);

		tfCpf = new JTextField();
		tfCpf.setBounds(95, 82, 334, 20);
		contentPane.add(tfCpf);
		tfCpf.setColumns(10);

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
	 * @see viewer.UIFuncionario#solicitarExecucaoDeEfetivacao()
	 */
	@Override
	public void solicitarExecucaoDeEfetivacao() {
		try {
			// Recupero os valores digitados nos textfields
			String login = tfLogin.getText();
			String senha = tfSenha.getText();
			String cpf = tfCpf.getText();
			// Verifico qual é a operação que estou fazendo
			// e notifico ao controlador
			if (!ehAlteração)
				ctrlIncluir.incluir(login, senha, cpf);
			else
				ctrlAlterar.alterar(login, senha, cpf);
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
	 * @see viewer.UIFuncionario#solicitarCancelamentoDeEfetivacao()
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
	 * @see viewer.UIFuncionario#fechar()
	 */
	@Override
	public void exibir() {
		this.setVisible(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see viewer.UIFuncionario#fechar()
	 */
	@Override
	public void fechar() {
		this.setVisible(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see viewer.UIFuncionario#limpar()
	 */
	@Override
	public void limpar() {
		this.tfLogin.setText(null);
		this.tfSenha.setText(null);
		this.tfCpf.setText(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see viewer.UIFuncionario#atualizarCampos(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void atualizarCampos(String login, String senha, String cpf) {
		limpar();
		this.tfLogin.setText(login);
		this.tfSenha.setText(senha);
		this.tfCpf.setText(cpf);
	}

}
