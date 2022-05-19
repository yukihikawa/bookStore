package com.wrf.Bean;

import com.wrf.Bean.CartItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @program: bookStore
 * @description: 购物车
 * @author: Rifu Wu
 * @create: 2022-05-17 22:02
 **/

@Component
@Slf4j
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

    /**
     * 删除商品项
     * @param id
     */
    public void deleteItem(Integer id){
        items.remove(id);
    }

    /**
     * 清空购物车
     */
    public void clear(){
        items.clear();
    }

    /**
     * 更新购物车
     * @param id
     * @param count
     */
    public void updateCount(Integer id, Integer count){
        //查看购物车中是否已有
        CartItem item = items.get(id);

        if(item != null){
            item.setCount(count);
            BigDecimal total = item.getPrice().multiply(new BigDecimal(item.getCount()));
            item.setTotalPrice(total);
        }
    }


    /**
     * 获取购物车中总数
     * @return
     */
    public Integer getTotalCount(){
        Integer totalCount = 0;
        for (Map.Entry<Integer, CartItem> entry : items.entrySet())
            totalCount += entry.getValue().getCount();

        return totalCount;
    }


    /**
     * @return 购物车中总价
     */
    public BigDecimal getTotalPrice(){
        BigDecimal totalPrice = new BigDecimal(0);
        for(Map.Entry<Integer, CartItem> entry : items.entrySet())
            totalPrice = totalPrice.add(entry.getValue().getTotalPrice());

        return totalPrice;
    }


    /**
     * @return 购物车中对象
     */
    public Map<Integer, CartItem> getItems(){
        return items;
    }


    /**
     * @param items
     */
    public void setItems(Map<Integer, CartItem> items){
        this.items = items;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "totalCount=" + getTotalCount() +
                ", totalPrice=" + getTotalPrice() +
                ", items=" + items +
                "}";
    }
}