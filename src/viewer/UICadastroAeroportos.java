package viewer;

import java.util.List;

import model.util.IDadosParaTabela;

public interface UICadastroAeroportos extends UI{

	/**
	 * Exibe os objetos na UI
	 * 
	 * @param objetos
	 */
	public abstract void exibirObjetos(List<IDadosParaTabela> objetos);

	/**
	 * Solicita a execução do caso de uso Incluir
	 */
	public abstract void solicitarExecucaoDeIncluirAeroporto();

	/**
	 * Solicita a execução do caso de uso Excluir
	 */
	public abstract void solicitarExecucaoDeExcluirAeroporto();

	/**
	 * Solicita a execução do caso de uso Alterar
	 */
	public abstract void solicitarExecucaoDeAlterarAeroporto();

	/**
	 * Solicita o término da execução do caso de uso Manter
	 */
	public abstract void solicitarTerminoDeManterAeroportos();
}
