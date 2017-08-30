package com.harvey.common.base;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Id;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 公共持久类
 * Created by lhw on 2017/4/14.
 */
@Repository
public abstract class BaseHibernateDao<E, PK extends Serializable> implements EntityDao<E, PK>{

    private Class<E> entityClass;
    private String idFieldName;
    private Class<?> idFieldType;
    private boolean idOrderable;

    /**
     * 应用到反射技术
     * Class<? super T> getSuperclass():返回本类的父类
     * Type getGenericSuperclass():返回本类的父类,包含泛型参数信息
     */
    public BaseHibernateDao(){
        Class typeCls = this.getClass();
        Type genType;
        for(genType = typeCls.getGenericSuperclass(); !(genType instanceof ParameterizedType); genType = typeCls.getGenericSuperclass()) {
            typeCls = typeCls.getSuperclass();
        }

        this.entityClass = (Class)((ParameterizedType)genType).getActualTypeArguments()[0];//获取泛型的一个参数，即实体类
        Field[] fields = this.entityClass.getDeclaredFields();
        Field[] arr = fields;
        int len = fields.length;

        for(int i = 0; i < len; ++i) {
            Field field = arr[i];
            if(field.isAnnotationPresent(Id.class)) {
                this.idFieldName = field.getName();
                this.idFieldType = field.getType();
            }
        }
        this.idOrderable = this.idFieldType != null && (this.idFieldType.isAssignableFrom(Integer.class) || this.idFieldType.isAssignableFrom(String.class) || this.idFieldType.isAssignableFrom(Long.class));
    }


    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected Session getSession(){
        //需要开启事务，才能得到CurrentSession
        return sessionFactory.getCurrentSession();
    }

    protected Class<E> getEntityClass() {
        return entityClass;
    }

    @Override
    public E getById(PK id) {
        return (E)this.getSession().get(this.getEntityClass(),id);
    }

    @Override
    public void deleteById(PK id) {
        Object obj = this.getById(id);
        if(obj != null) {
            this.delete((E)obj);
        }
    }

    @Override
    public void delete(E obj) {
        this.getSession().delete(obj);
    }

    @Override
    public void save(final E entity) {
        this.getSession().save(entity);
    }

    @Override
    public void update(E entity) {
        this.getSession().saveOrUpdate(entity);
    }

    @Override
    public void saveOrUpdate(E entity) {
        this.saveOrUpdate(entity);
    }

    @Override
    public void flush() {
        this.getSession().flush();
    }

    @Override
    public void clear() {
        this.getSession().clear();
    }

    @Override
    public List<E> findAll() {
        Criteria criteria = this.getSession().createCriteria(this.getEntityClass());
        if(this.idOrderable) {
            criteria.addOrder(Order.asc(this.idFieldName));
        }
        return criteria.list();
    }

    @Override
    public List<E> findByKey(String keyName, Object value) {
        Criteria criteria = this.getSession().createCriteria(this.getEntityClass()).add(Restrictions.eq(keyName, value));
        if(this.idOrderable) {
            criteria.addOrder(Order.asc(this.idFieldName));
        }

        return criteria.list();
    }
}
