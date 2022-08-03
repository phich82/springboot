package com.example.demo.services.implementations;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.models.resources.TestResource;
import com.example.demo.repositories.DemoRepository;
import com.example.demo.services.contracts.ServiceContract;


@Component
public class ExampleService implements ServiceContract {

    @Autowired
    protected DemoRepository demoRepository;

    public TestResource find(int id) {
        return this.demoRepository.find(id);
    }

    public TestResource findOrFailed(int id) {
        return this.demoRepository.findOrFailed(id);
    }

    @Override
    public List<TestResource> get() {
        return this.demoRepository.get();
    }

    @Override
    public TestResource getBy(int id) {
        return this.demoRepository.findOrFailed(id);
    }

    public List<TestResource> search(String keyword) {
        return this.demoRepository.search(keyword);
    }

    @Override
    public TestResource store(Map<String, String> params) {
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
