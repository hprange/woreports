package com.woreports.api;

public interface ReportProcessor<T> {
    ReportRecipe<T> recipeFor(ReportModel model);
}
