package com.lyna.web.domain.product.repository.impl;

import com.lyna.commons.infrustructure.exception.DomainException;
import com.lyna.commons.infrustructure.repository.BaseRepository;
import com.lyna.web.domain.product.Product;
import com.lyna.web.domain.product.repository.ProductRepository;
import com.lyna.web.domain.stores.repository.impl.StoreRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Transactional
public class ProductRepositoryImpl extends BaseRepository<Product, Long> implements ProductRepository {

    private final Logger log = LoggerFactory.getLogger(StoreRepositoryImpl.class);

    public ProductRepositoryImpl(EntityManager em) {
        super(Product.class, em);
    }


    @Override
    public Product findOneByProductId(String productId) throws DomainException {
        try {
            return entityManager
                    .createQuery("SELECT p FROM Product p WHERE p.productId=:productId", Product.class)
                    .setParameter("productId", productId)
                    .getSingleResult();
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean deletebyProductId(List<String> listProductId) throws DomainException {
        try {
            String query = "DELETE FROM Product p WHERE p.productId in (:listProductId)";
            entityManager.createQuery(query).setParameter("listProductId", listProductId).executeUpdate();
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public Product findOneByCode(String code) throws DomainException {
        try {
            return entityManager
                    .createQuery("SELECT p FROM Product p WHERE p.code=:code", Product.class)
                    .setParameter("code", code)
                    .getSingleResult();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Product> findAll(int tenantId) throws DomainException {
        return entityManager
                .createQuery("SELECT p FROM Product p WHERE p.tenantId=:tenantId order by p.name", Product.class)
                .setParameter("tenantId", tenantId)
                .getResultList();
    }
}
