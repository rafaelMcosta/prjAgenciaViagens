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

import control.CtrlManterFuncionarios;
import control.util.ControleException;
import model.util.DadosException;
import model.util.IDadosParaTabela;
import viewer.UICadastroFuncionarios;

public class JanelaCadastroFuncionarios extends JFrame implements UICadastroFuncionarios{

	/**
	 * Refer�ncia para o controlador do caso de uso Manter Funcionarios
	 */
	private CtrlManterFuncionarios ctrl;
	/**
	 * Lista de objetos a serem exibidos
	 */
	private List<IDadosParaTabela> objetos;
	/**
	 * Refer�ncia ao contentPane da Janela
	 */
	private JPanel contentPane;
	/**
	 * Refer�ncia ao JTable embutido na janela
	 */
	private JTable table;
	/**
	 * Refer�ncia para o TableModel da tabela
	 */
	private DefaultTableModel tableModel;

	/**
	 * Create the frame.
	 */
	public JanelaCadastroFuncionarios(CtrlManterFuncionarios c) {
		this.ctrl = c;
		this.criarUI();
	}

	/**
	 * Cria Visualmente a Janela
	 */
	@Override
	public void criarUI() {
		setTitle("Funcionarios");
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
				solicitarExecucaoDeIncluirFuncionario();
			}
		});
		btnIncluir.setBounds(100, 232, 89, 23);
		contentPane.add(btnIncluir);

		JButton btnExcluir = new JButton("Excluir");
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				solicitarExecucaoDeExcluirFuncionario();
			}
		});
		btnExcluir.setBounds(199, 232, 89, 23);
		contentPane.add(btnExcluir);

		JButton btnAlterar = new JButton("Alterar");
		btnAlterar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				solicitarExecucaoDeAlterarFuncionario();
			}
		});
		btnAlterar.setBounds(298, 232, 89, 23);
		contentPane.add(btnAlterar);

		JButton btnSair = new JButton("Sair");
		btnSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				solicitarTerminoDeManterFuncionarios();
			}
		});
		btnSair.setBounds(397, 232, 89, 23);
		contentPane.add(btnSair);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 586, 212);
		contentPane.add(scrollPane);

		table = new JTable();
		this.tableModel = new DefaultTableModel(null, new String[] { "Funcionarios" });
		table.setModel(this.tableModel);
		scrollPane.setViewportView(table);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see viewer.UICadastroFuncionarios#exibirObjetos(java.util.List)
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
	 * @see viewer.UICadastroFuncionarios#limpar()
	 */
	@Override
	public void limpar() {
		while (this.tableModel.getRowCount() > 0)
			this.tableModel.removeRow(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see viewer.UICadastroFuncionarios#solicitarExecucaoDeIncluirFuncionario()
	 */
	@Override
	public void solicitarExecucaoDeIncluirFuncionario() {
		try {
			this.ctrl.iniciarCasoDeUsoIncluirFuncionario();
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
	 * @see viewer.UICadastroFuncionarios#solicitarExecucaoDeExcluirFuncionario()
	 */
	@Override
	public void solicitarExecucaoDeExcluirFuncionario() {
		// Recupero a posi��o selecionada
		int pos = table.getSelectedRow();
		// Se a posi��o for -1, n�o h� ningu�m selecionado. Ent�o cancelo
		// a opera��o
		if (pos < 0)
			return;
		// Informo ao controlador para iniciar o processo de exclus�o
		try {
			ctrl.iniciarCasoDeUsoExcluirFuncionario(this.objetos.get(pos));
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
	 * @see viewer.UICadastroFuncionarios#solicitarExecucaoDeAlterarFuncionario()
	 */
	@Override
	public void solicitarExecucaoDeAlterarFuncionario() {
		// Recupero a posi��o selecionada
		int pos = table.getSelectedRow();
		// Se a posi��o for -1, n�o h� ningu�m selecionado. Ent�o cancelo
		// a opera��o
		if (pos < 0)
			return;
		// Informo ao controlador para iniciar o processo de altera��o
		try {
			ctrl.iniciarCasoDeUsoAlterarFuncionario(this.objetos.get(pos));
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
	 * @see viewer.UICadastroFuncionarios# solicitarTerminoDeManterFuncionarios()
	 */
	@Override
	public void solicitarTerminoDeManterFuncionarios() {
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
	 * @see viewer.UICadastroFuncionarios#fechar()
	 */
	@Override
	public void exibir() {
		this.setVisible(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see viewer.UICadastroFuncionarios#fechar()
	 */
	@Override
	public void fechar() {
		this.setVisible(false);
	}

	/**
	 * Exemplo de manipula��o de altera��es no JTable
	 * 
	 * @param e
	 */
	public void tratarModificacaoNaTabela(TableModelEvent e) {
		if (e.getType() != TableModelEvent.UPDATE)
			return;
		int linha = e.getFirstRow();
		int coluna = e.getColumn();

		TableModel model = (TableModel) e.getSource();

		System.out.println("Voc� alterou a linha " + linha + ", coluna " + coluna);
		System.out.println("Valor da c�lula alterada: " + model.getValueAt(linha, coluna));
	}
}
