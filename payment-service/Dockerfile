FROM python:3.11-slim

WORKDIR /app
COPY . .

RUN pip install --no-cache-dir grpcio grpcio-tools

EXPOSE 50051

CMD ["python3", "payment_service.py"]

