package com.reactive.mongo.reactive_mongo.service;

import com.reactive.mongo.reactive_mongo.dto.ProductDto;
import com.reactive.mongo.reactive_mongo.repository.ProductRepository;
import com.reactive.mongo.reactive_mongo.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;


    public Flux<ProductDto> getProducts(){

        Flux<ProductDto> productDtoFlux = productRepository.findAll().map(AppUtils::entityToDto);
//        System.out.println(productDtoFlux);
        return productRepository.findAll().map(AppUtils::entityToDto);
    }

    public Mono<ProductDto> getProduct(String id){
        return productRepository.findById(id).map(AppUtils::entityToDto);
    }

    public Flux<ProductDto> getProductInRange(double min,double max){
        return productRepository.findByPriceBetween(Range.closed(min,max));
    }

    public Mono<ProductDto> saveProduct(Mono<ProductDto> productDtoMono){
         Mono<ProductDto> productDto = productDtoMono.map(AppUtils::dtoToEntity)
                .flatMap(productRepository::insert)
                .map(AppUtils::entityToDto);

         return productDto;
    }

    public Mono<ProductDto> updateProduct(Mono<ProductDto> productDtoMono,String id){
       return  productRepository.findById(id)
                .flatMap(p->productDtoMono.map(AppUtils::dtoToEntity)
                    .doOnNext(e->e.setId(id)))
                .flatMap(productRepository::save)
                .map(AppUtils::entityToDto);
    }

    public Mono<Void> deleteProduct(String id){
        return productRepository.deleteById(id);
    }
}


