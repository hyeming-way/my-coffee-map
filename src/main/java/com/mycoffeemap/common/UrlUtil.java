package com.mycoffeemap.common;

import org.springframework.stereotype.Component;

@Component("urlUtil")
public class UrlUtil {

	
	// 페이징을 위한 함수
    public String buildQueryParams(Long beanId, Long cafeId) {
        StringBuilder query = new StringBuilder();

        if (beanId != null) {
            query.append("&beanId=").append(beanId);
        }
        if (cafeId != null) {
            query.append("&cafeId=").append(cafeId);
        }

        return query.toString();
    }
}
