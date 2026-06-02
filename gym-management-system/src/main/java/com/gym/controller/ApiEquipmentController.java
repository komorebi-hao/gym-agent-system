package com.gym.controller;

import com.gym.pojo.Equipment;
import com.gym.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/equipment")
public class ApiEquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    @GetMapping("/selEquipment")
    public Map<String, Object> selectEquipment() {
        List<Equipment> equipmentList = equipmentService.findAll();
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("equipmentList", equipmentList);
        return resp;
    }

    @PostMapping("/delEquipment")
    public ResponseEntity<Map<String, Object>> deleteEquipment(Integer equipmentId) {
        equipmentService.deleteByEquipmentId(equipmentId);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/toUpdateEquipment")
    public Map<String, Object> toUpdateEquipment(Integer equipmentId) {
        List<Equipment> equipmentList = equipmentService.selectByEquipmentId(equipmentId);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("equipmentList", equipmentList);
        return resp;
    }

    @GetMapping("/toAddEquipment")
    public Map<String, Object> toAddEquipment() {
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        return resp;
    }

    @PostMapping("/updateEquipment")
    public ResponseEntity<Map<String, Object>> updateEquipment(Equipment equipment) {
        equipmentService.updateEquipmentByEquipmentId(equipment);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/addEquipment")
    public ResponseEntity<Map<String, Object>> addEquipment(Equipment equipment) {
        equipmentService.insertEquipment(equipment);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        return ResponseEntity.ok(resp);
    }
}

