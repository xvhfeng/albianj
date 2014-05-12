package org.albianj.service.parser;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.albianj.logger.AlbianLoggerService;
import org.albianj.service.AlbianServiceAttribute;
import org.albianj.service.IAlbianServiceAttribute;
import org.albianj.verify.Validate;
import org.albianj.xml.XmlParser;
import org.dom4j.Element;

public class ServiceParser extends FreeServiceParser {

	private final static String ID_ATTRBUITE_NAME = "Id";
	private final static String TYPE_ATTRBUITE_NAME = "Type";

	@Override
	public void loading() {
		super.init();
		super.loading();
	}

	@Override
	protected Map<String, IAlbianServiceAttribute> parserServices(
			String tagName, @SuppressWarnings("rawtypes") List nodes) {
		if (Validate.isNullOrEmpty(nodes)) {
			AlbianLoggerService.error("the %1$s node is empty.", tagName);
			throw new IllegalArgumentException("node is null.");
		}
		Map<String, IAlbianServiceAttribute> map = new LinkedHashMap<String, IAlbianServiceAttribute>(
				nodes.size());
		String name = null;
		for (Object node : nodes) {
			Element elt = XmlParser.toElement(node);
			name = null == name ? "tagName" : name;
			IAlbianServiceAttribute serviceAttr = parserService(name, elt);
			if (null == serviceAttr) {
				AlbianLoggerService.error("parser the node next id:%1$s is fail.", name);
				throw new NullPointerException("parser service node is error.");
			}
			name = serviceAttr.getId();
			map.put(name, serviceAttr);
		}
		return 0 == map.size() ? null : map;
	}

	@Override
	protected IAlbianServiceAttribute parserService(String name, Element elt) {
		if (null == elt) {
			String msg = "The node is null";
			AlbianLoggerService.error("parser the node next id:%1$s is fail.", name);
			throw new IllegalArgumentException(msg);
		}
		IAlbianServiceAttribute serviceAttr = new AlbianServiceAttribute();
		String id = XmlParser.getAttributeValue(elt, ID_ATTRBUITE_NAME);
		if (Validate.isNullOrEmptyOrAllSpace(id)) {
			AlbianLoggerService.error("parser the node is fail.the node is is null or empty,the node next id:%1$s.", name);
			return null;
		}
		serviceAttr.setId(id);
		String type = XmlParser.getAttributeValue(elt, TYPE_ATTRBUITE_NAME);
		if (Validate.isNullOrEmptyOrAllSpace(type)) {
			AlbianLoggerService.error("The", serviceAttr.getId(),
					"Type is null or empty.");
			return null;
		}
		serviceAttr.setType(type);
		return serviceAttr;
	}

}
