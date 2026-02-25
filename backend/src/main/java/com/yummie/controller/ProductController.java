package com.yummie.controller;

import com.yummie.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/category/{category}")
    public ResponseEntity<?> getProductList(@PathVariable String category,
                                            @RequestParam String filter,
                                            @RequestParam int page) {
        return productService.getProductResponseList(category, filter, page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable long id) {
        return productService.getProductResponse(id);
    }
}

