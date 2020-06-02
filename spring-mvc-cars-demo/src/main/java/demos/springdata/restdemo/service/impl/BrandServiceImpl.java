package demos.springdata.restdemo.service.impl;

import demos.springdata.restdemo.dao.BrandRepository;
import demos.springdata.restdemo.dao.UserRepository;
import demos.springdata.restdemo.exception.EntityNotFoundException;
import demos.springdata.restdemo.model.Brand;
import demos.springdata.restdemo.service.BrandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandRepository brandRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public Collection<Brand> getBrands() {
        return brandRepo.findAll();
    }

    @Override
    public Brand getBrandById(Long id) {
        return brandRepo.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Brand with ID=%s not found.", id)));
    }

    @Override
    public Brand createBrand(@Valid Brand brand) {
        if(brand.getCreated() == null) {
            brand.setCreated(new Date());
        }
        brand.setModified(brand.getCreated());

        return brandRepo.save(brand);
    }

    @Override
    public Brand updateBrand(Brand brand) {
        brand.setModified(new Date());
        Brand old = getBrandById(brand.getId());
        return brandRepo.save(brand);
    }

    @Override
    public Brand deleteBrand(Long id) {
        Brand old = brandRepo.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Brand with ID=%s not found.", id)));
        brandRepo.deleteById(id);
        return old;
    }

    @Override
    public long getBrandsCount() {
        return brandRepo.count();
    }

    // Declarative transaction
    @Transactional
    public List<Brand> createBrandsBatch(List<Brand> brands) {
        List<Brand> created = brands.stream()
                .map(brand -> {
                    Brand resultBrand = createBrand(brand);
                    return resultBrand;
                }).collect(Collectors.toList());
        return created;
    }

 }
