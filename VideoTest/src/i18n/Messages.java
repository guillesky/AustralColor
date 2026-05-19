package i18n;

public enum Messages
{
	ACCEPT("Aceptar"), CANCEL("Cancelar"), EXIT("Salir"), FILE("Archivo"), STATUS("Estado"), COMPLETED("Completo"),
	ADD_FILES("Agregar Archivos"), DELETE("Eliminar"), STOP("Detener"), OPTIONS("Opciones"),
	OVERWRITE("Sobreescribir archivos existentes"), IGNORE("Ignorar Archivos Existentes"),
	RENAME("Renombrar Archivos Existentes"), PROGRESS("Progreso"), QUEUED("En Cola"), PROCESSING("Prosesando"),
	CANCELED("Cancelado"), ANALIZING("Analizando"), CORRECTED("Corregido"), OUTPUT_FOLDER("Carpeta Destino"),
	CHANGE_OUTPUT_FOLDER("Cambiar Carpeta Destino"), SELECT_OUTPUT_FOLDER("Seleccionar Carpeta Destino"),
	START_TASKS("Iniciar Procesos"), SELECT_MEDIA_FILES("Seleccionar Imagenes y Videos"),
	VIDEO_FILES("Archivos de Video"), IMAGE_FILES("Archivos de Imagenes"), MEDIA_FILES("Archivos de Imagen y Video");

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
