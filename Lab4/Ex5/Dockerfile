# Stage 1: Build
FROM golang:1.22-alpine AS builder

WORKDIR /app

# COPY go.mod ./
# RUN go mod download

COPY . .
RUN go build -o app


# Stage 2: Run
FROM alpine:latest

WORKDIR /root/

COPY --from=builder /app/app .

EXPOSE 8080

CMD ["./app"]