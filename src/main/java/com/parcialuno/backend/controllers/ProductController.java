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

    @GetMapping("/all")
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

    @PostMapping
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

    @PostMapping("/file")
    public ResponseEntity<String> saveFile(@RequestParam(name = "file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El archivo está vacío.");
        }

        try {
            // Guardar el archivo en el sistema de archivos
            String filePath = "C:\\Users\\HP\\OneDrive\\Desktop\\pruebasImgSpring/" + file.getOriginalFilename();
            file.transferTo(new File(filePath));
            return ResponseEntity.ok("Archivo subido con éxito: " + file.getOriginalFilename());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al subir el archivo.");
        }
    }
}