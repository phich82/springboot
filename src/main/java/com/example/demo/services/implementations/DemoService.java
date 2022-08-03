package com.example.demo.services.implementations;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.demo.models.resources.UserResource;
import com.example.demo.repositories.DemoRepository;
import com.example.demo.services.contracts.ServiceContract;


@Service()
public class DemoService implements ServiceContract {

    @Autowired
    protected DemoRepository demoRepository;

    public UserResource find(int id) {
        return this.demoRepository.find(id);
    }

    public UserResource findOrFailed(int id) {
        return this.demoRepository.findOrFailed(id);
    }

    @Override
    public List<UserResource> get() {
        return this.demoRepository.get();
    }

    @Override
    public UserResource getBy(int id) {
        return this.demoRepository.findOrFailed(id);
    }

    public List<UserResource> search(String keyword) {
        return this.demoRepository.search(keyword);
    }

    @Override
    public UserResource store(Map<String, String> params) {
        return this.demoRepository.store(params);
    }

    @Override
    public boolean update(int id, Map<String, String> params) {
        return this.demoRepository.update(id, params);
    }

    @Override
    public boolean destroy(int id) {
        return this.demoRepository.destroy(id);
    }
}
