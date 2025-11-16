package com.example.assetservice.service;

import com.example.assetservice.exception.ResourceNotFoundException;
import com.example.assetservice.model.Asset;
import com.example.assetservice.model.AssetDTO;
import com.example.assetservice.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssetService {

    @Autowired
    private AssetRepository assetRepository;  // ← Corregido aquí

    @Autowired
    private com.example.assetservice.service.FileStorageService fileStorageService;

    // CREATE - Crear asset con archivo
    public AssetDTO createAsset(String name, String description, MultipartFile file) {  // ← Corregido aquí

        // Guardar archivo
        String fileName = fileStorageService.storeFile(file);
        String category = fileStorageService.getFileCategory(file.getContentType());

        // Crear entidad
        Asset asset = new Asset();
        asset.setName(name);
        asset.setDescription(description);
        asset.setFileName(file.getOriginalFilename());
        asset.setFilePath(fileName);
        asset.setCategory(category);

        // Guardar en BD
        Asset savedAsset = assetRepository.save(asset);

        // Convertir a DTO y retornar
        return convertToDTO(savedAsset);
    }

    // Método auxiliar para convertir Asset a AssetDTO
    private AssetDTO convertToDTO(Asset asset) {
        AssetDTO dto = new AssetDTO();
        dto.setId(asset.getId());
        dto.setName(asset.getName());
        dto.setDescription(asset.getDescription());
        dto.setFileName(asset.getFileName());
        dto.setFilePath(asset.getFilePath());
        dto.setCategory(asset.getCategory());
        return dto;
    }

    // READ - Obtener todos los assets
    public List<AssetDTO> getAllAssets() {
        return assetRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // READ - Obtener asset por ID
    public AssetDTO getAssetById(Long id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset no encontrado con id: " + id));
        return convertToDTO(asset);
    }

    // UPDATE - Actualizar asset
    public AssetDTO updateAsset(Long id, String name, String description, MultipartFile file) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset no encontrado con id: " + id));

        asset.setName(name);
        asset.setDescription(description);

        // Si hay un nuevo archivo, actualizarlo
        if (file != null && !file.isEmpty()) {
            String fileName = fileStorageService.storeFile(file);
            String category = fileStorageService.getFileCategory(file.getContentType());

            asset.setFileName(file.getOriginalFilename());
            asset.setFilePath(fileName);
            asset.setCategory(category);
        }

        Asset updatedAsset = assetRepository.save(asset);
        return convertToDTO(updatedAsset);
    }

    // DELETE - Eliminar asset
    public void deleteAsset(Long id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset no encontrado con id: " + id));

        assetRepository.delete(asset);
    }

    // Buscar por categoría
    public List<AssetDTO> getAssetsByCategory(String category) {
        return assetRepository.findByCategory(category).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    // Método para actualizar asset CON archivo nuevo
    public AssetDTO updateAssetWithFile(Long id, String name, String description, MultipartFile file) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset no encontrado con id: " + id));

        asset.setName(name);
        asset.setDescription(description);

        // Si hay un nuevo archivo, actualizarlo
        if (file != null && !file.isEmpty()) {
            // Opcional: Eliminar archivo anterior
            if (asset.getFilePath() != null) {
                try {
                    fileStorageService.deleteFile(asset.getFilePath());
                } catch (Exception e) {
                    // Log el error pero continúa
                    System.err.println("No se pudo eliminar el archivo anterior: " + e.getMessage());
                }
            }

            // Guardar nuevo archivo
            String fileName = fileStorageService.storeFile(file);
            String category = fileStorageService.getFileCategory(file.getContentType());

            asset.setFileName(file.getOriginalFilename());
            asset.setFilePath(fileName);
            asset.setCategory(category);
        }

        Asset updatedAsset = assetRepository.save(asset);
        return convertToDTO(updatedAsset);
    }
}