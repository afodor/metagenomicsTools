
rm(list=ls())

#the output of ReduceToTopTaxa.java

setwd("D:\\Erin\\Jan2013_run");
t <- read.table("phylaTop6SamplesAsColumns.txt",header=TRUE,row.names=1)

str(t)

labels <- c("Proteobacteria",
"Firmicutes",
"Bacteroidetes",
"Actinobacteria",
"Lentisphaerae",
"Verrucomicrobia",
"other")


pie(t$human1, labels=labels)
pie(t$human3, labels=labels)
pie(t$human13)