package i18n;

public enum Messages
{
	ACCEPT("Aceptar"), CANCEL("Cancelar"), EXIT("Salir"), FILE("Archivo"), STATUS("Estado"), COMPLETED("Completo"),
	ADD_FILES("Agregar Archivos"), DELETE("Eliminar"), STOP("Detener"), OPTIONS("Opciones"),
	OVERWRITE("Sobrescribir Archivos Existentes"), IGNORE("Ignorar Archivos Existentes"),
	RENAME("Renombrar Archivos Existentes"), PROGRESS("Progreso"), QUEUED("En Cola"), PROCESSING("Procesando..."),
	CANCELED("Cancelado"), ANALYZING("Analizando..."), CORRECTED("Corregido"), OUTPUT_FOLDER("Carpeta Destino"),
	CHANGE_OUTPUT_FOLDER("Cambiar Carpeta Destino"), SELECT_OUTPUT_FOLDER("Seleccionar Carpeta Destino"),
	START_TASKS("Iniciar Procesos"), SELECT_MEDIA_FILES("Seleccionar Imagenes y Videos"),
	VIDEO_FILES("Archivos de Video"), IMAGE_FILES("Archivos de Imagenes"), MEDIA_FILES("Archivos de Imagen y Video"),
	OUTPUT_FILE("Archivo Destino"), PERCENT("_porciento"), CANCELING("Cancelando..."), FINISHING("Finalizando..."),
	FPS_LEGEND("Frames Procesados por Segundo: "), CONFIRM_EXIT_MESSAGE("Esta seguro que desea salir?"),
	CONFIRM_EXIT_MESSAGE_WITH_PENDING_THREADS(
			"Esta seguro que desea salir?\n(Los procesos pendientes seran cancelados)"),
	EXIT_DIALOG_TITLE("Salir"), EXITING("Saliendo"),
	EXITING_MESSAGE("Cancelando procesos pendientes, por favor espere..."),
	TASK_CANCELED(" Correccion de Color Cancelada para "), TASK_COMPLETED(" Correccion de Color Completa para "),
	MILLISECONDS(" Milisegundos"), UNKNOWN_FILES("Archivos no reconocidos:\n"),
	ALREADY_QUEUED_FILES("Los siguientes archivos ya estaban en cola:\n"),
	ALREADY_EXISTING_FILES_OVERWRITTEN("Los siguientes archivos ya existian y seran sobrescritos:\n"),
	ALREADY_EXISTING_FILES_IGNORED("Los siguientes archivos ya existian y seran ignorados:\n"),
	ALREADY_EXISTING_FILES_RENAMED("Los siguientes archivos ya existian y seran procesados con los nombres:\n"),
	ADDED("Se agregaron "), NEW_FILES_TO_PROCESS(" nuevos archivos para procesar.\n"),
	CANCELED_TASK_OF("Cancelada la correccion de: "), LANGUAGE_NAME("Español"), LANGUAGE("Idioma"),
	SIMULTANEOUS_PROCESSES("Procesos Simultaneos"), CREDITS("Creditos"), CREDITS_TEXT(
			"AustralColor\nVersion 1.0.0\n\nAplicacion desarrollada para la correccion automatica de color en imagenes y videos submarinos.\n\nAutor:\nGuillermo Lazzurri\n\nDesarrollado en:\nJava\n\nTecnologias utilizadas:\n- Java Swing\n- OpenCV\n- FFmpeg\n- Gson\n- Multithreading\n\nCaracteristicas:\n- Correccion automatica de color para fotografia y video submarino\n- Procesamiento concurrente\n- Soporte multilenguaje\n- Procesamiento parcial y cancelacion segura\n- Compatible con Windows y Android\n\nDisenado y desarrollado en Argentina.\n\nAgradecimientos:\nA la comunidad de buceo, fotografia submarina y desarrollo open source.\n\n2026");

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
