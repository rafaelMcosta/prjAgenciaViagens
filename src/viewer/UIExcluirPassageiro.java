package viewer;

public interface UIExcluirPassageiro extends UI {
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
	public abstract void atualizarCampos(String nome, String dtNascimento, String cpf, String passaporte);
}
