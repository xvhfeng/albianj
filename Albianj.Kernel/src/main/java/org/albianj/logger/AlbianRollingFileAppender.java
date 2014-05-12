package org.albianj.logger;

import java.io.IOException;

import org.albianj.datetime.DateTime;
import org.apache.log4j.RollingFileAppender;

public class AlbianRollingFileAppender extends RollingFileAppender
{
	protected String format = "yyyyMMddHHmmss";
	protected String suffix="log";
	protected String fileName = "albianj";
	
	
	 public String getFormat()
	{
		return format;
	}


	public void setFormat(String format)
	{
		this.format = format;
	}


	public String getSuffix()
	{
		return suffix;
	}


	public void setSuffix(String suffix)
	{
		this.suffix = suffix;
	}


	public String getFileName()
	{
		return fileName;
	}


	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}


	public
	  synchronized
	  void setFile(String fileName, boolean append, boolean bufferedIO, int bufferSize)
	                                                                 throws IOException {
		if(fileName.endsWith(this.suffix))
		{
			fileName = fileName.substring(0,fileName.lastIndexOf(this.getFileName()));
		}
	    StringBuilder sbFileName = new StringBuilder();
//			 SimpleDateFormat dateFormat=new SimpleDateFormat(this.format);   
			 sbFileName.append(fileName)
			 .append(this.getFileName()).append("_").append(DateTime.getDateTimeString())
			 .append(".").append(this.suffix);	    	
		 
		 super.setFile(sbFileName.toString(), append, this.bufferedIO, this.bufferSize);
	  }
}
