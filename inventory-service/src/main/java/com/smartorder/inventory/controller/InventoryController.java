import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final String host;
    private final int port;

    public InventoryController(
            @Value("${inventory.grpc.host}") String host,
            @Value("${inventory.grpc.port}") int port) {
        this.host = host;
        this.port = port;
    }

    @GetMapping("/check")
    public String checkStock(@RequestParam String productId, @RequestParam int quantity) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();

        InventoryServiceGrpc.InventoryServiceBlockingStub stub =
                InventoryServiceGrpc.newBlockingStub(channel);

        StockRequest request = StockRequest.newBuilder()
                .setProductId(productId)
                .setQuantity(quantity)
                .build();

        StockResponse response = stub.checkStock(request);
        channel.shutdown();
        return response.getMessage();
    }
}
