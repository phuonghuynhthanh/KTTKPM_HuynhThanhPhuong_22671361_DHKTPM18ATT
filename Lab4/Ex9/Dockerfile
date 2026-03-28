# Sử dụng image redis mới nhất
FROM redis:latest

# Copy file config vào container
COPY redis.conf /usr/local/etc/redis/redis.conf

# Chạy redis với config tùy chỉnh
CMD ["redis-server", "/usr/local/etc/redis/redis.conf"]