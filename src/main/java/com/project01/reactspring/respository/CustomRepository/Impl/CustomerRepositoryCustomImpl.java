package com.project01.reactspring.respository.CustomRepository.Impl;

import com.project01.reactspring.dto.request.BuildingRequestDTO;
import com.project01.reactspring.dto.response.CustomerResponseDTO;
import com.project01.reactspring.entity.BuildingEntity;
import com.project01.reactspring.entity.CustomerEntity;
import com.project01.reactspring.respository.CustomRepository.CustomerRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.List;


@Repository
public class CustomerRepositoryCustomImpl implements CustomerRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    public void joinTable(CustomerResponseDTO customerResponseDTO, StringBuilder sql){
        if(customerResponseDTO.getStaffid()!=null){
            sql.append("inner join assignmentcustomer ab on ab.customerid=c.id ");
        }
    }


    public void queryNomal(CustomerResponseDTO customerResponseDTO,StringBuilder where) throws IllegalAccessException {
        Field[] fields = customerResponseDTO.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object object = field.get(customerResponseDTO);
            if(object != null && !object.equals("")) {
                if(field.getType().getName().equals("java.lang.Long")) {
                    if(fieldName.equals("staffid")){
                        where.append(" and ab."+fieldName+"="+object);
                    }
                    else{
                        where.append(" and c."+fieldName+"="+object);
                    }

                }
                else if(field.getType().getName().equals("java.lang.String")){
                    where.append(" and c."+fieldName+" like '%"+object+"%' ");
                }
            }
        }
    }

    @Override
    public Page<CustomerEntity> search(CustomerResponseDTO customerResponseDTO, Pageable pageable) throws IllegalAccessException {
        StringBuilder sql=new StringBuilder("select c.* from customer c ");
        joinTable(customerResponseDTO,sql);

        StringBuilder pagiSql=new StringBuilder("select count(c.id) from customer c ");
        joinTable(customerResponseDTO,pagiSql);

        StringBuilder where=new StringBuilder("where 1=1 ");
        queryNomal(customerResponseDTO,where);
        sql.append(where.append(" and status = 1"));
        pagiSql.append(where.append(" and status = 1"));

        Query query=entityManager.createNativeQuery(sql.toString(),CustomerEntity.class);
        Query totalItem=entityManager.createNativeQuery(pagiSql.toString());

        Number number=(Number)totalItem.getSingleResult();

        //Bo qua so ban ghi
        Query offset=query.setFirstResult((int)pageable.getOffset());
        Query limit=query.setMaxResults((int)pageable.getPageSize());

        List<CustomerEntity> list = query.getResultList();

        return new PageImpl<>(list,pageable,number.longValue());
    }
}
