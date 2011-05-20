package br.com.wobr.reports.impl;

import ar.com.fdvs.dj.domain.CustomExpression;
import br.com.wobr.reports.api.Alignment;
import br.com.wobr.reports.api.ReportColumn;
import br.com.wobr.reports.api.ReportModel;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class DefaultReportColumn implements ReportColumn
{
	private Alignment alignment = Alignment.LEFT;

	private Class<? extends CustomExpression> customExpressionClass;

	private String fontColor;

	private String fontSize;

	private Boolean groupedBy = Boolean.FALSE;

	private Boolean hidden = Boolean.FALSE;

	private String keypath;

	private ReportModel model;

	private String pattern;

	private String title;

	private Integer width;

	public Alignment alignment()
	{
		return alignment;
	}

	public Class<? extends CustomExpression> customExpressionClass()
	{
		return customExpressionClass;
	}

	public String fontColor()
	{
		return fontColor;
	}

	public String fontSize()
	{
		return fontSize;
	}

	public Boolean groupedBy()
	{
		return groupedBy;
	}

	public Boolean hidden()
	{
		return hidden;
	}

	public String keypath()
	{
		return keypath;
	}

	public ReportModel model()
	{
		return model;
	}

	public String pattern()
	{
		return pattern;
	}

	public void setAlignment(Alignment alignment)
	{
		this.alignment = alignment;
	}

	public void setCustomExpressionClass(Class<? extends CustomExpression> customExpressionClass)
	{
		this.customExpressionClass = customExpressionClass;
	}

	public void setFontColor(String fontColor)
	{
		this.fontColor = fontColor;
	}

	public void setFontSize(String fontSize)
	{
		this.fontSize = fontSize;
	}

	public void setGroupedBy(Boolean groupedBy)
	{
		this.groupedBy = groupedBy;
	}

	public void setHidden(Boolean hidden)
	{
		this.hidden = hidden;
	}

	public void setKeypath(String keypath)
	{
		this.keypath = keypath;
	}

	public void setModel(ReportModel model)
	{
		this.model = model;
	}

	public void setPattern(String pattern)
	{
		this.pattern = pattern;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public void setWidth(Integer width)
	{
		this.width = width;
	}

	public String title()
	{
		return title;
	}

	public Integer width()
	{
		return width;
	}
}
