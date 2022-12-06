# ==== CONFIGURE =====
# Use a Node 16 base image
FROM node:16-alpine
COPY . .
# ==== BUILD =====
# Install dependencies (npm ci makes sure the exact versions in the lockfile gets installed)
RUN node --max-old-space-size=200 $(which npm) ci
# Build the app
RUN node --max-old-space-size=200 $(which npm) run build
# Expose the port on which the app will be running (3000 is the default that `serve` uses)
EXPOSE 3000
# Start the app
CMD [ "npx", "serve", "build" ]