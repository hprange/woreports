package br.com.wobr.reports.api;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 * @param <T>
 */
public interface ReportExporter<T>
{
	public byte[] export( T document ) throws ReportProcessingException;
}
