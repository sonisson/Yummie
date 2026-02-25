package com.yummie.service;

import com.yummie.converter.ProductConverter;
import com.yummie.entity.ProductEntity;
import com.yummie.repository.ProductRepository;
import com.yummie.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductConverter productConverter;
    private final PageUtil pageUtil;

    public ResponseEntity<?> getProductResponseList(String category, String filter, int page) {
        Sort sort;
        if ("newest".equals(filter)) {
            sort = Sort.by("createAt").descending();
        } else {
            sort = Sort.by("rating").descending();
        }
        Page<ProductEntity> productEntityPage;
        if ("menu".equals(category)) {
            productEntityPage = pageUtil.getPage(page, 20, sort,
                    productRepository::findAll);
        } else {
            productEntityPage = pageUtil.getPage(page, 20, sort,
                    pageable -> productRepository.findByCategoryEntityName(category, pageable));
        }
        return ResponseEntity.ok(productConverter.toProductResponsePage(productEntityPage));
    }

    public ResponseEntity<?> getProductResponse(long id) {
        ProductEntity productEntity = productRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found")
        );
        return ResponseEntity.ok(productConverter.toProductResponse(productEntity));
    }
}
