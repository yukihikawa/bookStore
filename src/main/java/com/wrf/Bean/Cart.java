package com.wrf.Bean;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @program: bookStore
 * @description: 购物车
 * @author: Rifu Wu
 * @create: 2022-05-17 22:02
 **/

public class Cart {
    private Map<Integer, CartItem> items = new LinkedHashMap<>();


    /**添加商品
     * @param cartItem
     */
    public void addItem(CartItem cartItem){
        CartItem item = items.get(cartItem.getId());

        if(item == null)
            //不存在则添加
            items.put(cartItem.getId(), cartItem);
        else {
            //更新数量和总价
            item.setCount(item.getCount() + 1);
            BigDecimal total = item.getPrice().multiply(new BigDecimal(item.getCount()));
            item.setTotalPrice(total);
        }
    }

    public void deleteItem(Integer id){
        items.remove(id);
    }

    public void updateCount(Integer id, Integer count){

    }
}