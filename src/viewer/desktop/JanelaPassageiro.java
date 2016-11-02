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

import control.CtrlAlterarPassageiro;
import control.CtrlIncluirPassageiro;
import control.util.ControleException;
import model.util.DadosException;
import viewer.UIPassageiro;

public class JanelaPassageiro extends JFrame implements UIPassageiro {
	//
	// ATRIBUTOS
	//
	/**
	 * Referência para o controlador do caso de uso Incluir Passageiro
	 */
	private final CtrlIncluirPassageiro ctrlIncluir;
	/**
	 * Referência para o controlador do caso de uso Alterar Passageiro
	 */
	private final CtrlAlterarPassageiro ctrlAlterar;
	/**
	 * Indica se estou fazendo uma operação de inclusão ou alteração
	 */
	private boolean ehAlteração;
	/**
	 * Content Pane da Janela
	 */
	private JPanel contentPane;
	/**
	 * TextField para o nome do Passageiro
	 */
	private JTextField tfNome;
	/**
	 * TextField para o cpf do Passageiro
	 */
	private JTextField tfCpf;
	/**
	 * TextField para o passaporte do Passageiro
	 */
	private JTextField tfPassaporte;

	/**
	 * DateChooser para a data de nascimento do Passageiro
	 */
	private JDateChooser dtNascimento;

	/**
	 * Construtor para inclusão
	 * 
	 * @param ct
	 * @throws DadosException
	 * @throws ControleException
	 */
	public JanelaPassageiro(CtrlIncluirPassageiro ct) throws DadosException, ControleException {
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
	public JanelaPassageiro(CtrlAlterarPassageiro ct) throws DadosException, ControleException {
		this.ehAlteração = true;
		this.ctrlIncluir = null;
		this.ctrlAlterar = ct;
		this.criarUI();
	}

	/**
	 * Criação da UI
	 */
	public void criarUI() {
		setTitle("Passageiro");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 450, 250);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNome = new JLabel("Nome:");
		lblNome.setBounds(10, 11, 70, 14);
		contentPane.add(lblNome);

		JLabel lblCpf = new JLabel("Cpf:");
		lblCpf.setBounds(10, 48, 70, 14);
		contentPane.add(lblCpf);

		JLabel lblPassaporte = new JLabel("Passaporte:");
		lblPassaporte.setBounds(10, 85, 70, 14);
		contentPane.add(lblPassaporte);

		JLabel lblDtNascimento = new JLabel("Nascimento:");
		lblDtNascimento.setBounds(10, 127, 80, 14);
		contentPane.add(lblDtNascimento);

		tfNome = new JTextField();
		tfNome.setBounds(95, 8, 334, 20);
		contentPane.add(tfNome);
		tfNome.setColumns(10);

		tfCpf = new JTextField();
		tfCpf.setBounds(95, 45, 334, 20);
		contentPane.add(tfCpf);
		tfCpf.setColumns(10);

		tfPassaporte = new JTextField();
		tfPassaporte.setBounds(95, 82, 334, 20);
		contentPane.add(tfPassaporte);
		tfPassaporte.setColumns(10);

		dtNascimento = new JDateChooser();
		dtNascimento.setBounds(95, 122, 90, 20);
		contentPane.add(dtNascimento, BorderLayout.CENTER);
		dtNascimento.getDateEditor().setEnabled(false);

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
	 * @see viewer.UIPassageiro#solicitarExecucaoDeEfetivacao()
	 */
	@Override
	public void solicitarExecucaoDeEfetivacao() {
		try {
			// Recupero os valores digitados nos textfields
			String nome = tfNome.getText();
			LocalDate nascimento = new LocalDate(dtNascimento.getDate());
			// String dtNascimento = tfDtNascimento.getText();
			String cpf = tfCpf.getText();
			String passaporte = tfPassaporte.getText();
			// Verifico qual é a operação que estou fazendo
			// e notifico ao controlador
			if (!ehAlteração)
				ctrlIncluir.incluir(nome, nascimento, cpf, passaporte);
			else
				ctrlAlterar.alterar(nome, nascimento, cpf, passaporte);
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
	 * @see viewer.UIPassageiro#solicitarCancelamentoDeEfetivacao()
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
	 * @see viewer.UIPassageiro#fechar()
	 */
	@Override
	public void exibir() {
		this.setVisible(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see viewer.UIPassageiro#fechar()
	 */
	@Override
	public void fechar() {
		this.setVisible(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see viewer.UIPassageiro#limpar()
	 */
	@Override
	public void limpar() {
		this.tfNome.setText(null);
		// this.tfDtNascimento.setText(null);
		this.dtNascimento.setDate(new Date());
		this.tfCpf.setText(null);
		this.tfPassaporte.setText(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see viewer.UIPassageiro#atualizarCampos(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void atualizarCampos(String nome, String cpf, String passaporte, Date dtNascimento) {
		limpar();
		this.tfNome.setText(nome);
		// this.tfDtNascimento.setText(dtNascimento);
		this.dtNascimento.setDate(dtNascimento);
		this.tfCpf.setText(cpf);
		this.tfPassaporte.setText(passaporte);
	}
}
