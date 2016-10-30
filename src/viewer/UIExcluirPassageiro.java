package viewer;

public interface UIExcluirPassageiro extends UI {
	/**
	 * Solicita a efetiva��o da a��o de inclus�o ou altera��o
	 */
	public abstract void solicitarExecucaoDeEfetivacao();

	/**
	 * Solicita o cancelamento da a��o de inclus�o ou altera��o
	 */
	public abstract void solicitarCancelamentoDeEfetivacao();

	/**
	 * Atualiza os campos na UI
	 */
	public abstract void atualizarCampos(String nome, String dtNascimento, String cpf, String passaporte);
}
