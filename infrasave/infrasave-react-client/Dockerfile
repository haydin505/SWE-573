# ==== CONFIGURE =====
# Use a Node 16 base image
FROM node:16-alpine as builder
ARG REACT_APP_BASE_URL
COPY . .
# ==== BUILD =====
# Install dependencies (npm ci makes sure the exact versions in the lockfile gets installed)
RUN npm ci
# Build the app
ENV REACT_APP_BASE_URL=$REACT_APP_BASE_URL
RUN npm run build


FROM nginx:1.21.0-alpine as production
ENV NODE_ENV production
# Copy built assets from `builder` image
COPY --from=builder build /usr/share/nginx/html
# Add your nginx.conf
COPY nginx.conf /etc/nginx/conf.d/default.conf
# Expose port
EXPOSE 80
# Start nginx
CMD ["nginx", "-g", "daemon off;"]