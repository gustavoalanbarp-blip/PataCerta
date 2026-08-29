package com.patacerta.app.data.remote.model;

import com.google.gson.annotations.SerializedName;

/**
 * Modelo parcial da resposta de GET /v1/breeds (TheDogAPI).
 * Usado para preencher a raça sugerida no cadastro do pet (RF02).
 */
public class Breed {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("life_span")
    private String lifeSpan;

    @SerializedName("temperament")
    private String temperament;

    @SerializedName("reference_image_id")
    private String referenceImageId;

    public int getId() { return id; }
    public String getName() { return name; }
    public String getLifeSpan() { return lifeSpan; }
    public String getTemperament() { return temperament; }
    public String getReferenceImageId() { return referenceImageId; }
}
