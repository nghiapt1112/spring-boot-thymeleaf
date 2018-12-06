package com.lyna.web.domain.product.repository.impl;

import com.lyna.commons.infrustructure.repository.BaseRepository;
import com.lyna.web.domain.product.Product;
import com.lyna.web.domain.product.repository.ProductRepository;
import com.lyna.web.domain.stores.repository.impl.StoreRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
public class ProductRepositoryImpl extends BaseRepository<Product, Long> implements ProductRepository {

    private final Logger log = LoggerFactory.getLogger(StoreRepositoryImpl.class);

    @PersistenceContext
    private EntityManager em;

    public ProductRepositoryImpl(EntityManager em) {
        super(Product.class, em);
    }

    @Override
    public void updateProduct(Product product) {
        try {
            String hql = "UPDATE Product p set p.tenantId = :tenantId, p.updateUser = :updateUser, p.updateDate = :updateDate,"
                    + "p.code = :code,p.name = :name, p.unit = :unit, p.price = :price, p.category1 = :category1,"
                    + "p.category2 = :category2, p.category3 = :category3 WHERE p.productId=:productId";
            entityManager.createQuery(hql)
                    .setParameter("tenantId", product.getTenantId())
                    .setParameter("updateUser", product.getUpdateUser())
                    .setParameter("updateDate", product.getUpdateDate())
                    .setParameter("name", product.getName())
                    .setParameter("unit", product.getUnit())
                    .setParameter("price", product.getPrice())
                    .setParameter("category1", product.getCategory1())
                    .setParameter("category2", product.getCategory2())
                    .setParameter("category3", product.getCategory3())
                    .setParameter("productId", product.getProductId())
                    .executeUpdate();
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public Product findOneByProductId(String productId) {
        return entityManager
                .createQuery("SELECT p FROM Product p WHERE p.productId=:productId", Product.class)
                .setParameter("productId", productId)
                .getSingleResult();
    }

    @Override
    public boolean deletebyProductId(List<String> listProductId) {
        try {
            String query = "DELETE FROM Product p WHERE p.productId in (:listProductId)";
            entityManager.createQuery(query).setParameter("listProductId", listProductId).executeUpdate();
            return true;
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public List<String> getListProductByProductCode(int tenantId, List<String> products) {
        return entityManager
                .createQuery("SELECT p FROM Product p WHERE p.tenantId = :tenantId and  p.code in (:products)")
                .setParameter("tenantId", tenantId)
                .setParameter("products", products)
                .getResultList();
    }
}
