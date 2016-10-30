package viewer;

public interface UIExcluirAeroporto extends UI {
	/**
	 * Solicita a efetivação da ação de inclusão ou alteração
	 */
	public abstract void solicitarExecucaoDeEfetivacao();

	/**
	 * Solicita o cancelamento da ação de inclusão ou alteração
	 */
	public abstract void solicitarCancelamentoDeEfetivacao();

	/**
	 * Atualiza os campos na UI
	 */
	public abstract void atualizarCampos(String sigla, String cidade, String estado, String pais);
}
