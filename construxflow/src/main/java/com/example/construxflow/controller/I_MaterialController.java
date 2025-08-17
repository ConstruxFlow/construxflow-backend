package com.example.construxflow.controller;

import com.example.construxflow.dto.I_MaterialDTO;
import com.example.construxflow.entity.I_Material;
import com.example.construxflow.service.I_MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api/inventory")
public class I_MaterialController {

    @Autowired
    private I_MaterialService materialService;

    @PostMapping("/add")
    public I_Material addMaterial(@RequestBody I_MaterialDTO dto) {
        return materialService.addMaterial(dto);
    }


    @GetMapping("/all")
    public List<I_Material> getAllMaterials() {
        return materialService.getAllMaterials();
    }

}
