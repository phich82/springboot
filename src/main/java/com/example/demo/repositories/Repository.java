package com.example.demo.repositories;

import java.util.List;
import java.util.Map;

import com.example.demo.exceptions.NotFoundException;

public class Repository {

    public Object find(int id) {
        return null;
    }

    public Object findOrFailed(int id) {
        throw new NotFoundException();
    }

    public List<?> get() {
        return null;
    }

    public Object getBy(int id) {
        return this.findOrFailed(id);
    }

    public List<?> search(String keyword) {
        return null;
    }

    public Object store(Map<String, String> params) {
        return null;
    }

    public boolean update(int id, Map<String, String> params) {
        return false;
    }

    public boolean destroy(int id) {
        return false;
    }
}
