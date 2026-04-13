package com.example.myapplication.models;

public class CropPrice {
    public String name;
    public String price;

    public CropPrice() {} // Needed for Firestore

    public CropPrice(String name, String price) {
        this.name = name;
        this.price = price;
    }
}