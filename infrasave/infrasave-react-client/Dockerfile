# ==== CONFIGURE =====
# Use a Node 16 base image
FROM node:16-alpine
ARG REACT_APP_BASE_URL
COPY . .
# ==== BUILD =====
# Install dependencies (npm ci makes sure the exact versions in the lockfile gets installed)
RUN npm ci
# Build the app
ENV REACT_APP_BASE_URL=$REACT_APP_BASE_URL
RUN npm run build
# Expose the port on which the app will be running (3000 is the default that `serve` uses)
EXPOSE 3000
# Start the app
CMD [ "npx", "serve", "build" ]