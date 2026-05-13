package com.muhou.backend.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public class CreatePropRequest {

    @NotBlank(message = "name is required")
    private String name;
    private String image;
    private String imageUrl;
    private List<String> images;
    private String style;
    private String type;
    @NotBlank(message = "size is required")
    private String size;
    private BigDecimal lengthCm;
    private BigDecimal widthCm;
    private BigDecimal heightCm;
    @NotBlank(message = "material is required")
    private String material;
    @NotNull(message = "price is required")
    private BigDecimal price;
    @NotNull(message = "deposit is required")
    private BigDecimal deposit;
    @NotBlank(message = "fireResistantOption is required")
    private String fireResistantOption;
    @NotBlank(message = "weight is required")
    private String weight;
    @NotBlank(message = "transportSuggestion is required")
    private String transportSuggestion;
    private String qrCodeId;
    @NotBlank(message = "style is required")
    public String getStyle() { return style; }
    public void setStyle(String style) { this.style = style; }
    @NotBlank(message = "type is required")
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public List<String> getImages() { return images; }
    public void setImages(List<String> images) { this.images = images; }
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public BigDecimal getLengthCm() { return lengthCm; }
    public void setLengthCm(BigDecimal lengthCm) { this.lengthCm = lengthCm; }
    public BigDecimal getWidthCm() { return widthCm; }
    public void setWidthCm(BigDecimal widthCm) { this.widthCm = widthCm; }
    public BigDecimal getHeightCm() { return heightCm; }
    public void setHeightCm(BigDecimal heightCm) { this.heightCm = heightCm; }
    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public BigDecimal getDeposit() { return deposit; }
    public void setDeposit(BigDecimal deposit) { this.deposit = deposit; }
    public String getFireResistantOption() { return fireResistantOption; }
    public void setFireResistantOption(String fireResistantOption) { this.fireResistantOption = fireResistantOption; }
    public String getWeight() { return weight; }
    public void setWeight(String weight) { this.weight = weight; }
    public String getTransportSuggestion() { return transportSuggestion; }
    public void setTransportSuggestion(String transportSuggestion) { this.transportSuggestion = transportSuggestion; }
    public String getQrCodeId() { return qrCodeId; }
    public void setQrCodeId(String qrCodeId) { this.qrCodeId = qrCodeId; }
}
