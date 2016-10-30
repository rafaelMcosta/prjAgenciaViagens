package viewer;

public interface UIAeroporto extends UI {
	/**
	 * Solicita a efetiva��o da a��o de inclus�o ou altera��o
	 * 
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	public abstract void solicitarExecucaoDeEfetivacao();

	/**
	 * Solicita o cancelamento da a��o de inclus�o ou altera��o
	 */
	public abstract void solicitarCancelamentoDeEfetivacao();

	/**
	 * Atualiza os campos na UI
	 */
	public abstract void atualizarCampos(String sigla, String cidade, String estado, String pais);
}
