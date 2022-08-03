package com.example.demo.services.contracts;

import java.util.List;
import java.util.Map;


public interface ServiceContract {
    public List<?> get();
    public Object getBy(int id);
    // public List<Object> search(String keyword);
    public Object store(Map<String, String> params);
    public boolean update(int id, Map<String, String> params);
    public boolean destroy(int id);
}
