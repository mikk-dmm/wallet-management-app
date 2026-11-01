# Build Stage1
FROM node:24-alpine AS builder

WORKDIR /builder

COPY package.json package-lock.json ./

RUN npm ci

COPY . .

RUN npm run build

# Build Stage2
FROM nginx:alpine

COPY --from=builder /builder/dist/ /usr/share/nginx/html/

COPY nginx.conf /etc/nginx/conf.d/default.conf