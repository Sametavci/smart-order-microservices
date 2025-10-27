FROM python:3.11-slim

WORKDIR /app

COPY . .

# Anında log için
ENV PYTHONUNBUFFERED=1

# Gereken Python paketleri
RUN pip install --no-cache-dir grpcio grpcio-tools

# .proto'dan Python dosyaları oluştur (varsa overwrite et)
RUN python3 -m grpc_tools.protoc -I. --python_out=. --grpc_python_out=. payment.proto

# Yardımcı araçlar
RUN apt-get update && apt-get install -y procps curl net-tools && rm -rf /var/lib/apt/lists/*

EXPOSE 50051

# Debug için: Python hata stack trace’leri logta görünsün
ENTRYPOINT ["python3", "-u", "payment_service.py"]
