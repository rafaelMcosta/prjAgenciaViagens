package viewer.desktop;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.util.DadosException;
import viewer.UIPrincipal;
import control.CtrlSessaoUsuario;
import control.util.ControleException;
import javax.swing.JToggleButton;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.SystemColor;

/**
 * Classe que implementa a janela principal do sistema
 * 
 * @author Alessandro Cerqueira
 */
public class JanelaPrincipal extends JFrame implements UIPrincipal {

	//
	// ATRIBUTOS
	//

	/**
	 * Referência para o painel de conteúdo da janela
	 */
	private JPanel contentPane;
	/**
	 * Referência para o controlador do programa a quem a janela principal irá
	 * sempre mandar a notificação.
	 */
	private CtrlSessaoUsuario ctrlPrg;

	//
	// MÉTODOS
	//
	/**
	 * Create the frame.
	 */
	// public JanelaPrincipal() {
	public JanelaPrincipal(CtrlSessaoUsuario p) {
		this.ctrlPrg = p;
		this.criarUI();
	}

	/**
	 * Cria visualmente a Janela
	 */
	public void criarUI() {
		setTitle("Principal");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 188);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnPassageiros = new JButton("Passageiros");
		btnPassageiros.setIcon(
				new ImageIcon(JanelaPrincipal.class.getResource("/javax/swing/plaf/metal/icons/ocean/directory.gif")));
		btnPassageiros.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					ctrlPrg.iniciarCasoDeUsoManterPassageiros();
				} catch (ControleException e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();
				} catch (DadosException e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();
				}
			}
		});
		
		btnPassageiros.setBounds(10, 11, 170, 55);
		contentPane.add(btnPassageiros);

		JButton btnModelosAeronaves = new JButton("Modelos Aeronaves");
		btnModelosAeronaves.setIcon(
				new ImageIcon(JanelaPrincipal.class.getResource("/javax/swing/plaf/metal/icons/ocean/directory.gif")));
		btnModelosAeronaves.addActionListener(new ActionListener() {
			// Método acionado quando o botão "Modelo Aeronave"
			// for pressionado (Método de Callback).
			public void actionPerformed(ActionEvent arg0) {
				try {
					ctrlPrg.iniciarCasoDeUsoManterModelosAeronaves();
				} catch (ControleException e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();
				} catch (DadosException e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();
				}
			}
		});
		btnModelosAeronaves.setBounds(190, 11, 170, 55);
		contentPane.add(btnModelosAeronaves);
		
		JButton btnAeroportos = new JButton("Aeroportos");
		btnAeroportos.setIcon(
				new ImageIcon(JanelaPrincipal.class.getResource("/javax/swing/plaf/metal/icons/ocean/directory.gif")));
		btnAeroportos.addActionListener(new ActionListener() {
			// Método acionado quando o botão "Aeroportos"
			// for pressionado (Método de Callback).
			public void actionPerformed(ActionEvent arg0) {
				try {
					ctrlPrg.iniciarCasoDeUsoManterAeroportos();
				} catch (ControleException e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();
				} catch (DadosException e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();
				}
			}
		});
		btnAeroportos.setBounds(10, 84, 170, 55);
		contentPane.add(btnAeroportos);

		JButton btnSair = new JButton("Sair");
		btnSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fechar();
			}
		});
		btnSair.setIcon(new ImageIcon(
				JanelaPrincipal.class.getResource("/com/sun/java/swing/plaf/windows/icons/HardDrive.gif")));
		btnSair.setBounds(190, 84, 170, 55);
		contentPane.add(btnSair);
	}

	/**
	 * Fecha a janela
	 */
	public void fechar() {
		this.setVisible(false);
		ctrlPrg.terminar();
	}

	/**
	 * Limpa a Janela
	 */
	public void limpar() {
	}

	/**
	 * Exibe a Janela
	 */
	public void exibir() {
		this.setVisible(true);
	}
}
