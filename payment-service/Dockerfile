FROM python:3.11-slim

WORKDIR /app

# Gereken Python bağımlılıklarını yükle
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# 🧠 Sistem araçlarını ekle (ps, netstat, curl)
RUN apt-get update && apt-get install -y procps curl net-tools && rm -rf /var/lib/apt/lists/*

# Uygulama dosyalarını kopyala
COPY . .

# gRPC portu
EXPOSE 50051

# Servisi başlat
CMD ["python", "payment_service.py"]
