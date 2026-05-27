package core;

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
		super();
		this.languageFile = languageFile;
		this.maxSimultaneousProcessing = maxSimultaneousProcessing;
	}
	

}
