package org.adaschool.api.controller.product;

import org.adaschool.api.exception.ProductNotFoundException;
import org.adaschool.api.repository.product.Product;
import org.adaschool.api.repository.product.ProductDto;
import org.adaschool.api.service.product.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/products/")
public class ProductsController {

    @Autowired
    private ProductsService productsService;


    public ProductsController(@Autowired ProductsService productsService) {
        this.productsService = productsService;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductDto productDto) {
        Product newProduct = productsService.save(new Product(productDto));
        URI createdProductUri = URI.create("/v1/products/" + newProduct.getId());
        return ResponseEntity.created(createdProductUri).body(newProduct);
    }


    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
            List<Product> products = productsService.all();
        return ResponseEntity.ok(products);
    }

    @GetMapping("{id}")
    public ResponseEntity<Product> findById(@PathVariable("id") String id) {
        Optional<Product> productOptional = productsService.findById(id);
        if (productOptional.isPresent()){
            return ResponseEntity.ok(productOptional.get());
        } else {
            throw new ProductNotFoundException(id);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable ("id") String id, @RequestBody ProductDto productDto) {
        Optional<Product> optionalProduct = productsService.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.update(productDto);
            Product updatedUser = productsService.save(product);
            return ResponseEntity.ok(updatedUser);
        } else {
            throw new ProductNotFoundException(id);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") String id) {
        Optional<Product> existingProduct = productsService.findById(id);
        if (existingProduct.isPresent()) {
            productsService.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            throw new ProductNotFoundException(id);
        }

    }
}
