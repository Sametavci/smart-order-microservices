#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Wed Oct 15 10:50:18 2025
@author: sametavci
"""

import grpc
from concurrent import futures
import time
import sys
import payment_pb2
import payment_pb2_grpc


class PaymentService(payment_pb2_grpc.PaymentServiceServicer):
    def ProcessPayment(self, request, context):
        print(f"💳 Processing payment: Order {request.order_id}, Amount: {request.amount}", flush=True)
        return payment_pb2.PaymentResponse(
            success=True,
            transaction_id="TXN12345",
            message=f"Payment of {request.amount} for order {request.order_id} successful!"
        )


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    payment_pb2_grpc.add_PaymentServiceServicer_to_server(PaymentService(), server)
    server.add_insecure_port("0.0.0.0:50051")
    server.start()
    print("🚀 Payment gRPC server running on port 50051...", flush=True)
    sys.stdout.flush()

    try:
        while True:
            time.sleep(86400)
    except KeyboardInterrupt:
        print("🛑 Payment server shutting down...", flush=True)
        server.stop(0)


if __name__ == "__main__":
    serve()
