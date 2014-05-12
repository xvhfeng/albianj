package org.albianj.persistence.impl.persistence;

public class PersistenceAttributeException extends RuntimeException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6883346258879546366L;

	/**
	 * 
	 */

	public PersistenceAttributeException()
	{
		super();
	}

	/**
	 * @param msg
	 */
	public PersistenceAttributeException(String msg)
	{
		super(msg);
	}

	/**
	 * @param msg
	 * @param cause
	 */
	public PersistenceAttributeException(String msg, Throwable cause)
	{
		super(msg, cause);
	}

	/**
	 * @param cause
	 */
	public PersistenceAttributeException(Throwable cause)
	{
		super(cause);
	}
}
