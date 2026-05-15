package i18n;

public enum Messages
{
	ACCEPT("Aceptar"), CANCEL("Cancelar"), EXIT("Salir"), FILE("Archivo"), STATUS("Estado"), COMPLETED("Completo");

	private String value;

	private Messages(String value)
	{
		this.value = value;
	}

	/**
	 * Retorna el texto del mensaje
	 * 
	 * @return texto del mensaje
	 */
	public String getValue()
	{
		return value;
	}

	/**
	 * Setea el valor texto del mensaje
	 * 
	 * @param valor texto del mensaje
	 */
	public void setValue(String valor)
	{
		this.value = valor;
	}

}
