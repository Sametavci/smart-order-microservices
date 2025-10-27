FROM python:3.11-slim

WORKDIR /app

COPY . .

RUN pip install --no-cache-dir grpcio grpcio-tools

RUN apt-get update && apt-get install -y procps curl net-tools && rm -rf /var/lib/apt/lists/*


# gRPC portu
EXPOSE 50051

# Servisi başlat
CMD ["python3", "payment_service.py"]
