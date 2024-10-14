package com.project01.reactspring.respository.CustomRepository.Impl;

import com.project01.reactspring.dto.request.BuildingRequestDTO;
import com.project01.reactspring.dto.response.BuildingResponseDTO;
import com.project01.reactspring.entity.BuildingEntity;
import com.project01.reactspring.respository.CustomRepository.BuildingRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Repository
public class BuildingRepositoryCustomImpl implements BuildingRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;


    public void joinTable(BuildingRequestDTO buildingRequestDTO,StringBuilder sql){
        if(buildingRequestDTO.getStaffId()!=null){
            sql.append("inner join assignmentbuilding ab on ab.buildingid=b.id ");
        }
        if(buildingRequestDTO.getAreaFrom()!=null || buildingRequestDTO.getAreaTo()!=null){
            sql.append("inner join rentarea re on re.buildingid=b.id ");
        }
    }

    public void queryNomal(BuildingRequestDTO buildingRequestDTO,StringBuilder query){
        Field[] fields = buildingRequestDTO.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(buildingRequestDTO);
                if(value!=null && !value.equals("")){
                    String name=field.getName();
                    if(!name.startsWith("area") && !name.startsWith("rentPrice") && !name.equals("typeCode") && !name.equals("staffId")){
                        if(field.getType().getName().equals("java.lang.Long")){
                            query.append("and b."+name+" = "+value+" ");
                        }
                        else{
                            query.append("and b."+name+" like '%"+value+"%' ");
                        }
                    }

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void querySepcial(BuildingRequestDTO buildingRequestDTO,StringBuilder query) {

        Long staffId=buildingRequestDTO.getStaffId();
        if(staffId!=null){
            query.append(" and ab.staffid = "+staffId+" ");
        }

        Long rentAreaFrom=buildingRequestDTO.getAreaFrom();
        Long rentAreaTo=buildingRequestDTO.getAreaTo();
        if(rentAreaFrom!=null || rentAreaTo!=null){
            if(rentAreaFrom!=null){
                query.append(" and re.value >= "+rentAreaFrom+" ");
            }
            if(rentAreaTo!=null){
                query.append(" and re.value <= "+rentAreaTo+" ");
            }
        }

        Long rentPriceFrom=buildingRequestDTO.getRentPriceFrom();
        Long rentPriceTo=buildingRequestDTO.getRentPriceTo();
        if(rentPriceFrom != null || rentPriceTo != null) {
            if(rentPriceFrom != null) {
                query.append(" and b.rentprice >= "+rentPriceFrom+" ");
            }
            if(rentPriceTo != null) {
                query.append(" and b.rentprice <= "+rentPriceTo+" ");
            }
        }

        List<String> typeCode=buildingRequestDTO.getTypeCode();
        if(typeCode!=null && typeCode.size()!=0){
            query.append(" and ( ");
            String queryTypeCode= typeCode.stream().map(item->" b.type like '%"+item.toString()+"%' ").collect(Collectors.joining("or"));
            query.append(queryTypeCode);
            query.append(" ) ");
        }
        else{
            typeCode=new ArrayList<String>();
        }


    }



    @Override
    public Page<BuildingEntity> searchBuilding(BuildingRequestDTO buildingRequestDTO, Pageable pageable){
        StringBuilder sql=new StringBuilder("select b.* from building b ");
        joinTable(buildingRequestDTO,sql);

        StringBuilder totalItemQuery=new StringBuilder("select count(distinct b.id) from building b ");
        joinTable(buildingRequestDTO,totalItemQuery);

        StringBuilder query=new StringBuilder(" where 1=1 ");
        queryNomal(buildingRequestDTO,query);
        querySepcial(buildingRequestDTO,query);
        sql.append(query);
        sql.append(" group by b.id ");
        Query nativeQuery = entityManager.createNativeQuery(sql.toString(),BuildingEntity.class);

        //Phân Trang Lấy tổng Item
        totalItemQuery.append(query);
        Query exicuteTotalItem = entityManager.createNativeQuery(totalItemQuery.toString());

        Number totalItems = (Number) exicuteTotalItem.getSingleResult();

        Query offSet = nativeQuery.setFirstResult((int) pageable.getOffset());  // Bắt đầu từ bản ghi thứ bao nhiêu
        Query Limit=nativeQuery.setMaxResults(pageable.getPageSize());
        List<BuildingEntity> list = nativeQuery.getResultList();

        // Trả về kết quả dưới dạng Page
        return new PageImpl<>(list,pageable,totalItems.longValue());
    }
}
