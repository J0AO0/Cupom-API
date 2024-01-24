package com.joao.cafe.serviceImpl;

import com.google.common.base.Strings;
import com.joao.cafe.JWT.JwtFilter;
import com.joao.cafe.POJO.Category;
import com.joao.cafe.constants.CafeConstants;
import com.joao.cafe.dao.CategoryDao;
import com.joao.cafe.service.CategoryService;
import com.joao.cafe.utils.CafeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requesMap) {
        try{
            if(jwtFilter.isAdmin()){
                if(validadeCategoyMap(requesMap,false)){
                    categoryDao.save(getCategoryFromMap(requesMap, false));
                    return CafeUtils.getResponseEntity("Categoria Adicionada com Sucesso.", HttpStatus.OK);
                }
            }
            else{
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESSS, HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validadeCategoyMap(Map<String, String> requesMap, boolean validateId) {
        if(requesMap.containsKey("name")){
            if(requesMap.containsKey("id") && validateId){
                return true;
            } else if(!validateId) {
                return true;
            }
        }
        return false;
    }

    private Category getCategoryFromMap(Map<String, String> requesMap, boolean isAdd){
        Category category = new Category();
        if (isAdd) {
            category.setId(Integer.parseInt(requesMap.get("id")));
        }
        category.setName(requesMap.get("name"));
        return category;
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
        try{
            if(!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true")){
                log.info("Dentro do if");
                return new ResponseEntity<List<Category>>(categoryDao.getAllCategory(), HttpStatus.OK);
            }
            return new ResponseEntity<>(categoryDao.findAll(), HttpStatus.OK);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<List<Category>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin()) {
                if(validadeCategoyMap(requestMap, true)){
                    Optional optional = categoryDao.findById(Integer.parseInt(requestMap.get("id")));
                    if(!optional.isEmpty()) {
                        categoryDao.save(getCategoryFromMap(requestMap, true));
                        return CafeUtils.getResponseEntity("Categoria alterada com sucesso.",HttpStatus.OK);
                    } else {
                        return CafeUtils.getResponseEntity("Categoria não encontrada.", HttpStatus.OK);
                    }
                }
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
            else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESSS, HttpStatus.UNAUTHORIZED);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
