package org.albianj.persistence.impl.db;

import org.albianj.persistence.impl.context.IWriterJob;

public interface ITransactionClusterScope
{
	public boolean execute(IWriterJob writerJob);
}
