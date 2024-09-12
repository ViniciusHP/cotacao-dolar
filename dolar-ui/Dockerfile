FROM node:18.20.4-alpine as build-app

WORKDIR /app-build

COPY . .

RUN npm i \
    && npx ng build --configuration=production

FROM nginx:stable-alpine3.19-slim

COPY --from=build-app /app-build/server/nginx.conf /etc/nginx/nginx.conf
COPY --from=build-app /app-build/server/mime.types /etc/nginx/mime.types
COPY --from=build-app /app-build/dist/dolar-ui /usr/share/nginx/html

ENV API_URL='http://localhost:8080'

EXPOSE 80

CMD ["/bin/sh",  "-c", "envsubst < /usr/share/nginx/html/assets/env.template.js > /usr/share/nginx/html/assets/env.js && exec nginx -g 'daemon off;'"]