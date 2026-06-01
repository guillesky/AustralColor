package util;

public class Config
{
	private String languageFile = "es";
	private int maxSimultaneousProcessing = 2;

	public String getLanguageFile()
	{
		return languageFile;
	}

	public int getMaxSimultaneousProcessing()
	{
		return maxSimultaneousProcessing;
	}

	public Config(String languageFile, int maxSimultaneousProcessing)
	{
		this.languageFile = languageFile;
		this.maxSimultaneousProcessing = maxSimultaneousProcessing;
	}
	
	public Config() 
	{
	}
	

}
