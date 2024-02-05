FROM node:21-alpine

WORKDIR /usr/src/app

COPY . /usr/src/app

RUN npm install -g @angular/cli

RUN npm install

CMD ng serve --serve-path=/application/user/ --host=0.0.0.0
