package com.parcialuno.backend.controllers;

import com.parcialuno.backend.dto.ErrorDTO;
import com.parcialuno.backend.dto.ProductDTO;
import com.parcialuno.backend.mappers.ProductMapper;
import com.parcialuno.backend.models.Category;
import com.parcialuno.backend.models.Product;
import com.parcialuno.backend.services.CategoryService;
import com.parcialuno.backend.services.EmailService;
import com.parcialuno.backend.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController
{
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private EmailService emailService;

    @GetMapping("/")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<Product> products = productService.findAll();
        List<ProductDTO> productDTOs = products.stream()
                .map(productMapper::toDto)
                .toList();
        return new ResponseEntity<>(productDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Integer id) {
        Optional<Product> product = productService.findById(id);
        return product.map(value -> new ResponseEntity<>(productMapper.toDto(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/")
    public ResponseEntity<Object> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        Optional<Category> categoryOptional = categoryService.findById(productDTO.getCategoryId());
        if (!categoryOptional.isPresent()) {
            ErrorDTO errorDTO = new ErrorDTO("La categoría con ID " + productDTO.getCategoryId() + " no existe");
            return ResponseEntity.badRequest().body(errorDTO);
        }

        Product product = productMapper.toEntity(productDTO);
        Product newProduct = productService.save(product);
        emailService.sendEmail("dieguinquip@gmail.com", "Producto creado", "Se ha creado un producto");
        return new ResponseEntity<>(productMapper.toDto(newProduct), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Integer id, @Valid @RequestBody ProductDTO productDTO) {
        Product product = productMapper.toEntity(productDTO);
        product.setId(id);
        Product updatedProduct = productService.update(id, product);
        if (updatedProduct != null) {
            return new ResponseEntity<>(productMapper.toDto(updatedProduct), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProduct(@RequestParam Integer id) {
        productService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/file/{id}")
    public ResponseEntity<?> saveFile(@PathVariable("id") Integer id, @RequestParam(name = "file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO("El archivo está vacío."));
        }

        // Buscar el producto por ID
        Optional<Product> optionalProduct = productService.findById(id);
        if (!optionalProduct.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("Producto no encontrado."));
        }

        Product product = optionalProduct.get();

        // Guardar el archivo en una ubicación específica (por ejemplo, 'uploads/')
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        // Actualizar el campo "image" del producto
        product.setImage(fileName);
        Product updatedProduct = productService.save(product);

        // Convertir el producto actualizado a ProductDTO
        ProductDTO productDTO = productMapper.toDto(updatedProduct);

        // Devolver el ProductDTO actualizado con la imagen
        return ResponseEntity.ok(productDTO);
    }
}