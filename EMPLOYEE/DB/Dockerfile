FROM node:8.14.0

WORKDIR /app

COPY package.json ./app

RUN npm install

COPY . /app
ENV PORT 8080
CMD ["npm", "start"]