package com.zkq.blog.service;

import com.zkq.blog.NotFoundException;
import com.zkq.blog.dao.TypeRepository;
import com.zkq.blog.po.Blog;
import com.zkq.blog.po.Type;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class TypeServiceImpl implements TypeService{

    @Autowired
    private TypeRepository repository;


    @Override
    public List<Type> listTypeTop(Integer size) {
        Type type=new Type();
        Sort sort = Sort.by(Sort.Direction.DESC, "blogs.size");
        Pageable pageable=PageRequest.of(0,size,sort);
        List<Type> types=repository.findTop(pageable);
        for (int j=0;j<types.size();j++){
            List<Blog> blogs=types.get(j).getBlogs();
            List<Blog> blogs1=new ArrayList<>();
            for (int i=0;i<blogs.size();i++){
                Blog b=blogs.get(i);
                if(b.isPublished()){
                    blogs1.add(b);
                }
            }
            types.get(j).setBlogs(blogs1);
        }
        return types;
    }

    @Override
    public List<Type> listType() {
        return repository.findAll();
    }

    @Transactional
    @Override
    public Type saveType(Type type) {
        return repository.save(type);
    }

    @Override
    public Type getTypeByName(String name) {
        return repository.findByName(name);
    }

    @Transactional
    @Override
    public Type getType(Long id) {
        return repository.getOne(id);
    }

    @Transactional
    @Override
    public Page<Type> listType(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional
    @Override
    public Type updateType(Long id, Type type) {
        Type t=repository.getOne(id);
        if(t==null){
            throw new NotFoundException("不存在该类型");
        }
        BeanUtils.copyProperties(type,t);
        return repository.save(t);
    }

    @Transactional
    @Override
    public void deleteType(Long id) {
        repository.deleteById(id);
    }
}
