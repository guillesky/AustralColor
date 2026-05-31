package i18n;

import java.util.HashMap;

import javax.swing.JTextArea;

import core.Environment;

public class Language
{
    private String fileCode;
    private HashMap<String, String> words = new HashMap<String, String>();

    /**
     * Retorna el codigo de archivo (es = espanol; en= english; etc.)
     * 
     * @return codigo de archivo (es = espanol; en= english; etc.)
     */
    public String getFileCode()
    {
	return fileCode;
    }

    /**
     * Retorna el HashMap [String, String] con todos los valores de los mensajes del
     * idioma. Los mensajes se correponden al enumerate Messages
     * 
     * @return HashMap [String, String] con todos los valores de los mensajes del
     *         idioma. Los mensajes se correponden al enumerate Messages
     */
    public HashMap<String, String> getWords()
    {
	return words;
    }

    public void setMessages()
    {

	for (Messages key : Messages.values())
	{
	    String msg = this.getWords().get(key.name());
	    if (key == Messages.CREDITS_TEXT)
	    {
		String credistText = Environment.APP_NAME + "\n" + Environment.VERSION + msg;
		Messages.CREDITS_TEXT.setValue(credistText);
	    } else
	    {
		if (msg != null)
		{
		    key.setValue(msg);
		} else
		{
		    key.setValue("???" + key.name());
		}
	    }
	}

    }

    public static Language getLanguage(String fileCode)
    {
	Language language = new Language();
	language.fileCode = fileCode;
	for (Messages key : Messages.values())
	{
	    language.words.put(key.name(), key.getValue());
	}
	return language;
    }

    @Override
    public String toString()
    {
	return this.words.get("LANGUAGE_NAME");
    }

}
