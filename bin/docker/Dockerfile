FROM lsiobase/ubuntu:bionic

ENV HOME="/config"

# package install
RUN \
    apt-get update && \
    apt-get install -y \
    sudo


# 
RUN sudo bash -c '\
    curl -SsL https://github.com/boxboat/fixuid/releases/download/v0.4/fixuid-0.4-linux-amd64.tar.gz | tar -C /usr/local/bin -xzf - && \
    chmod 4755 /usr/local/bin/fixuid && \
    mkdir -p /etc/fixuid && \
    printf "user: coder\ngroup: coder\n" > /etc/fixuid/config.yml'

# code-server source install
RUN curl -fOL https://github.com/cdr/code-server/releases/download/v3.4.1/code-server_3.4.1_amd64.deb
RUN sudo dpkg -i code-server_3.4.1_amd64.deb 
RUN locale-gen en_US.UTF-8
ENV LC_ALL=en_US.UTF-8 \
    SHELL=/bin/bash

# add sudo
RUN adduser --gecos '' --disabled-password coder && \
    # echo 'code:coder' | chpasswd \ 
    echo "coder ALL=(ALL) NOPASSWD:ALL" >> /etc/sudoers.d/nopasswd
USER coder

# config.yaml copy (bind-addr update)
COPY ./config.yaml /config/.config/code-server/config.yaml

# create & mount project dir
RUN mkdir -p /home/coder/projects
VOLUME ["/home/coder/projects"]


# start
EXPOSE 5111 
CMD ["code-server"]
