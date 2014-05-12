package org.albianj.persistence.impl.db;

import java.util.Map;



public class Command implements ICommand
{
	private String commandText = null;
	private CommandType commandType = CommandType.Text;
	private Map<String,ISqlParameter> paramsters = null;
	private Map<Integer,String> parameterMapper = null; 
	@Override
	public String getCommandText()
	{
		// TODO Auto-generated method stub
		return this.commandText;
	}

	@Override
	public void setCommandText(String commandText)
	{
		// TODO Auto-generated method stub
		this.commandText = commandText;
	}

	@Override
	public CommandType getCommandType()
	{
		// TODO Auto-generated method stub
		return this.commandType;
	}

	@Override
	public void setCommandType(CommandType commandType)
	{
		// TODO Auto-generated method stub
		this.commandType = commandType;
	}

	public Map<Integer,String> getParameterMapper()
	{
		return this.parameterMapper;
	}
	public void setParameterMapper(Map<Integer,String> parameterMapper)
	{
		this.parameterMapper = parameterMapper;
	}
	
	@Override
	public Map<String, ISqlParameter> getParameters()
	{
		// TODO Auto-generated method stub
		return this.paramsters;
	}

	@Override
	public void setParameters(Map<String, ISqlParameter> parameters)
	{
		// TODO Auto-generated method stub
		this.paramsters = parameters;
	}

}
