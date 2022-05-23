package com.wrf.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @program: bookStore
 * @description: 购物车商品项
 * @author: Rifu Wu
 * @create: 2022-05-17 20:15
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    private Integer id;
    private String name;
    private Integer count;
    private BigDecimal price;
    private BigDecimal totalPrice;
}