package com.leaf8.zishao.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单项结构
 * 
 * @author 紫韶 
 * @date Oct 22, 2013
 */
public class ZsMenuItem {
    
    private String id;
    private String name;
    private List<ZsMenuItem> sub = new ArrayList<ZsMenuItem>();
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public List<ZsMenuItem> getSub() {
        return sub;
    }

    public void setSub(List<ZsMenuItem> sub) {
        this.sub = sub;
    }
}
