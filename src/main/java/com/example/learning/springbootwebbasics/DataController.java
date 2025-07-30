package com.example.learning.springbootwebbasics;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataController {

    @GetMapping("/products/{productId}")
    public String getProductById(@PathVariable String productId) {
        return "You requested product with ID: " + productId;
    }

    @GetMapping("/users/{userId}/orders/{orderId}")
    public String getUserOrder(@PathVariable String userId, @PathVariable String orderId) {
        return "User ID: " + userId + ", Order ID: " + orderId;
    }

    @GetMapping("/search")
    public String searchItems(
            @RequestParam String keyword,
            @RequestParam(required = false) Integer limit,
            @RequestParam(defaultValue = "asc") String sort) {
        String result = "Searching for: " + keyword;
        if (limit != null) {
            result += ", Limit: " + limit;
        }
        result += ", Sort order: " + sort;
        return result;
    }

    @GetMapping("/items")
    public String getItemsByCategory(@RequestParam(required = false) String category) {
        if (category != null) {
            return "Fetching items in category: " + category;
        } else {
            return "Fetching all items";
        }
    }

    @GetMapping("/filter")
    public String filterByIds(@RequestParam java.util.List<String> ids) {
        return "Filtering by IDs: " + String.join(", ", ids);
    }
}
