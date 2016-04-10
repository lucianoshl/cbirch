library(knitr)
args <- commandArgs(trailingOnly = TRUE)

originFile = args[1]
options(echo=TRUE)
report_folder = paste0(dirname(originFile),'/',gsub('.json','',basename(originFile)))
dir.create(report_folder)
opts_knit$set(base.dir = report_folder)
outFile = paste0(report_folder,'/report.html')
knit('report.Rhtml',outFile)
##print(outFile)