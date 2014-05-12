package org.albianj.persistence.impl.context;

public enum WriterJobLifeTime
{
	Normal,
    NoStarted,
    Opening,
    Opened,
    Running,
    Runned,
    Commiting,
    Commited,
    Rollbacking,
    Rollbacked,
}
