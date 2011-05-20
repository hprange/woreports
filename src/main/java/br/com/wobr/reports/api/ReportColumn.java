package br.com.wobr.reports.api;

import ar.com.fdvs.dj.domain.CustomExpression;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 * 
 */
public interface ReportColumn
{
	public Alignment alignment();

	public Class<? extends CustomExpression> customExpressionClass();

	public String fontColor();

	public String fontSize();

	public Boolean groupedBy();

	public Boolean hidden();

	public String keypath();

	public ReportModel model();

	public String pattern();

	public String title();

	public Integer width();
}
