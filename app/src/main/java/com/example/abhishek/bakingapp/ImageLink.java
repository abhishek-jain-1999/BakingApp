package com.example.abhishek.bakingapp;

import java.util.ArrayList;
import java.util.List;

public class ImageLink {

    public static final String image1="https://assets.epicurious.com/photos/54ac95e819925f464b3ac37e/master/pass/51229210_nutella-pie_1x1.jpg";
    public static final String image2="https://img.taste.com.au/kdMbjArS/taste/2010/01/chocolate-brownies-118925-2.jpg";
    public static final String image3="https://d3cizcpymoenau.cloudfront.net/images/28462/SFS_yellow_layer_cake-195.jpg";
    public static final String image4="https://assets.marthastewart.com/styles/wmax-1500/d28/new-york-cheesecake-mhlb2030/new-york-cheesecake-mhlb2030_horiz.jpg?itok=OVfIi-EA";

    private static List<String> images=new ArrayList<>(4);

    public static void setImages() {
        images.add("");
        images.add("");
        images.add("");
        images.add("");

        images.set(0,image1);
        images.set(1,image2);
        images.set(2,image3);
        images.set(3,image4);
    }

    public static List<String> getImages() {
        return images;
    }
}
