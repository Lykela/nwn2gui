package org.stanwood.nwn2.gui.options;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public abstract class Settings {
	private final static Log log = LogFactory.getLog(Settings.class);
	private final static Properties props = new Properties();
		
	protected Settings() {
		load();
	}	
	
	protected abstract File getPropertiesPath();
		
	public void setString(String key,List<String> list) {
		setInteger(key+".count", list.size());
		for (int i=0;i<list.size();i++) {
			setString(key+".value."+i, list.get(i));			
		}
	}
	
	public List<String> getStringList(String key) 
	{
		int count = getInteger(key+".count",0);
		
		List<String>result = new ArrayList<String>();
		for (int i=0;i<count;i++) {
			String str = getString(key+".value."+i,"");
			if (!str.isEmpty()) {
				result.add(str);
			}
		}
		return result;
	}
	
	public void setFileList(String key,List<File> list) {
		setInteger(key+".count", list.size());
		for (int i=0;i<list.size();i++) {
			setString(key+".value."+i, list.get(i).getAbsolutePath());	
		}
	}
	
	public List<File> getFileList(String key) 
	{
		int count = getInteger(key+".count",0);
		
		List<File>result = new ArrayList<File>();
		for (int i=0;i<count;i++) {
			String file = getString(key+".value."+i,"");
			if (!file.isEmpty()) {
				result.add(new File(file));
			}
		}
		return result;
	}
	
	
	public String getString(String key,String defaultValue) {
		return props.getProperty(key,defaultValue);
	}
	
	public void setString(String key,String value) {
		props.setProperty(key, value);
	}
	
	public int getInteger(String key,int defaultValue) {
		return Integer.parseInt(props.getProperty(key, String.valueOf(defaultValue)));
	}
	
	public void setInteger(String key,int value) {
		props.setProperty(key, String.valueOf(value));
	}
	
	public void save() {		
		File settingsPath = getPropertiesPath();		
		FileOutputStream fs = null;
		try {
			if (!settingsPath.getParentFile().mkdirs() && !settingsPath.getParentFile().exists()) {
				throw new IOException("Unable to create settings dir: " + settingsPath.getParent());
			}
		
			fs = new FileOutputStream(settingsPath);
			props.store(fs,"NWN2Gui Settings file");
		}
		catch (IOException e) {
			log.error("Unable to load user settings: " +e.getMessage(),e);
		}
		finally {
			if (fs!=null) {
				try {
					fs.close();
				} catch (IOException e) {
					log.error("Unable to close settings file: " + e.getMessage(),e);
				}
			}
		}
		
		if (log.isDebugEnabled()) {
			log.debug("Settings saved: " + settingsPath);
		}
	}
	
	public void load() {		
		File settingsPath = getPropertiesPath();
		if (settingsPath.exists()) {
			
			FileInputStream fs = null;
			try {
				fs = new FileInputStream(settingsPath);
				props.load(fs);
			}
			catch (IOException e) {
				log.error("Unable to load user settings: " +e.getMessage(),e);
			}
			finally {
				if (fs!=null) {
					try {
						fs.close();
					} catch (IOException e) {
						log.error("Unable to close settings file: " + e.getMessage(),e);
					}
				}
			}
			if (log.isDebugEnabled()) {
				log.debug("Settings loaded: " + settingsPath);
			}
		}	
	}	
	
}