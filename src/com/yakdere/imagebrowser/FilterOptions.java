package com.yakdere.imagebrowser;

import java.io.Serializable;

public class FilterOptions implements Serializable {
	 /**
	 * 
	 */
	private static final long serialVersionUID = -1872160013276016515L;
	private String size;
    private String color;
    private String type;
    private String site;

    public FilterOptions(String size, String color, String type, String site) {
            this.size = size;
            this.color = color;
            this.type = type;
            this.site = site;
    }

    public String getSize() {
            return size;
    }

    public String getColor() {
            return color;
    }

    public String getType() {
            return type;
    }

    public String getSite() {
            return site;
    }
}
