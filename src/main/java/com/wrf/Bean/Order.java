package com.wrf.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @program: bookStore
 * @description: 订单
 * @author: Rifu Wu
 * @create: 2022-05-19 21:50
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private String orderId;
    private LocalDateTime createTime;
    private BigDecimal price;
    //订单状态，0未发货，1已发货
    private Integer status;
    private Integer userId;
}