package com.project01.reactspring.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaginationDTO <T> {

    List<T> data=new ArrayList<>();
    private int currentPage;
    private int pageSize;
    private long totalPages;
    private long totalItem;

}
