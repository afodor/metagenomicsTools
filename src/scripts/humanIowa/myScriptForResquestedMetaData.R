rm(list=ls())
setwd("C:\\iowaHuman_May_2018\\aaronJuly19_2018")
library("vegan")
#read in data
myT <- read.table("iowaRdpMetaGenusLognorm.tsv", sep="\t", header=TRUE,row.names=1)

#just the bacteria
myTData <- myT[,1:35]

#PCOA of just the bacteria
myPCOA <- capscale(myTData~1,distance="bray")
#Save our data
write.table(myPCOA$CA$u, sep="\t", file=paste("genus_allData",".txt",sep=""))
write.table(myPCOA$CA$eig,file=paste("genus_",".txt", sep=""), sep="\t")	

myMerge <- cbind( myT, myPCOA$CA$u)
write.table(myMerge , sep="\t", file=paste("genus_withMDS",".txt",sep=""),row.names=FALSE)
pValues <-vector()
pcoaColumns <- vector()
metaNames <- vector()
metaIndex <- vector()
pcoaIndex <- vector()
index <- 1

metaDataOfInterest = strsplit('AP_step_count,adlib,dfiber,pre_BMI,pre_WBTOT_FAT,pre_WBTOT_LEAN,pre_TRUNK_FAT,moderate_time,vigorous_time,tee,aee,tcho,tfat,tpro,alc,calcEI,POMS_Ang,POMS_Con,POMS_Dep,POMS_Fat,POMS_TMD,POMS_Ten,POMS_Vig,Measured_Sleep_Minutes,sedentary_length_30,sedentary_length_60,min_sedentary',',')
for( m in metaDataOfInterest[[1]]){
  
  for( p in 1:5 ){
    aLm <- lm( as.numeric(unlist(myMerge[m])) ~  myPCOA$CA$u[,p])
    pValues[index]  <-1 #to be changes in the following line of code
    
    try( pValues[index] <- anova(aLm)$"Pr(>F)"[1])
    metaNames[index] <-m
    pcoaColumns[index] <- p
    pcoaIndex[index] <- p
    #metaIndex[index] <- which(colnames(myMerge) == m,TRUE) #why doesn't work?
    index = index + 1
  }#end for
}
dFrame <- data.frame(pValues,pcoaColumns,metaNames,pcoaIndex)
dFrame <- dFrame [order(dFrame$pValues),]
dFrame$pValuesAdjusted<- p.adjust( dFrame$pValues, method = "BH" )	

write.table(dFrame, file="specificMetadataGenus.txt", row.names=FALSE, sep="\t")