library(knitr)
args <- commandArgs(trailingOnly = TRUE)

originFile = args[1]
outFile = gsub('.json','.html',args[1])
options(echo=TRUE)
knit2html('report.Rhtml',outFile)
print(outFile)