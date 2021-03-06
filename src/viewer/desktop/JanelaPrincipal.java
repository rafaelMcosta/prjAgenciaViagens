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
	 * Refer�ncia para o painel de conte�do da janela
	 */
	private JPanel contentPane;
	/**
	 * Refer�ncia para o controlador do programa a quem a janela principal ir�
	 * sempre mandar a notifica��o.
	 */
	private CtrlSessaoUsuario ctrlPrg;

	//
	// M�TODOS
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
		setResizable(false);
		setBounds(100, 100, 375, 250);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnFuncionarios = new JButton("Funcionarios");
		btnFuncionarios.setIcon(
				new ImageIcon(JanelaPrincipal.class.getResource("/javax/swing/plaf/metal/icons/ocean/directory.gif")));
		btnFuncionarios.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					ctrlPrg.iniciarCasoDeUsoManterFuncionarios();
				} catch (ControleException e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();
				} catch (DadosException e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();
				}
			}
		});
		
		btnFuncionarios.setBounds(190, 84, 170, 55);
		contentPane.add(btnFuncionarios);

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
			// M�todo acionado quando o bot�o "Modelo Aeronave"
			// for pressionado (M�todo de Callback).
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
			// M�todo acionado quando o bot�o "Aeroportos"
			// for pressionado (M�todo de Callback).
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
		
		JButton btnPlanosVoos = new JButton("Planos Voos");
		btnPlanosVoos.setIcon(
				new ImageIcon(JanelaPrincipal.class.getResource("/javax/swing/plaf/metal/icons/ocean/directory.gif")));
		btnPlanosVoos.addActionListener(new ActionListener() {
			// M�todo acionado quando o bot�o "Planos Voos"
			// for pressionado (M�todo de Callback).
			public void actionPerformed(ActionEvent arg0) {
				try {
					ctrlPrg.iniciarCasoDeUsoManterPlanosVoos();
				} catch (ControleException e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();
				} catch (DadosException e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();
				}
			}
		});
		btnPlanosVoos.setBounds(10, 157, 170, 55);
		contentPane.add(btnPlanosVoos);

		JButton btnSair = new JButton("Sair");
		btnSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fechar();
			}
		});
		btnSair.setIcon(new ImageIcon(
				JanelaPrincipal.class.getResource("/com/sun/java/swing/plaf/windows/icons/HardDrive.gif")));
		btnSair.setBounds(190, 157, 170, 55);
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
