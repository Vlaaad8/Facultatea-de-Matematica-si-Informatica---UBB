public class Application {

    // Main method for testing the cache
    public static void main(String[] args) throws InterruptedException {
        ExpiringCache<String, String> cache = new ExpiringCache<>(1000); // Cleanup every 1 second

        // Add items to the cache with different expiration times
        cache.put("key1", "value1", 3000); // Expires in 3 seconds
        cache.put("key2", "value2", 5000); // Expires in 5 seconds
        cache.put("key3", "value3", 1000); // Expires in 1 second

        // Access and print cache values
        System.out.println("key1: " + cache.get("key1")); // Should print "value1"
        System.out.println("key2: " + cache.get("key2")); // Should print "value2"
        System.out.println("key3: " + cache.get("key3")); // Should print "value3"

        // Wait for items to expire
        Thread.sleep(2000);
        System.out.println("After 2 seconds:");
        System.out.println("key1: " + cache.get("key1")); // Should print "value1"
        System.out.println("key3: " + cache.get("key3")); // Should print null (expired)

        // Wait for cleanup
        Thread.sleep(3000);
        System.out.println("After 5 seconds:");
        System.out.println("key1: " + cache.get("key1")); // Should print null (expired)
        System.out.println("key2: " + cache.get("key2")); // Should print null (expired)

        // Shut down the cache cleaner
        cache.shutdown();
    }
}
