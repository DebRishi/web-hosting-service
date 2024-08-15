FROM ubuntu:noble

RUN apt update
RUN apt install git -y
RUN apt install curl -y
RUN curl -sL https://deb.nodesource.com/setup_20.x | bash -
RUN apt install nodejs -y

WORKDIR /home/app

COPY package*.json .
RUN npm ci --omit=dev

COPY script.sh script.sh
COPY script.js script.js
COPY .env .env

RUN chmod +x script.sh
RUN chmod +x script.js

ENTRYPOINT [ "/home/app/script.sh" ]