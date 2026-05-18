package i18n;

import java.util.HashMap;

/**
 * Clase que representa un idioma durante el juego
 * 
 * @author Guillermo Lazzurri
 */
public class Language {
	private String fileCode;
	private HashMap<String, String> words = new HashMap<String, String>();

	/**
	 * Retorna el codigo de archivo (es = espanol; en= english; etc.)
	 * 
	 * @return codigo de archivo (es = espanol; en= english; etc.)
	 */
	public String getFileCode() {
		return fileCode;
	}

	/**
	 * Retorna el HashMap [String, String] con todos los valores de los mensajes del
	 * idioma. Los mensajes se correponden al enumerate Messages
	 * 
	 * @return HashMap [String, String] con todos los valores de los mensajes del
	 *         idioma. Los mensajes se correponden al enumerate Messages
	 */
	public HashMap<String, String> getWords() {
		return words;
	}

	public static void i18n(Language language) {

		for (Messages key : Messages.values()) {
			String msg = language.getWords().get(key.name());
			if (msg != null) {
				key.setValue(msg);
			} else {
				key.setValue("???" + key.name());
			}
		}
	}

	public static Language getLanguage() {
		Language language = new Language();
		language.fileCode = "es";
		for (Messages key : Messages.values()) {
			language.words.put(key.name(), key.getValue());
		}
		return language;
	}

	public static void i18n(String languageCode) {
		String path = "i18n/" + languageCode + ".json";
		for (Messages key : Messages.values()) {

		}
		/*
		 * if (!file.exists()) { throw new RuntimeException("Language file not found: "
		 * + path); }
		 * 
		 * Json json = new Json(); ObjectMap<String, String> loaded =
		 * json.fromJson(ObjectMap.class, file);
		 * 
		 * for (Messages key : Messages.values()) { String msg = loaded.get(key.name());
		 * if (msg != null) { key.setValue(msg); } else { key.setValue("???" +
		 * key.name()); } }
		 */
	}

	@Override
	public String toString() {
		return "Language [fileCode=" + fileCode + ", words=" + words + "]";
	}

}
