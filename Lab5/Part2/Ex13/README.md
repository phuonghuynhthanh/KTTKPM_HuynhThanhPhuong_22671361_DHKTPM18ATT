# Lab 5 - Part 2 - Ex13

Bai 13: Chay ung dung React va serve bang Nginx.

## Cach chay

### Chay bang Docker Compose (khuyen dung)

```bash
docker compose up --build -d
```

Dung container:

```bash
docker compose down
```

### Build Docker image

```bash
docker build -t ex13-react-nginx .
```

### Chay container

```bash
docker run -p 8080:80 ex13-react-nginx
```

Mo trinh duyet tai:

```bash
http://localhost:3000
```
