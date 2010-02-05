package br.com.wobr.reports;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class ReportProcessingException extends Exception
{
	private static final long serialVersionUID = 1L;

	public ReportProcessingException()
	{
		super();
	}

	public ReportProcessingException(String message)
	{
		super(message);
	}

	public ReportProcessingException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ReportProcessingException(Throwable cause)
	{
		super(cause);
	}
}
