package viewer.desktop;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import control.CtrlSessaoUsuario;
import model.util.DadosException;
import viewer.UILogin;

/**
 * Calsse que implementa a janela de Login
 * 
 * @author Rafael
 *
 */
public class JanelaLogin extends JFrame implements UILogin {
	// ATRIBUTOS
	/**
	 * Referência para o painel de conteúdo da janela
	 */
	private JPanel contentPane;
	/**
	 * Referência para o controlador do programa a quem a janela principal irá
	 * sempre mandar a notificação.
	 */
	private CtrlSessaoUsuario ctrlPrg;

	/**
	 * TextField para o login
	 */
	private JTextField tfLogin;

	/**
	 * TextField para a senha
	 */
	private JPasswordField pfSenha;

	/**
	 * Cria o Frame
	 * 
	 * @param s
	 */
	public JanelaLogin(CtrlSessaoUsuario s) {
		this.ctrlPrg = s;
		this.criarUI();
	}

	public void criarUI() {
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 300, 150);
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

		tfLogin = new JTextField();
		tfLogin.setBounds(70, 8, 200, 20);
		contentPane.add(tfLogin);
		tfLogin.setColumns(10);
		tfLogin.setText("admin");

		pfSenha = new JPasswordField();
		pfSenha.setBounds(70, 45, 200, 20);
		contentPane.add(pfSenha);
		pfSenha.setColumns(10);
		pfSenha.setText("12345678");
		
		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				solicitarValidarLogin();
			}
		});
		btnOk.setBounds(70, 80, 90, 23);
		contentPane.add(btnOk);

		JButton btnEncerrar = new JButton("Encerrar");
		btnEncerrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				solicitarEncerrarLogin();;
			}
		});
		btnEncerrar.setBounds(180, 80, 90, 23);
		contentPane.add(btnEncerrar);
	}

	@Override
	public void limpar() {
		this.tfLogin.setText(null);
		this.pfSenha.setText(null);
	}

	@Override
	public void exibir() {
		this.setVisible(true);
	}

	@Override
	public void fechar() {
		this.setVisible(false);
	}

	@Override
	public void solicitarValidarLogin() {
		String login = tfLogin.getText();
		String senha = String.valueOf(pfSenha.getPassword());
		try {
			this.ctrlPrg.validarLogin(login, senha);
		} catch (DadosException e) {
			JOptionPane.showMessageDialog(null, "Usuario e/ou senha inválidos");
		}
	}

	@Override
	public void solicitarEncerrarLogin() {
		this.ctrlPrg.terminar();
	}
}
