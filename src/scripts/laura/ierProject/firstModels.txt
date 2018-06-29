rm(list=ls())

setwd("C:\\LauraProjects_May_2018\\IER_Project\\whitneyOut")
library("nlme")

taxa <- c("genus" )
for( t in taxa )
{
	inFile <- paste0( "pcoa_",t, "Log10NormMetadataallData.txt")	
	myT <- read.table(inFile, sep="\t", header=TRUE)	
	numCols <- ncol(myT)
	myColClasses <- c("character",rep("numeric", which(names(myT)=="BarcodeSequence")-2),
						"character","character","character","numeric","numeric","numeric","character",
							"numeric","numeric","character","character","numeric","numeric","character",
							rep("numeric", numCols-which(names(myT)=="Num_Hits")+1 ))
							
							
	myT <-read.table(inFile,header=TRUE,sep="\t",colClasses=myColClasses)

	myT <- myT[!is.na(myT$TumorVolume ), ]
		
	pValuesTime <-vector()
	pValuesTumor <- vector()
	pValuesGroup<- vector()
	bugNames <- vector()
	bugIndex <- vector()
	index <- 1
	
	cage <- myT$Cage.number
	time <- myT$Time.point..weeks.
	time <- factor(time)
	volume <- myT$TumorVolume
	treatmentGroup <- myT$Treatment.Group
	treatmentGroup <- factor(treatmentGroup )
	
	treatmentColors <- vector()
	
	for( i in 1:length(treatmentGroup))
	{
		if( treatmentGroup[i]=="Control" )
			treatmentColors[i] = "RED"
		else
			treatmentColors[i] = "BLUE"
		
	}
	
	toRun <- c(2:(which(names(myT)=="BarcodeSequence")-2),which(names(myT)=="MDS1"):which(names(myT)=="MDS10"))
	
	for( i in toRun)
	{
		bug <- myT[,i]
		
		if( sum( bug != 0) > nrow(myT) /4 ) 
		{
			aFrame <- data.frame(cage,time,volume,bug)
			M.mixed <- lme( bug ~ time +volume+treatmentGroup, method = "REML", random = ~1 | cage, data =aFrame)
			
			pValuesTime[index] <-anova(M.mixed)$"p-value"[2]
			pValuesTumor[index] <- anova(M.mixed)$"p-value"[3]
			pValuesGroup[index] <- anova(M.mixed)$"p-value"[4]
			bugIndex[index] <- i
			bugNames[index] <- names(myT)[i]
	
			index = index + 1
		}
	}
	
	dFrame <- data.frame(	bugNames, pValuesTime,pValuesTumor,pValuesGroup,bugIndex) 
	
	dFrame <- dFrame [order(dFrame$pValuesGroup),]
	dFrame$pValuesAdjustedTime<- p.adjust( dFrame$pValuesTime, method = "BH" )
	dFrame$pValuesAdjustedTumor<- p.adjust( dFrame$pValuesTumor, method = "BH" )
	dFrame$pValuesAdjustedGroup<- p.adjust( dFrame$pValuesGroup, method = "BH" )	
		
	write.table(dFrame, file=paste0("secondModelsBugs",t,".txt"), row.names=FALSE, sep="\t")
	
	pdf(paste0(t,"_plot2ndModels.pdf"))
	par(mfrow=c(2,2))
	
	for( i in 1:nrow(dFrame))
	{
		bug <- myT[,dFrame$bugIndex[i]]
		
		aText <- paste0(  "time\n", dFrame$bugNames[i] ,  "\nq= ",format( dFrame$pValuesAdjustedTime[i],digits=3))
		boxplot( bug ~ time, main=aText,cex.main=.8,xlab="timepoint")
		
		myFrame <- data.frame( bug,time )
		
		stripchart(bug~ time, data = myFrame,vertical = TRUE, pch = 21, add=TRUE )		
			
		
		aText <- paste0(  "tumor volume" ,  "\nq= ",format( dFrame$pValuesAdjustedTumor[i],digits=3))
		plot( bug ~ volume,main=aText,cex.main=.8,xlab="tumor volume",col=treatmentColors ,pch=16, cex=1.3 )	
		
		aText <- paste0(  "treatment group" ,  "\nq= ",format( dFrame$pValuesAdjustedGroup[i],digits=3))
		boxplot( bug ~ treatmentGroup,main=aText,cex.main=.8,xlab="treatment group")	
		
		boxplot( bug ~ cage, main ="cage", xlab="cage",las=2)	
	}
	
	dev.off()
}

