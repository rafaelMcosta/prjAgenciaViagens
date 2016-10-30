package viewer;

public interface UIPassageiro extends UI{
	/**
	 * Solicita a efetivação da ação de inclusão ou alteração
	 * 
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	public abstract void solicitarExecucaoDeEfetivacao();

	/**
	 * Solicita o cancelamento da ação de inclusão ou alteração
	 */
	public abstract void solicitarCancelamentoDeEfetivacao();

	/**
	 * Atualiza os campos na UI
	 */
	public abstract void atualizarCampos(String nome, String cpf, String passaporte, String dtNascimento);
}
