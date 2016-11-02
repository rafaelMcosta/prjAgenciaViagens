package viewer.desktop;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import control.CtrlManterPassageiros;
import control.util.ControleException;
import model.util.DadosException;
import model.util.IDadosParaTabela;
import viewer.UICadastroPassageiros;

public class JanelaCadastroPassageiros extends JFrame implements UICadastroPassageiros {
	/**
	 * Referência para o controlador do caso de uso Manter Passageiros
	 */
	private CtrlManterPassageiros ctrl;
	/**
	 * Lista de objetos a serem exibidos
	 */
	private List<IDadosParaTabela> objetos;
	/**
	 * Referência ao contentPane da Janela
	 */
	private JPanel contentPane;
	/**
	 * Referência ao JTable embutido na janela
	 */
	private JTable table;
	/**
	 * Referência para o TableModel da tabela
	 */
	private DefaultTableModel tableModel;

	/**
	 * Create the frame.
	 */
	public JanelaCadastroPassageiros(CtrlManterPassageiros c) {
		this.ctrl = c;
		this.criarUI();
	}

	/**
	 * Cria Visualmente a Janela
	 */
	@Override
	public void criarUI() {
		setTitle("Passageiros");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 619, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnIncluir = new JButton("Incluir");
		btnIncluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				solicitarExecucaoDeIncluirPassageiro();
			}
		});
		btnIncluir.setBounds(100, 232, 89, 23);
		contentPane.add(btnIncluir);

		JButton btnExcluir = new JButton("Excluir");
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				solicitarExecucaoDeExcluirPassageiro();
			}
		});
		btnExcluir.setBounds(199, 232, 89, 23);
		contentPane.add(btnExcluir);

		JButton btnAlterar = new JButton("Alterar");
		btnAlterar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				solicitarExecucaoDeAlterarPassageiro();
			}
		});
		btnAlterar.setBounds(298, 232, 89, 23);
		contentPane.add(btnAlterar);

		JButton btnSair = new JButton("Sair");
		btnSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				solicitarTerminoDeManterPassageiros();
			}
		});
		btnSair.setBounds(397, 232, 89, 23);
		contentPane.add(btnSair);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 586, 212);
		contentPane.add(scrollPane);

		table = new JTable();
		this.tableModel = new DefaultTableModel(null, new String[] { "Passageiros" });
		table.setModel(this.tableModel);
		scrollPane.setViewportView(table);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see viewer.UICadastroPassageiros#exibirObjetos(java.util.List)
	 */
	@Override
	public void exibirObjetos(List<IDadosParaTabela> objetos) {
		this.limpar();
		if (objetos.size() > 0) {
			this.tableModel = new DefaultTableModel(null, objetos.get(0).getCamposDeTabela());
			table.setModel(this.tableModel);
			table.getColumnModel().getColumn(0).setPreferredWidth(170);
			/*
			 * table.getColumnModel().getColumn(1).setPreferredWidth(90);
			 * table.getColumnModel().getColumn(2).setPreferredWidth(60);
			 * table.getColumnModel().getColumn(3).setPreferredWidth(60);
			 */
		}
		this.objetos = objetos;
		for (IDadosParaTabela d : objetos)
			this.tableModel.addRow(d.getDadosParaTabela());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see viewer.UICadastroPassageiros#limpar()
	 */
	@Override
	public void limpar() {
		while (this.tableModel.getRowCount() > 0)
			this.tableModel.removeRow(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see viewer.UICadastroPassageiros#solicitarExecucaoDeIncluirPassageiro()
	 */
	@Override
	public void solicitarExecucaoDeIncluirPassageiro() {
		try {
			this.ctrl.iniciarCasoDeUsoIncluirPassageiro();
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
	 * @see viewer.UICadastroPassageiros#solicitarExecucaoDeExcluirPassageiro()
	 */
	@Override
	public void solicitarExecucaoDeExcluirPassageiro() {
		// Recupero a posição selecionada
		int pos = table.getSelectedRow();
		// Se a posição for -1, não há ninguém selecionado. Então cancelo
		// a operação
		if (pos < 0)
			return;
		// Informo ao controlador para iniciar o processo de exclusão
		try {
			ctrl.iniciarCasoDeUsoExcluirPassageiro(this.objetos.get(pos));
		} catch (ControleException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		} catch (DadosException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see viewer.UICadastroPassageiros#solicitarExecucaoDeAlterarPassageiro()
	 */
	@Override
	public void solicitarExecucaoDeAlterarPassageiro() {
		// Recupero a posição selecionada
		int pos = table.getSelectedRow();
		// Se a posição for -1, não há ninguém selecionado. Então cancelo
		// a operação
		if (pos < 0)
			return;
		// Informo ao controlador para iniciar o processo de alteração
		try {
			ctrl.iniciarCasoDeUsoAlterarPassageiro(this.objetos.get(pos));
		} catch (ControleException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		} catch (DadosException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see viewer.UICadastroPassageiros# solicitarTerminoDeManterPassageiros()
	 */
	@Override
	public void solicitarTerminoDeManterPassageiros() {
		try {
			ctrl.terminar();
		} catch (ControleException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see viewer.UICadastroPassageiros#fechar()
	 */
	@Override
	public void exibir() {
		this.setVisible(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see viewer.UICadastroPassageiros#fechar()
	 */
	@Override
	public void fechar() {
		this.setVisible(false);
	}

	/**
	 * Exemplo de manipulação de alterações no JTable
	 * 
	 * @param e
	 */
	public void tratarModificacaoNaTabela(TableModelEvent e) {
		if (e.getType() != TableModelEvent.UPDATE)
			return;
		int linha = e.getFirstRow();
		int coluna = e.getColumn();

		TableModel model = (TableModel) e.getSource();

		System.out.println("Você alterou a linha " + linha + ", coluna " + coluna);
		System.out.println("Valor da célula alterada: " + model.getValueAt(linha, coluna));
	}
}
