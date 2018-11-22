package com.example.abhishek.bakingapp.dataHolder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Awesome Pojo Generator
 */
public class Ingredients {
    @SerializedName("quantity")
    @Expose
    private float quantity;
    @SerializedName("measure")
    @Expose
    private String measure;
    @SerializedName("ingredient")
    @Expose
    private String ingredient;

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getMeasure() {
        return measure;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getIngredient() {
        return ingredient;
    }
}