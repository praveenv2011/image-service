package com.sample.image.httprequest;

import com.sample.image.dto.ProductDTO;
import com.sample.image.exception.ApplicationException;
import com.sample.image.exception.ProductFetchException;
import com.sample.image.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
public class HttpRequest {

    @Value("${product.service.url}")
    private String productServiceUrl;

    private RestTemplate restTemplate;

    public HttpRequest(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

   public ProductDTO getProductById(Long id) throws ProductFetchException, ApplicationException {
       String productServiceUrl = this.productServiceUrl + id;
         if (id == null || id <= 0) {
              throw new ProductFetchException("Invalid product ID: " + id, HttpStatus.BAD_REQUEST);
         }
       try {
           return restTemplate.getForObject(productServiceUrl, ProductDTO.class);
       } catch (HttpClientErrorException e) {
           throw new ProductNotFoundException("product with id " + id + " not found", HttpStatus.NOT_FOUND);
       } catch (HttpServerErrorException e) {
           throw new ProductFetchException("Error fetching product with id " + id, HttpStatus.INTERNAL_SERVER_ERROR);
       } catch (ResourceAccessException e) {
           throw new ProductFetchException("Product service is currently unavailable", HttpStatus.SERVICE_UNAVAILABLE);
       }catch (Exception e) {
           throw new ApplicationException("An unexpected error occurred while fetching product with id " + id, HttpStatus.INTERNAL_SERVER_ERROR);
       }
   }
}
