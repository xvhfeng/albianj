package org.albianj.persistence.impl.db;

import java.util.Map;


public interface ICommand
{
	public String getCommandText();
	public void setCommandText(String commandText);
	public CommandType getCommandType();
	public void setCommandType(CommandType commandType);
	public Map<Integer,String> getParameterMapper();
	public void setParameterMapper(Map<Integer,String> parameterMapper);
	public Map<String,ISqlParameter> getParameters();
	public void setParameters(Map<String,ISqlParameter> parameters);
}
