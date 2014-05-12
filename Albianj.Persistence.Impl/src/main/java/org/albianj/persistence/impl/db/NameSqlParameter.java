package org.albianj.persistence.impl.db;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameSqlParameter
{
	/**
	 * 分析处理带命名参数的SQL语句。使用Map存储参数，然后将参数替换成? <br/>
	 * @param sql
	 * @return
	 */
	public static void parseSql(ICommand cmd)
	{
		String regex = "#\\w+#";//insert into tablename(col1,col2) values( #col1#,#col2#)
		String cmdText = cmd.getCommandText();
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(cmdText);
		Map<Integer, String> paramsMap = new HashMap<Integer, String>();
		
		int idx = 1;
		while (m.find())
		{
			// 参数名称可能有重复，使用序号来做Key
			paramsMap.put(new Integer(idx++), m.group());
		}
		cmdText = cmdText.replaceAll(regex, "?");
		cmd.setCommandText(cmdText);
		cmd.setParameterMapper(paramsMap);
		return;
	}
}
