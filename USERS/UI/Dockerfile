FROM node:8.14.0

WORKDIR /app

EXPOSE 3000

COPY package.json /app

RUN npm install

COPY . /app
CMD ["npm", "start"]
