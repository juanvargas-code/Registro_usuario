
package com.example.assetservice.model;

public class AssetDTO {
    private Long id;
    private String name;
    private String description;
    private String fileName;
    private String filePath;
    private String category;

    // Constructor vacío
    public AssetDTO() {
    }

    // Constructor con todos los campos
    public AssetDTO(Long id, String name, String description, String fileName,
                    String filePath, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.fileName = fileName;
        this.filePath = filePath;
        this.category = category;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
