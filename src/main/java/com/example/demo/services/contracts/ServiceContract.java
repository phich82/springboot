package com.example.demo.services.contracts;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;


@Service
public interface ServiceContract {
    public List<?> findBy(Map<String, ?> params);
    public Object find(Object id);
    public Object store(Object params);
    public boolean update(Object id, Map<String, Object> params);
    public boolean update(Object entity);
    public boolean destroy(Object id);
}
