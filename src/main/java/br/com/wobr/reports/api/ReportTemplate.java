package br.com.wobr.reports.api;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 * 
 * @param <T>
 */
public interface ReportTemplate<T>
{
	public T build(ReportModel model);
}
