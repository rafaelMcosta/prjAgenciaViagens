package viewer;

public interface UILogin extends UI {
	/**
	 * Solicita a verificação do login
	 */
	public abstract void solicitarValidarLogin();

	/**
	 * Solicita o encerramento do login
	 */
	public abstract void solicitarEncerrarLogin();
}
