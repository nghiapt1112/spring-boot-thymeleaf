package com.lyna.web.domain.product.repository.impl;

import com.lyna.commons.infrustructure.repository.BaseRepository;
import com.lyna.web.domain.product.Product;
import com.lyna.web.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class ProductRepositoryImpl extends BaseRepository<Product, String> implements ProductRepository {

    public ProductRepositoryImpl(EntityManager em) {
        super(Product.class, em);
    }


    @Transactional
    public Product findOneByProductIdAndTenantId(String productId, int tenantId) {
        return entityManager
                .createQuery("SELECT p FROM Product p WHERE p.productId=:productId AND p.tenantId=:tenantId", Product.class)
                .setParameter("productId", productId)
                .setParameter("tenantId", tenantId)
                .getSingleResult();
    }

    @Override
    public boolean deleteByProductIdsAndTenantId(List<String> productIds, int tenantId) {

        String query = "DELETE FROM Product p WHERE p.productId in (:listProductId) AND p.tenantId=:tenantId";
        entityManager.createQuery(query)
                .setParameter("listProductId", productIds)
                .setParameter("tenantId", tenantId).executeUpdate();
        return true;
    }

    @Override
    public Product findOneByCodeAAndTenantId(String code, int tenantId) {
        List products = entityManager
                .createQuery("SELECT p FROM Product p WHERE p.code=:code And p.tenantId = :tenantId", Product.class)
                .setParameter("code", code)
                .setParameter("tenantId", tenantId)
                .getResultList();
        return (products != null && products.size() > 0) ? (Product) products.get(0) : null;
    }


    @Override
    public List<Product> findByTenantId(int tenantId) {
        return entityManager
                .createQuery("SELECT p FROM Product p WHERE p.tenantId=:tenantId order by p.name", Product.class)
                .setParameter("tenantId", tenantId).getResultList();
    }

    @Transactional
    public List<String> getListProductCodeByProductCode(int tenantId, List<String> products) {
        return entityManager
                .createQuery("SELECT p.code FROM Product p WHERE p.tenantId = :tenantId and  p.code in (:products)")
                .setParameter("tenantId", tenantId)
                .setParameter("products", products)
                .getResultList();
    }

    @Override
    public List<Product> getProductsByProductCode(int tenantId, List<String> products) {
        return entityManager
                .createQuery("SELECT p FROM Product p WHERE p.tenantId = :tenantId and  p.code in (:products)")
                .setParameter("tenantId", tenantId)
                .setParameter("products", products)
                .getResultList();
    }
}
