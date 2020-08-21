package com.zkq.blog.service;

import com.zkq.blog.NotFoundException;
import com.zkq.blog.dao.TagRepository;
import com.zkq.blog.po.Blog;
import com.zkq.blog.po.Tag;
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
public class TagServiceImp implements TagService{

    @Autowired
    private TagRepository repository;


    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        return  repository.save(tag);
    }

    @Transactional
    @Override
    public Tag getTag(Long id) {
        return repository.getOne(id);
    }

    @Transactional
    @Override
    public Tag getTagByName(String name) {
        return repository.findByName(name);
    }

    @Transactional
    @Override
    public Page<Tag> listTag(Pageable pageable) {

        return repository.findAll(pageable);
    }

    @Override
    public List<Tag> listTag() {
        return repository.findAll();
    }

    @Override
    public List<Tag> listTagTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "blogs.size");
        Pageable pageable= PageRequest.of(0,size,sort);
        List<Tag> tags=repository.findTop(pageable);
        for (int j=0;j<tags.size();j++){
            List<Blog> blogs=tags.get(j).getBlogs();
            List<Blog> blogs1=new ArrayList<>();
            for (int i=0;i<blogs.size();i++){
                Blog b=blogs.get(i);
                if(b.isPublished()){
                    blogs1.add(b);
                }
            }
            tags.get(j).setBlogs(blogs1);
        }
        return tags;
    }

    @Override
    public List<Tag> listTag(String ids) {//1,2,3
        return repository.findAllById(convertToList(ids));
    }

    private  List<Long> convertToList(String ids){
        List<Long> list=new ArrayList<>();
        if(!"".equals(ids) && ids!=null){
            String[] idarray=ids.split(",");
            for (int i=0;i<idarray.length;i++){
                list.add(new Long(idarray[i]));
            }
        }
        return list;
    }



    @Transactional
    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag t=repository.getOne(id);
        if(t==null){
            throw new NotFoundException("不存在该类型");
        }
        BeanUtils.copyProperties(tag,t);
        return repository.save(t);
    }

    @Transactional
    @Override
    public void deleteTag(Long id) {
        repository.deleteById(id);
    }
}
