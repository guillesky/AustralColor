package i18n;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Clase que guarda la informacion de todos los idiomas disponibles
 * 
 * @author Guillermo Lazzurri
 */
public class AllLanguages
{
	private HashMap<String, Language> languages = new HashMap<String, Language>();

	/**
	 * Agrega un idioma a la lista
	 * 
	 * @param language objeto de tipo Language que representa un idioma
	 */
	public void addLanguaje(Language language)
	{
		this.languages.put(language.getWords().get("LANGUAGE_NAME"), language);
	}

	/**
	 * Retorna un objeto de tipo Language correspondiente al idioma indicado por
	 * parametro
	 * 
	 * @param languageName String correspondiente al LANGUAGE_NAME del idioma
	 *                     solicitado
	 * @return retorna el objeto Language correspondiente al idioma indicado por
	 *         parametro, si no hay ningun idioma con ese nombre, retorna null
	 */
	public Language getLanguageforName(String languageName)
	{
		return this.languages.get(languageName);
	}

	public Language getLanguageforFileCodeName(String fileCode)
	{
		Language result = null;

		Iterator<Language> it = this.languages.values().iterator();
		Language language = null;
		if (it.hasNext())
			language = it.next();
		while (it.hasNext() && !language.getFileCode().equalsIgnoreCase(fileCode))
			language = it.next();
		if (language.getFileCode().equalsIgnoreCase(fileCode))
			result = language;
		return result;
	}

	/**
	 * Devuelve un Iterator con todos los nombres de idioma (LANGUAGE_NAME)
	 * registrados
	 * 
	 * @return Iterator[String] con todos los nombres de idioma (LANGUAGE_NAME)
	 *         registrados
	 */
	public Iterator<String> getLanguagesName()
	{
		return this.languages.keySet().iterator();
	}

	public Collection<Language> getLanguages()
	{
		return this.languages.values();
	}

}
