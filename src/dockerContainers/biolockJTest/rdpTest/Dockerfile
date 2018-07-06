# build with something like
#docker image build . -t rdptest
# d
# instantiate with something like docker container run -it rdptest:latest /bin/bash
# remove: docker rmi name
# list: docker images

# help from https://github.com/mjzapata/BioLockJDocker

FROM ubuntu:18.04


#1.) Directory structure
RUN mkdir /data/ && mkdir /data/results \
	&& mkdir /databases/ && mkdir /sample_datasets/ \
	&& mkdir /apps/ \
	&& mkdir /config/
ENV DATABASE_DIR=/databases APPS=/apps
ARG DEBIAN_FRONTEND=noninteractive


#2.) ======== Basic pre-reqs for most ubuntu systems =================== #
RUN apt-get update && \
	apt-get install -y build-essential \
	checkinstall \
	apt-utils \
	software-properties-common \
	git \
	unzip \
	ca-certificates \
	bsdtar \
	wget \
	nano

#4.) =================    Java   ======================== #  
# Add the appropriate repository, and autoselect menu options to avoid user interaction during install.
RUN apt-get update && \
   apt-get upgrade -y && \
   apt-get install -y  software-properties-common && \
   add-apt-repository ppa:webupd8team/java -y && \
   apt-get update && \
   echo oracle-java7-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections && \
   apt-get install -y oracle-java8-installer && \
   apt-get clean


#10.) =======================  RDP  ========================== #  
#RDP 2.12 (direct download link may break if it's a temporary link... look here: https://sourceforge.net/projects/rdp-classifier/)
#shorten the rdp link for formatting reasons
RUN cd /apps/ && \
	RDPLINKPT1="https://downloads.sourceforge.net/project/rdp-classifier/rdp-classifier/rdp_classifier_2.12.zip" && \
	RDPLINKPT2="?r=https%3A%2F%2Fsourceforge.net%2Fprojects%2Frdp-classifier%2Ffiles%2Frdp-classifier%2Frdp_classifier_2.12.zip%2Fdownload&ts=1525888784" && \
	wget -qO- $RDPLINKPT1$RDPLINKPT2 | bsdtar -xvf- && cd /

# grab the latest git
RUN apt-get install ant

RUN cd /apps/ && \
	git fetch && \
	git checkout -f origin && \
	ant

	
